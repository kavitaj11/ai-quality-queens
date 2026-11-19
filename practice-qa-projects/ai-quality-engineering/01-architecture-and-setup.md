# 1. Architecture and setup

The engineering problem is deciding whether a change to a prompt, model, retrieval system or connector still satisfies the product's quality contract. A response can be fluent but unsupported, safe but unhelpful, or correct in text while the tool silently fails. Keep these outcomes separate.

## Build the boundaries

| Boundary | Input | Evidence it produces |
|---|---|---|
| Dataset → runner | Conversation and independent expectations | Case ID, category, dataset fingerprint |
| Runner → assistant | User message only; prior turns stay in its session | Answer and retrieved source records |
| Assistant → backend | Tool arguments and correlation header | HTTP response and actual stored booking |
| Runner → evaluators | Transcript, sources and backend state | Content, safety, contract and outcome scores |
| Runner → optional judge | Untrusted evidence plus a versioned rubric | Validated ordinal scores and confidence |
| Metrics → gate | Absolute thresholds and compatible baseline | Pass/fail with reasons and artifacts |

Read `aiqe/agent.py`, `backend.py` and `pipeline.py`. `DemoAgent` never receives `expect` or `expected_bookings_after`. The runner can inspect backend state independently. This avoids a circular test in which the same model generates the answer and declares its own success.

## First run

```bash
python --version
python -m unittest discover -s tests -v
python -m aiqe run --repeats 3 --seed 7
```

The application creates its own loopback service; there is no second terminal or manual server startup. A clean run produces 60 successful rollouts. Inspect the manifest in `artifacts/current/report.json`: it records data, knowledge, rubric and threshold hashes, seeds, model/prompt identifiers and tool-contract version.

## Exercise

Trace the `book-confirm` case through the code. Identify the exact line where a message becomes a tool request and the separate line where the evaluator checks the stored booking. Then run `--fault consent`. It must fail even if the final appointment looks correct, because the first turn's backend state was already wrong.

## Review questions

**How would you establish an AI quality framework from scratch?** Start with user journeys and failure severity; define measurable acceptance contracts and independent task oracles; curate representative cases; implement cheap deterministic checks; add calibrated semantic judges; validate the evaluators with known defects; then gate releases and learn from production failures.

**Why is this more than API automation?** API contracts remain necessary, but generated content and plans also need evidence-based scoring, multi-turn state checks, safety boundaries and repeated measurements. Existing API testing skills transfer directly to tool and connector verification.

Next: [Golden and synthetic datasets](02-golden-and-synthetic-data.md).

