# LLM app testing

[00 · Module topics](../../modules/00-sdlc-test-engineering.md) · [Curriculum index](../../README.md)

**Category** · AI TESTING PROCESS

LLM systems add their own process layer: prompts are code (versioned, reviewed, regression-tested), a golden dataset is the spec, judges + rubrics replace expected values, and red-teaming is mandatory. Every prompt, model, or RAG change triggers the eval suite — with aggregate thresholds as the merge gate. This topic is the process view; Phase 13 is the deep dive.

## Worked reference

````text
LLM APP TESTING — the process layer

PROMPTS ARE CODE
  versioned in git · reviewed in PRs · every change
  runs the eval suite · rollback-able · no hotfixing
  prompts in a dashboard nobody diffs

THE GOLDEN DATASET IS THE SPEC
  seeded from real logs + edge cases · every escaped
  bug becomes a permanent case · versioned next to
  the prompt it tests · owned like any test asset

JUDGES + RUBRICS REPLACE EXPECTED VALUES
  deterministic checks first (format, schema, must/
  must-not strings) → LLM-as-judge for open-ended
  quality → humans calibrate the judge (see Phase 13)

WHAT TRIGGERS A FULL EVAL RUN
  prompt change · model/version change · RAG config
  change · guardrail change · vendor silent update
  (pin versions; re-run on any bump)

DEFINITION OF DONE — a prompt change ships when:
  ✓ smoke evals green in CI (aggregate ≥ threshold)
  ✓ no metric regressed vs baseline beyond tolerance
  ✓ red-team suite still resists (injection, leakage)
  ✓ cost + latency within budget
  ✓ reviewer approved the diff — prompt AND scores

DEEP DIVE → Phase 13: AI Testing & LLM Evaluation
  (methods, judges, frameworks, CI/CD, red-teaming)
````

## Exercise

Write the definition-of-done for a prompt change in a production LLM feature: what must run, what must not regress, and who signs off?
