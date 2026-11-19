# Verification scope

Reference verification: November 18, 2026, Linux, Python **3.12.14**. See the collection [validation notes](../VALIDATION.md) for checks performed after copying these projects.

| Check | Observed result |
|---|---|
| `python -m unittest discover -s tests -v` | 24 tests passed |
| Golden evaluation | 20 cases × 3 seeds = 60 rollouts, 90 turns; all passed |
| Deliberate defects | All 10 fault modes rejected by the gate in regression tests |
| CLI positive control | Normal evaluation and baseline comparison exit 0 |
| CLI negative control | Hallucination evaluation and comparison exit 1 |
| Candidate boundary | Unreviewed input rejected with exit 2 unless explicitly allowed |
| Candidate generation | 24 unique scenario fingerprints, reproducible with seed 7 |
| Calibration fixture | 16 illustrative records scored; development and holdout reported separately |
| Backend contracts | Consent, account isolation, slots, idempotency conflict and response validation exercised |
| HTTP correlation | Client/server trace parent relationship verified |
| Judge failure behavior | Mocked timeout, truncation, invalid scores and label exclusion verified |
| Report privacy | Synthetic canary/email detected before redaction and absent from saved artifacts |

The baseline summary in `project/data/baseline.json` was produced by the normal offline run, not invented target values. Its latency is an observation of this local rule-based app and loopback service, not LLM performance. Baseline comparison does not gate on that latency.

No paid live judge call was executed. No production model, real customer data or external backend was evaluated. Python 3.11 and 3.13 are specified in the optional GitHub Actions template. No remote matrix run is claimed for this copy.

The calibration dataset and fixture scores are explicitly illustrative. Their agreement numbers demonstrate calculations; they are not an independent human evaluation or a certification of any judge. The offline faithfulness and hallucination fields are lexical proxies.
