# Conversational AI QE lab

Python 3.11+ · Standard library only · No installation required

The practice app, **Orbit Support**, answers questions from four fictional support documents and books synthetic appointments through a local HTTP service. These fictional policies are fixtures, not real product guidance.

## Run

```bash
python -m unittest discover -s tests -v
python -m aiqe run --output artifacts/current
python -m aiqe compare
python -m aiqe generate --count 24 --seed 7 --output artifacts/candidates.jsonl
python -m aiqe calibrate --judge fixture
python -m aiqe inspect --report artifacts/current/report.json --case book-repeat
```

The default run uses 20 golden cases and seeds 7, 8 and 9. Each conversation gets a fresh assistant and backend; state persists only between turns of that conversation. The HTTP server binds to loopback on an OS-assigned port and closes automatically.

Exit codes: **0** = command succeeded / evaluation gate passed; **1** = quality or comparison gate failed; **2** = configuration, data or command error. `calibrate` reports agreement; it does not certify a judge or apply a production calibration threshold.

## Prove the gate catches defects

```bash
python -m aiqe run --fault hallucination --output artifacts/negative
python -m aiqe compare --candidate artifacts/negative/report.json
```

Both commands must exit **1**. The suite tests all fault modes: `hallucination`, `incomplete`, `citation`, `retrieval`, `memory`, `unsafe`, `pii`, `consent`, `duplicate`, and `overrefusal`. `--fault none` is the normal implementation. Fault modes deliberately alter the test double, not a real service.

## Optional live semantic judge

In your own shell, set `OPENAI_API_KEY` and `AIQE_JUDGE_MODEL` to a model available to your account that supports Chat Completions with strict JSON Schema. `.env.example` documents the variable names; files are not auto-loaded. Do not commit keys.

```bash
python -m aiqe calibrate --judge live --output artifacts/live-calibration.json
python -m aiqe run --judge live --repeats 1 --output artifacts/live
```

These are explicit paid API calls: calibration makes **16** requests; a one-repeat golden run makes **20**. No model name is hard-coded. Choose and pin a supported snapshot, review cost, and use a trusted synthetic dataset. The live judge receives redacted transcripts, retrieved evidence and verified backend outcomes; it never receives reference labels or fixture predictions. A timeout, refusal, truncated reply, invalid schema, low confidence, or score below 3 prevents a passing evaluation. The judge's self-reported confidence is a review signal, not a calibrated probability.

The fixed adapter endpoint is `https://api.openai.com/v1/chat/completions`. Its schema is documented by [OpenAI's structured-output guide](https://developers.openai.com/api/docs/guides/structured-outputs?api-mode=chat). Mocked adapter tests do not prove your selected live model supports every parameter. This repository does not execute live judging in CI.

## Files

| Path | Responsibility |
|---|---|
| `aiqe/agent.py` | Seeded rule-based assistant and controlled defects |
| `aiqe/backend.py` | Real HTTP connector and independent observable service state |
| `aiqe/datasets.py` | Case validation and unreviewed persona candidates |
| `aiqe/scoring.py` | Deterministic predicates, intervals, gates and baseline comparison |
| `aiqe/judge.py` | Optional live structured LLM judge with strict response validation |
| `aiqe/pipeline.py` | Evaluation orchestration, calibration and report writing |
| `aiqe/telemetry.py` | Redaction, trace IDs, spans and file fingerprints |
| `data/golden.jsonl` | 20 curated dialogues with independent per-turn expectations |
| `data/knowledge.json` | Fictional source documents available to the assistant |
| `data/calibration.jsonl` | 8 development + 8 holdout illustrative labeled examples |
| `data/baseline.json` | Measured offline baseline summary, not an auto-updating target |
| `data/rubric.json`, `data/thresholds.json` | Versioned rubric and lab gate settings |
| `tests/test_lab.py` | Regression, backend contract, negative-control and judge tests |

Generated reports go to ignored `artifacts/`. Report scoring happens before redaction, so a leaked synthetic canary still fails the gate even though saved files hide it. The redactor covers only the lab's canary and email format; production data needs an appropriate privacy policy and detector.

## Update a baseline deliberately

Never replace `data/baseline.json` in the same automation that decides whether a candidate regressed. After reviewing a legitimate dataset or rubric change, create a fresh report, review every changed case, then copy only `manifest`, `metrics`, `slices` and `gate` into a proposed baseline and review that diff. Preserve the old baseline in Git. Dataset, rubric, threshold, seed, case-set or judge-identity changes make comparison fail as incomparable; a model or prompt under test may change and must be evaluated.

Back to the [tutorial](../README.md).
