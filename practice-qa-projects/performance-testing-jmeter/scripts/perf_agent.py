"""A small agentic QA loop for performance testing: find the capacity of the demo app.

The agent repeats  plan -> act -> observe -> decide  until it has an answer:
  plan     choose the next load step (more users) from what it has seen so far
  act      run JMeter at that load (run_test.py)
  observe  read the results and the server's /metrics endpoint
  decide   continue, stop because the SLO broke (the knee), or stop at a guardrail

The decision logic is plain, readable rules so the agent is deterministic and needs no
API key. With --ai, Claude writes the final narrative from the evidence the agent gathered
(see ai_summary.py); the agent's verdict itself never depends on the model.

Guardrails (what keeps an agent safe to run):
  * only targets 127.0.0.1 / localhost unless you pass --allow-host
  * hard cap on users and on the number of steps
  * asks you before every step unless you pass --yes
  * writes everything it did to agent_report.md, so a person can review it

Usage:
    python perf_agent.py --p95-ms 800 --max-error-pct 1
    python perf_agent.py --start-users 25 --max-users 300 --step-duration 90 --yes --ai
"""
import argparse
import json
import sys
import urllib.request
from datetime import datetime
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))
import run_test  # noqa: E402

LOCAL_HOSTS = {"127.0.0.1", "localhost", "::1"}


def server_metrics(host, port, reset=False):
    query = "?reset=true" if reset else ""
    try:
        with urllib.request.urlopen(f"http://{host}:{port}/metrics{query}", timeout=5) as resp:
            return json.loads(resp.read())
    except Exception as exc:  # metrics are helpful, not essential
        return {"error": f"could not read /metrics: {exc}"}


def decide(history, args):
    """Return (action, reason). Actions: 'continue', 'stop-knee', 'stop-guardrail'."""
    last = history[-1]
    if not last["slo_passed"]:
        return "stop-knee", f"SLO broke at {last['users']} users: {'; '.join(last['slo_failures'])}"
    if len(history) >= 2:
        prev = history[-2]
        user_growth = last["users"] / prev["users"]
        tput_growth = last["throughput_rps"] / max(prev["throughput_rps"], 0.01)
        # Users grew but throughput did not keep up: the system is saturating even if the SLO still holds.
        if user_growth >= 1.5 and tput_growth < 1 + (user_growth - 1) * 0.5:
            return "stop-knee", (f"throughput grew only {tput_growth:.2f}x while users grew {user_growth:.2f}x "
                                 f"({prev['throughput_rps']} -> {last['throughput_rps']} req/s)")
    if len(history) >= args.max_steps:
        return "stop-guardrail", f"reached the step limit ({args.max_steps})"
    if next_users(last["users"], args) > args.max_users:
        return "stop-guardrail", f"next step would exceed --max-users {args.max_users}"
    return "continue", "SLO met and throughput still scaling with load"


def next_users(current, args):
    return int(current * args.growth)


def approve(step_users, args):
    if args.yes:
        return True
    answer = input(f"Run step with {step_users} users for {args.step_duration}s? [y/N] ").strip().lower()
    return answer == "y"


def write_report(history, verdict, reason, args, out_dir):
    lines = [
        "# Performance agent report",
        "",
        f"Date: {datetime.now():%Y-%m-%d %H:%M}  ",
        f"Target: http://{args.host}:{args.port}  ",
        f"SLO: p95 <= {args.p95_ms} ms per transaction, error rate <= {args.max_error_pct}%",
        "",
        "## Steps",
        "",
        "| Step | Users | Req/s | p95 overall (ms) | Errors % | SLO | Pool waiting (max) | Avg pool wait (ms) |",
        "|---|---|---|---|---|---|---|---|",
    ]
    for i, h in enumerate(history, 1):
        m = h["server_metrics"]
        lines.append(f"| {i} | {h['users']} | {h['throughput_rps']} | {h['p95_ms']:.0f} | {h['error_pct']} | "
                     f"{'pass' if h['slo_passed'] else 'FAIL'} | {m.get('max_waiting_seen', 'n/a')} | "
                     f"{m.get('avg_pool_wait_ms', 'n/a')} |")
    passing = [h for h in history if h["slo_passed"]]
    capacity = passing[-1]["users"] if passing else None
    lines += [
        "",
        "## Verdict",
        "",
        f"- Decision: **{verdict}**",
        f"- Reason: {reason}",
        f"- Highest load that met the SLO: **{capacity if capacity else 'none of the steps'} users**",
        "",
        "## Decision log",
        "",
    ]
    lines += [f"- After {h['users']} users: {h['decision']} ({h['reason']})" for h in history]
    lines += ["", "Review the per-step HTML dashboards in the results folder before acting on this report.", ""]
    report = out_dir / "agent_report.md"
    report.write_text("\n".join(lines), encoding="utf-8")
    return report


