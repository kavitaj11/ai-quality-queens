import argparse
import json
from pathlib import Path
import sys
from .agent import FAULTS
from .datasets import DATA, load_cases, synthesize
from .judge import JudgeError
from .pipeline import calibrate, run, write_report
from .scoring import compare


def main(argv=None):
    parser = argparse.ArgumentParser(description="Conversational AI QE practice lab")
    commands = parser.add_subparsers(dest="command", required=True)
    evaluate = commands.add_parser("run", help="run a seeded local evaluation")
    evaluate.add_argument("--dataset", type=Path, default=DATA/"golden.jsonl")
    evaluate.add_argument("--repeats", type=int, default=3)
    evaluate.add_argument("--seed", type=int, default=7)
    evaluate.add_argument("--fault", choices=FAULTS, default="none")
    evaluate.add_argument("--judge", choices=("none", "live"), default="none")
    evaluate.add_argument("--allow-candidates", action="store_true")
    evaluate.add_argument("--output", type=Path, default=Path("artifacts/current"))
    comparison = commands.add_parser("compare", help="compare compatible baseline and candidate reports")
    comparison.add_argument("--baseline", type=Path, default=DATA/"baseline.json")
    comparison.add_argument("--candidate", type=Path, default=Path("artifacts/current/report.json"))
    comparison.add_argument("--max-drop", type=float, default=.02)
    generate = commands.add_parser("generate", help="generate unreviewed synthetic candidates")
    generate.add_argument("--count", type=int, default=24)
    generate.add_argument("--seed", type=int, default=7)
    generate.add_argument("--output", type=Path, default=Path("artifacts/candidates.jsonl"))
    calibration = commands.add_parser("calibrate", help="compare judge scores with illustrative labels")
    calibration.add_argument("--dataset", type=Path, default=DATA/"calibration.jsonl")
    calibration.add_argument("--judge", choices=("fixture", "live"), default="fixture")
    calibration.add_argument("--output", type=Path, default=Path("artifacts/calibration.json"))
    inspect = commands.add_parser("inspect", help="inspect one case's redacted traces and scores")
    inspect.add_argument("--report", type=Path, default=Path("artifacts/current/report.json"))
    inspect.add_argument("--case", required=True)
    args = parser.parse_args(argv)
    try:
        if args.command == "run":
            result = run(args.dataset, args.repeats, args.seed, args.fault, args.judge, args.allow_candidates)
            write_report(result, args.output)
            print(json.dumps({"gate":result["gate"], "rollouts":result["metrics"]["rollouts"], "output":str(args.output)}))
            return 0 if result["gate"]["passed"] else 1
        if args.command == "compare":
            failures = compare(json.loads(args.baseline.read_text()), json.loads(args.candidate.read_text()), args.max_drop)
            print(json.dumps({"passed":not failures, "failures":failures}))
            return int(bool(failures))
        if args.command == "generate":
            candidates = synthesize(load_cases(DATA/"golden.jsonl"), args.count, args.seed)
            args.output.parent.mkdir(parents=True, exist_ok=True)
            args.output.write_text("".join(json.dumps(row)+"\n" for row in candidates), encoding="utf-8")
            print(f"Wrote {len(candidates)} unreviewed candidates to {args.output}")
        elif args.command == "calibrate":
            result = calibrate(args.dataset, args.judge)
            args.output.parent.mkdir(parents=True, exist_ok=True)
            args.output.write_text(json.dumps(result, indent=2, allow_nan=False)+"\n", encoding="utf-8")
            print(json.dumps(result))
        else:
            rows = [row for row in json.loads(args.report.read_text())["rows"] if row["case_id"] == args.case]
            if not rows:
                raise ValueError("case not found in report")
            print(json.dumps(rows, indent=2))
        return 0
    except (ValueError, OSError, KeyError, TypeError, JudgeError) as exc:
        print(f"Evaluation error ({type(exc).__name__}): {exc}", file=sys.stderr)
        return 2


if __name__ == "__main__":
    raise SystemExit(main())

