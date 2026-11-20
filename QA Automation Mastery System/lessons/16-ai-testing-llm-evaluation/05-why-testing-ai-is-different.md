# Why testing AI is different

[16 · Module topics](../../modules/16-ai-testing-llm-evaluation.md) · [Curriculum index](../../README.md)

**Category** · FUNDAMENTALS OF TESTING AI

Traditional software is deterministic: same input → same output, so assertEquals works. LLM systems are non-deterministic and open-ended — the same prompt can yield different, equally-valid wordings. You can't assert exact strings; you evaluate along quality dimensions (correctness, relevance, safety) over a dataset, and track scores as distributions, not pass/fail.

## Worked reference

````text
DETERMINISTIC SOFTWARE  vs  AI / LLM SYSTEMS

  TRADITIONAL                    LLM-BASED
  same input → same output       same input → varied output
  assertEquals(actual, expected) grade against a rubric/metric
  pass / fail                    score 0..1, track a distribution
  one correct answer             many acceptable answers
  bug = wrong output             "bug" = quality drift, drop in score

WHAT CHANGES FOR THE TESTER
  • no exact-match asserts → evaluate DIMENSIONS:
      correctness · relevance · faithfulness (no hallucination)
      · tone/format · safety · latency · cost
  • test over a DATASET of cases, not a single example
  • results are SCORES over a test set, gated on thresholds
      (e.g. "faithfulness ≥ 0.9 across 200 cases")
  • flakiness is inherent → set temperature=0 for repeatability,
      run N samples and look at the mean/variance
  • new failure modes: hallucination, prompt injection, bias,
      jailbreaks, PII leakage, regression after a prompt/model change

THE TWO THINGS YOU TEST
  1. THE SYSTEM AROUND the model  (deterministic — test normally):
     API contracts, retries, timeouts, tool calls, guardrails,
     RAG retrieval plumbing, UI. Your existing skills apply here.
  2. THE MODEL'S OUTPUT QUALITY    (non-deterministic — EVALUATE):
     this is "LLM evaluation" / "evals" — the next module.
````

## Exercise

Take one LLM feature (e.g. a support-reply generator) and list which of its behaviors you'd test with a hard assertion vs. a graded eval.