def main():
    parser = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    parser.add_argument("--host", default="127.0.0.1")
    parser.add_argument("--port", type=int, default=8099)
    parser.add_argument("--allow-host", action="store_true", help="allow a non-local target (only systems you own)")
    parser.add_argument("--start-users", type=int, default=25)
    parser.add_argument("--growth", type=float, default=2.0, help="multiply users by this each step")
    parser.add_argument("--max-users", type=int, default=400)
    parser.add_argument("--max-steps", type=int, default=6)
    parser.add_argument("--step-duration", type=int, default=90, help="seconds per step")
    parser.add_argument("--p95-ms", type=float, default=800)
    parser.add_argument("--max-error-pct", type=float, default=1.0)
    parser.add_argument("--yes", action="store_true", help="do not ask before each step")
    parser.add_argument("--ai", action="store_true", help="ask Claude for a narrative diagnosis at the end")
    args = parser.parse_args()

    if args.host not in LOCAL_HOSTS and not args.allow_host:
        sys.exit(f"Refusing to load-test {args.host}. Only test systems you own; pass --allow-host to confirm.")

    session = f"agent-{datetime.now():%Y%m%d-%H%M%S}"
    out_dir = run_test.RESULTS_DIR / session
    out_dir.mkdir(parents=True, exist_ok=True)
    history, users = [], args.start_users
    verdict, reason = "stop-guardrail", "no steps were run"

    while True:
        print(f"\n=== PLAN: step {len(history) + 1} at {users} users ===")
        if not approve(users, args):
            verdict, reason = "stop-guardrail", "a person declined the next step"
            break
        rampup = max(10, args.step_duration // 3)
        server_metrics(args.host, args.port, reset=True)  # count this step only
        summary = run_test.run(users, rampup, args.step_duration, name=f"{session}/step-{users}",
                               host=args.host, port=args.port, p95_ms=args.p95_ms,
                               max_error_pct=args.max_error_pct)
        metrics = server_metrics(args.host, args.port)
        history.append({
            "users": users,
            "throughput_rps": summary["overall"]["throughput_rps"],
            "p95_ms": summary["overall"]["p95_ms"],
            "error_pct": summary["overall"]["error_pct"],
            "slo_passed": summary["slo_passed"],
            "slo_failures": summary["slo_failures"],
            "server_metrics": metrics,
            "summary_path": str(out_dir / f"step-{users}" / "summary.json"),
        })
        action, why = decide(history, args)
        history[-1].update(decision=action, reason=why)
        print(f"=== OBSERVE: {history[-1]['throughput_rps']} req/s, p95 {history[-1]['p95_ms']:.0f} ms, "
              f"server pool waiting max {metrics.get('max_waiting_seen', 'n/a')} ===")
        print(f"=== DECIDE: {action} ({why}) ===")
        if action != "continue":
            verdict, reason = action, why
            break
        users = next_users(users, args)

    (out_dir / "server_metrics.json").write_text(
        json.dumps({f"{h['users']} users": h["server_metrics"] for h in history}, indent=2), encoding="utf-8")
    report = write_report(history, verdict, reason, args, out_dir)
    print(f"\nAgent report: {report}")

    if args.ai and history:
        import ai_summary
        prompt = ai_summary.build_prompt([h["summary_path"] for h in history],
                                         json.loads((out_dir / "server_metrics.json").read_text(encoding="utf-8")))
        (out_dir / "prompt.md").write_text(prompt, encoding="utf-8")
        analysis = ai_summary.ask_claude(prompt)
        (out_dir / "ai_analysis.md").write_text(analysis + "\n", encoding="utf-8")
        print(f"\nAI narrative saved to {out_dir / 'ai_analysis.md'}")


if __name__ == "__main__":
    main()
