# How LLM evals run in a pipeline

[16 · Module topics](../../modules/16-ai-testing-llm-evaluation.md) · [Curriculum index](../../README.md)

**Category** · EVALS IN CI/CD

Evals are a merge gate like any test suite — but non-determinism, token cost, and latency change the shape. Trigger on prompt/model/RAG-config changes; run a cheap smoke subset per PR and the full golden set nightly; compare scores to a stored baseline (regression = diff, not absolute); and because a single run is noisy, gate on aggregates over the dataset (pass-rate ≥ threshold), not one sample.

## Worked reference

````text
LLM EVALS IN CI/CD — the shape of the pipeline

WHAT TRIGGERS AN EVAL RUN
  • prompt template changed        (prompts/**)
  • model or params changed        (model id, temperature)
  • RAG config changed             (chunking, retriever, k)
  • eval dataset changed           (new golden cases)
  app code that never touches the LLM path → normal tests only

TIERED EXECUTION (cost control)
  per PR      smoke subset   ~20 cases   minutes   $
  nightly     full golden set  100s      ~1 hr     $$
  pre-release full + red-team suite                $$$

GATING — different from assert-based suites
  • single outputs are noisy → gate on AGGREGATES
      pass_rate >= 0.90 across dataset
      mean faithfulness >= 4.2 / 5
  • compare vs BASELINE stored from main
      fail on regression, not just absolute floor
  • cache: identical (prompt, model, case) → reuse result

ARTIFACTS every run should publish
  score report (per-metric, per-case) · regressions diff
  judge transcripts for failures · token cost of the run
````

## Exercise

Design the pipeline for a RAG chatbot: what runs on every PR, what runs nightly, and what score drop blocks a merge?
