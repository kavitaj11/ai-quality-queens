"""AI-augmented analysis: turn one or more summary.json files into a diagnosis.

By default this only BUILDS the prompt and saves it to prompt.md, so you can read exactly
what would be shared and paste it into an approved AI assistant. Only aggregated numbers
are included: no raw requests, tokens, user names or response bodies.

With --call it sends the prompt to Claude (needs `pip install -r requirements-ai.txt` and an
ANTHROPIC_API_KEY or `ant auth login` profile) and saves the answer to ai_analysis.md.
Each call is billed to your Anthropic account.

Usage:
    python ai_summary.py ../results/step-25/summary.json ../results/step-100/summary.json
    python ai_summary.py ../results/*/summary.json --server-metrics metrics.json --call
"""
import argparse
import json
import sys
from pathlib import Path

MODEL = "claude-opus-5-5"

SYSTEM_PROMPT = """You are a senior performance engineer reviewing load test results for a junior QA engineer.
Use only the numbers provided. Do not invent metrics, causes you cannot support, or values that were not measured.
When the evidence is not enough to name a cause, say what extra measurement would settle it."""

INSTRUCTIONS = """Analyze these load test runs of the same application at different load levels.

Answer in this structure:
1. Verdict: did each run meet its SLO? One line per run.
2. Capacity: at roughly what load does throughput stop growing while response time climbs (the knee)? Cite the numbers.
3. Likely bottleneck: the most likely cause, with the evidence. Use the server metrics if present.
4. Next tests: two or three specific follow-up tests, each with what it would prove.
5. Caveats: anything in the data that limits these conclusions (short runs, errors, load generator limits).

Keep it under 350 words and write for a beginner."""


def compact(summary):
    """Keep only what the model needs; never send raw samples."""
    return {
        "run": summary.get("run", {}).get("name", summary.get("source")),
        "peak_users": summary["peak_users"],
        "duration_s": summary["duration_s"],
        "overall": summary["overall"],
        "transactions": summary["transactions"],
        "error_codes": summary["error_codes"],
        "slo": summary["slo"],
        "slo_failures": summary["slo_failures"],
        "timeline": summary["timeline"],
    }


def build_prompt(summary_paths, server_metrics=None):
    runs = [compact(json.loads(Path(p).read_text(encoding="utf-8"))) for p in summary_paths]
    runs.sort(key=lambda r: r["peak_users"])
    parts = [INSTRUCTIONS, "\n## Load test runs (aggregated)\n```json", json.dumps(runs, indent=2), "```"]
    if server_metrics:
        parts += ["\n## Server-side metrics captured after each run\n```json",
                  json.dumps(server_metrics, indent=2), "```"]
    return "\n".join(parts)


def ask_claude(prompt):
    try:
        import anthropic
    except ImportError:
        sys.exit("Install the SDK first: pip install -r requirements-ai.txt")

    client = anthropic.Anthropic()
    try:
        response = client.beta.messages.create(
            model=MODEL,
            max_tokens=16000,
            system=SYSTEM_PROMPT,
            output_config={"effort": "medium"},
            # If a safety classifier declines, retry on a fallback model chosen by the API.
            betas=["server-side-fallback-2026-07-01"],
            fallbacks="default",
            messages=[{"role": "user", "content": prompt}],
        )
    except TypeError as e:
        sys.exit(f"Your anthropic package is too old for this script ({e}). Run: pip install -r requirements-ai.txt")
    except anthropic.AuthenticationError:
        sys.exit("No valid Claude credentials. Set ANTHROPIC_API_KEY or run `ant auth login`.")
    except anthropic.RateLimitError:
        sys.exit("Rate limited by the API. Wait a minute and try again.")
    except anthropic.APIStatusError as e:
        sys.exit(f"Claude API error {e.status_code}: {e.message}")
    except anthropic.APIConnectionError:
        sys.exit("Could not reach the Claude API. Check your network connection.")

    if response.stop_reason == "refusal":
        sys.exit("The model declined this request.")
    return "\n".join(block.text for block in response.content if block.type == "text")


def main():
    parser = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    parser.add_argument("summaries", nargs="+", type=Path, help="summary.json files from analyze_jtl.py")
    parser.add_argument("--server-metrics", type=Path, help="JSON file of server metrics (optional)")
    parser.add_argument("--call", action="store_true", help="send the prompt to Claude")
    parser.add_argument("--out-dir", type=Path, default=Path("."), help="where to write prompt.md / ai_analysis.md")
    args = parser.parse_args()

    metrics = json.loads(args.server_metrics.read_text(encoding="utf-8")) if args.server_metrics else None
    prompt = build_prompt(args.summaries, metrics)
    args.out_dir.mkdir(parents=True, exist_ok=True)
    prompt_file = args.out_dir / "prompt.md"
    prompt_file.write_text(f"<!-- system -->\n{SYSTEM_PROMPT}\n\n<!-- user -->\n{prompt}\n", encoding="utf-8")
    print(f"Prompt saved to {prompt_file} ({len(prompt):,} characters). Review it before sharing.")

    if args.call:
        analysis = ask_claude(prompt)
        out = args.out_dir / "ai_analysis.md"
        out.write_text(analysis + "\n", encoding="utf-8")
        print(f"\n{analysis}\n\nSaved to {out}. Check every claim against the numbers before you report it.")


if __name__ == "__main__":
    main()
