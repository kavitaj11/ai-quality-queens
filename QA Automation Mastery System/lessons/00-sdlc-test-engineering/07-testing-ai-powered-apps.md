# Testing AI-powered apps

[00 · Module topics](../../modules/00-sdlc-test-engineering.md) · [Curriculum index](../../README.md)

**Category** · AI TESTING PROCESS

Most of an AI app is NOT the model — it's retrieval, prompt assembly, parsing, guardrails, fallbacks, and UI. Test that plumbing with ordinary deterministic tests; reserve expensive model-graded evals for the model's actual behavior. The app's failure modes are new: hallucination surfaced as fact, prompt injection through user content, degraded-model fallbacks, and cost/latency blowups.

## Worked reference

````text
TESTING AN AI-POWERED APP — test the system, not just the model

  user input
     │
  ┌──▼────────────────────────────────────┐
  │ APP LAYER — deterministic, test normally  │
  │  input validation · retrieval · prompt     │
  │  assembly · output parsing · guardrails ·  │
  │  fallbacks · caching · rate limits · UI    │
  └──────────────┬────────────────────────┘
  ┌──────────────▼────────────────────────┐
  │ MODEL — non-deterministic, use EVALS      │
  └──────────────────────────────────────┘

DETERMINISTIC TESTS (cheap — most of your suite)
  • malformed model output → parser survives (mock the model!)
  • model timeout / 500 → fallback UX shown, no data loss
  • retrieval returns right docs for a known query
  • guardrail blocks PII / injection BEFORE the model
  • token budget + cost caps enforced

MODEL EVALS (expensive — few, gated on aggregates)
  quality on golden set · tone · faithfulness · safety

NEW FAILURE MODES TO PLAN FOR
  hallucination shown as fact · injection via user content ·
  stale RAG index · cost/latency creep · model-version drift

RULE
  mock the model to test the app; freeze the app to eval
  the model. Never let one test try to do both.
````

## Exercise

For an AI email-drafting feature, split 12 test ideas into: deterministic app tests, model evals, and production monitors. What fraction needed an LLM to test?
