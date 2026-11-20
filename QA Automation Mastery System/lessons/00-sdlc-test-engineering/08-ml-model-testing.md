# ML model testing

[00 · Module topics](../../modules/00-sdlc-test-engineering.md) · [Curriculum index](../../README.md)

**Category** · AI TESTING PROCESS

Classic ML models (classifiers, rankers, forecasters) are tested on three fronts: DATA (schema, ranges, leakage, balance), MODEL QUALITY (held-out metrics vs thresholds, per-slice performance, fairness), and BEHAVIOR (invariance and directional expectations — change an irrelevant field, prediction shouldn't move). Then in production: drift detection and champion/challenger comparison.

## Worked reference

````text
ML MODEL TESTING — three fronts + production

1. DATA TESTS (before any training run)
   schema + types · value ranges · nulls · class balance ·
   duplicates · LEAKAGE (target info hiding in features —
   the classic silent killer) · train/test contamination
   tools: Great Expectations · pandera · dbt tests

2. MODEL QUALITY (held-out data, gated thresholds)
   overall     accuracy / F1 / AUC ≥ bar vs BASELINE
   PER SLICE   metrics by segment (region, device, tenure)
               — a model can be great on average and
               broken for a subgroup you care about
   fairness    parity of error rates across protected groups
   calibration predicted 0.8 → right ~80% of the time?

3. BEHAVIORAL TESTS (unit tests for the model)
   invariance    rename user, same prediction
   directional   income ↑ → credit score must not ↓
   minimum func  obvious cases the model must never miss
   metamorphic   f(translate(x)) ≈ f(x)

PRODUCTION
   drift monitors (input dist vs training — see
   Distributions lesson) · delayed-label quality tracking ·
   champion/challenger · rollback = redeploy OLD MODEL

GATE PATTERN (CI for models)
   new model → data tests → quality vs baseline → slice
   gates → behavioral suite → shadow traffic → promote
````

## Exercise

For a fraud-scoring model, write one test per front: a data test, an overall-metric gate, a slice gate, an invariance test, and a drift monitor. Which would have caught a real incident you know of?
