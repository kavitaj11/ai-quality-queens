"""Summarize a JMeter results file (.jtl, CSV format) and check it against SLOs.

Standard library only. Prints a per-transaction table, writes summary.json next to the
.jtl (used by ai_summary.py and perf_agent.py), and exits with code 1 if an SLO fails,
so the same script works as a CI gate.

Usage:
    python analyze_jtl.py results/run1/results.jtl
    python analyze_jtl.py results/run1/results.jtl --p95-ms 800 --max-error-pct 1
"""
import argparse
import csv
import json
import math
import sys
from collections import defaultdict
from pathlib import Path


def percentile(sorted_values, pct):
    """Nearest-rank percentile, the same idea JMeter's dashboard uses."""
    if not sorted_values:
        return 0.0
    rank = max(1, math.ceil(pct / 100 * len(sorted_values)))
    return float(sorted_values[rank - 1])


def load_samples(jtl_path):
    with open(jtl_path, newline="", encoding="utf-8") as f:
        reader = csv.DictReader(f)
        required = {"timeStamp", "elapsed", "label", "success"}
        missing = required - set(reader.fieldnames or [])
        if missing:
            sys.exit(f"{jtl_path} is missing columns {sorted(missing)}; save results as CSV with a header")
        for row in reader:
            yield {
                "ts": int(row["timeStamp"]),
                "elapsed": int(row["elapsed"]),
                "label": row["label"],
                "ok": row["success"].lower() == "true",
                "code": row.get("responseCode", ""),
                "threads": int(row.get("allThreads") or 0),
            }


def stats(samples, duration_s):
    times = sorted(s["elapsed"] for s in samples)
    errors = sum(1 for s in samples if not s["ok"])
    return {
        "count": len(samples),
        "error_pct": round(100 * errors / len(samples), 2) if samples else 0.0,
        "avg_ms": round(sum(times) / len(times), 1) if times else 0.0,
        "p50_ms": percentile(times, 50),
        "p90_ms": percentile(times, 90),
        "p95_ms": percentile(times, 95),
        "p99_ms": percentile(times, 99),
        "max_ms": float(times[-1]) if times else 0.0,
        "throughput_rps": round(len(samples) / duration_s, 2) if duration_s else 0.0,
    }


def timeline(samples, window_s):
    """Group samples into fixed windows: active users, throughput and p95 per window."""
    if not samples:
        return []
    start = min(s["ts"] for s in samples)
    buckets = defaultdict(list)
    for s in samples:
        buckets[(s["ts"] - start) // (window_s * 1000)].append(s)
    rows = []
    for idx in sorted(buckets):
        group = buckets[idx]
        times = sorted(s["elapsed"] for s in group)
        rows.append({
            "t_start_s": idx * window_s,
            "users": max(s["threads"] for s in group),
            "throughput_rps": round(len(group) / window_s, 1),
            "p95_ms": percentile(times, 95),
            "error_pct": round(100 * sum(1 for s in group if not s["ok"]) / len(group), 1),
        })
    return rows


def analyze(jtl_path, p95_ms=None, max_error_pct=None, window_s=10):
    samples = list(load_samples(jtl_path))
    if not samples:
        sys.exit(f"No samples in {jtl_path}")
    start = min(s["ts"] for s in samples)
    end = max(s["ts"] + s["elapsed"] for s in samples)
    duration_s = max((end - start) / 1000, 0.001)

    by_label = defaultdict(list)
    for s in samples:
        by_label[s["label"]].append(s)

    summary = {
        "source": str(jtl_path),
        "duration_s": round(duration_s, 1),
        "peak_users": max(s["threads"] for s in samples),
        "overall": stats(samples, duration_s),
        "transactions": {label: stats(group, duration_s) for label, group in sorted(by_label.items())},
        "error_codes": dict(sorted(
            {code: sum(1 for s in samples if not s["ok"] and s["code"] == code)
             for code in {s["code"] for s in samples if not s["ok"]}}.items())),
        "timeline": timeline(samples, window_s),
        "slo": {"p95_ms": p95_ms, "max_error_pct": max_error_pct},
    }

    failures = []
    if p95_ms is not None:
        for label, st in summary["transactions"].items():
            if st["p95_ms"] > p95_ms:
                failures.append(f"{label}: p95 {st['p95_ms']:.0f} ms > {p95_ms} ms")
    if max_error_pct is not None and summary["overall"]["error_pct"] > max_error_pct:
        failures.append(f"overall error rate {summary['overall']['error_pct']}% > {max_error_pct}%")
    summary["slo_failures"] = failures
    summary["slo_passed"] = not failures
    return summary


def print_report(summary):
    print(f"\nRun: {summary['source']}  duration {summary['duration_s']} s  peak users {summary['peak_users']}\n")
    header = f"{'Transaction':<24}{'Count':>7}{'Err%':>7}{'Avg':>8}{'p50':>8}{'p90':>8}{'p95':>8}{'p99':>8}{'Max':>8}{'Req/s':>8}"
    print(header)
    print("-" * len(header))
    rows = list(summary["transactions"].items()) + [("TOTAL", summary["overall"])]
    for label, st in rows:
        print(f"{label[:23]:<24}{st['count']:>7}{st['error_pct']:>7}{st['avg_ms']:>8.0f}{st['p50_ms']:>8.0f}"
              f"{st['p90_ms']:>8.0f}{st['p95_ms']:>8.0f}{st['p99_ms']:>8.0f}{st['max_ms']:>8.0f}{st['throughput_rps']:>8}")
    if summary["error_codes"]:
        print(f"\nError response codes: {summary['error_codes']}")
    print("\nTimeline (watch throughput stop growing while p95 climbs: that is the knee)")
    print(f"{'t(s)':>6}{'users':>7}{'req/s':>8}{'p95 ms':>9}{'err%':>7}")
    for row in summary["timeline"]:
        print(f"{row['t_start_s']:>6}{row['users']:>7}{row['throughput_rps']:>8}{row['p95_ms']:>9.0f}{row['error_pct']:>7}")
    if summary["slo"]["p95_ms"] is not None or summary["slo"]["max_error_pct"] is not None:
        print("\nSLO: " + ("PASSED" if summary["slo_passed"] else "FAILED"))
        for failure in summary["slo_failures"]:
            print(f"  - {failure}")


def main():
    parser = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    parser.add_argument("jtl", type=Path)
    parser.add_argument("--p95-ms", type=float, help="fail if any transaction's p95 is above this")
    parser.add_argument("--max-error-pct", type=float, help="fail if the overall error rate is above this")
    parser.add_argument("--window-s", type=int, default=10, help="timeline window size in seconds")
    args = parser.parse_args()

    summary = analyze(args.jtl, args.p95_ms, args.max_error_pct, args.window_s)
    print_report(summary)
    out = args.jtl.with_name("summary.json")
    out.write_text(json.dumps(summary, indent=2), encoding="utf-8")
    print(f"\nWrote {out}")
    sys.exit(0 if summary["slo_passed"] else 1)


if __name__ == "__main__":
    main()
