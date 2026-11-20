# Eval gates with GitHub Actions

[16 · Module topics](../../modules/16-ai-testing-llm-evaluation.md) · [Curriculum index](../../README.md)

**Category** · EVALS IN CI/CD

The workflow-file approach: YAML in .github/workflows runs your eval harness (Promptfoo, DeepEval, pytest) on pull_request, on runners you configure, with API keys in repo secrets. A failing threshold fails the job; branch protection makes the job a required check. Simple, transparent, versioned with the code — the default starting point.

## Worked reference

````text
# The gate itself is just pytest + a threshold
import json

def test_smoke_set_pass_rate():
    results = [run_case(c) for c in load_dataset("smoke")]
    pass_rate = sum(r.passed for r in results) / len(results)

    # aggregate gate — robust to single-case noise
    assert pass_rate >= 0.90, (
        f"pass rate {pass_rate:.2f} < 0.90; "
        f"regressions: {[r.id for r in results if not r.passed]}")

def test_no_regression_vs_baseline():
    baseline = json.load(open("evals/baseline.json"))  # from main
    current = score_dataset("smoke")
    for metric, prev in baseline.items():
        assert current[metric] >= prev - 0.05, (
            f"{metric} regressed: {prev} -> {current[metric]}")
````

## Exercise

Write a workflow that runs your smoke eval set only when files under prompts/ change, and fails the check when pass-rate < 0.9.
