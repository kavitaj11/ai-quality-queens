"""Run the JMeter plan in non-GUI mode, build the HTML dashboard, then analyze the results.

Works on Windows, macOS and Linux. Finds JMeter through the JMETER_HOME environment
variable or the PATH.

Usage:
    python run_test.py --users 20 --rampup 20 --duration 60
    python run_test.py --users 100 --rampup 60 --duration 180 --name step-100 --p95-ms 800
"""
import argparse
import json
import os
import shutil
import subprocess
import sys
import urllib.request
from datetime import datetime
from pathlib import Path

TUTORIAL_DIR = Path(__file__).resolve().parent.parent
DEFAULT_PLAN = TUTORIAL_DIR / "jmeter" / "shop_load_test.jmx"
RESULTS_DIR = TUTORIAL_DIR / "results"

sys.path.insert(0, str(Path(__file__).resolve().parent))
from analyze_jtl import analyze, print_report  # noqa: E402


def find_jmeter():
    home = os.environ.get("JMETER_HOME")
    names = ["jmeter.bat", "jmeter.cmd"] if os.name == "nt" else ["jmeter"]
    if home:
        for name in names:
            candidate = Path(home) / "bin" / name
            if candidate.exists():
                return str(candidate)
    for name in names:
        found = shutil.which(name)
        if found:
            return found
    sys.exit("JMeter not found. Install it and set JMETER_HOME, or put its bin folder on PATH.")


def check_target(host, port):
    """Fail fast if the app under test is not the one we expect (a wrong port gives a run full of 404s)."""
    url = f"http://{host}:{port}/health"
    try:
        with urllib.request.urlopen(url, timeout=5) as resp:
            if json.loads(resp.read()).get("status") == "UP":
                return
    except Exception:
        pass
    sys.exit(f"{url} did not answer {{\"status\": \"UP\"}}. Start the demo app first, or check --host/--port.")


def run(users, rampup, duration, name=None, plan=DEFAULT_PLAN, host="127.0.0.1", port=8099,
        p95_ms=None, max_error_pct=None, extra_props=None):
    """Run one test and return the analysis summary (a dict)."""
    check_target(host, port)
    name = name or f"{datetime.now():%Y%m%d-%H%M%S}-u{users}"
    # Never overwrite an earlier run (JMeter also refuses to write a dashboard into a non-empty folder).
    out_dir, n = RESULTS_DIR / name, 2
    while out_dir.exists():
        out_dir, n = RESULTS_DIR / f"{name}-{n}", n + 1
    out_dir.mkdir(parents=True)
    jtl = out_dir / "results.jtl"

    props = {"users": users, "rampup": rampup, "duration": duration, "host": host, "port": port,
             **(extra_props or {})}
    cmd = [find_jmeter(), "-n", "-t", str(plan), "-l", str(jtl), "-j", str(out_dir / "jmeter.log"),
           "-e", "-o", str(out_dir / "dashboard"),
           "-Jjmeter.save.saveservice.output_format=csv"]
    cmd += [f"-J{key}={value}" for key, value in props.items()]
    print("Running:", " ".join(cmd))
    completed = subprocess.run(cmd, cwd=plan.parent)
    if completed.returncode != 0 or not jtl.exists():
        sys.exit(f"JMeter failed (exit {completed.returncode}); see {out_dir / 'jmeter.log'}")

    summary = analyze(jtl, p95_ms, max_error_pct)
    summary["run"] = {"name": name, **props}
    (out_dir / "summary.json").write_text(json.dumps(summary, indent=2), encoding="utf-8")
    print_report(summary)
    print(f"\nHTML dashboard: {out_dir / 'dashboard' / 'index.html'}")
    return summary


def main():
    parser = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    parser.add_argument("--users", type=int, default=20)
    parser.add_argument("--rampup", type=int, default=20, help="seconds to start all users")
    parser.add_argument("--duration", type=int, default=60, help="test length in seconds")
    parser.add_argument("--name", help="results folder name (default: timestamp)")
    parser.add_argument("--host", default="127.0.0.1")
    parser.add_argument("--port", type=int, default=8099)
    parser.add_argument("--p95-ms", type=float)
    parser.add_argument("--max-error-pct", type=float)
    args = parser.parse_args()
    summary = run(args.users, args.rampup, args.duration, args.name, host=args.host, port=args.port,
                  p95_ms=args.p95_ms, max_error_pct=args.max_error_pct)
    sys.exit(0 if summary["slo_passed"] else 1)


if __name__ == "__main__":
    main()
