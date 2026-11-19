# 7. Traces and failure localization

A failed answer is a symptom. Find the earliest boundary where the observed execution diverges from the expected behavior.

The lab emits `agent.turn`, `guardrail.input`, `retrieval.search`, `model.generate`, `planner.book`, `tool.book`, `connector.book` and optional `evaluation.judge` spans. Span names appear only when that path executes. Tool calls propagate a W3C-style `traceparent` header; the server records the client span as its parent. A test verifies this relationship.

```bash
python -m aiqe run --fault retrieval --output artifacts/retrieval-negative
python -m aiqe inspect --report artifacts/retrieval-negative/report.json --case returns
```

Read the per-turn predicates, retrieved document IDs, source evidence and span status. In this example, empty retrieval precedes the answer failure; the model did not receive the needed source. The report's `triage_hypotheses` are clues, not an automated claim of root cause.

| Evidence | Hypothesis | Next controlled experiment |
|---|---|---|
| Correct document absent | Retrieval, indexing, filter or query issue | Replay retrieval against a known fixture and inspect ranking/filter decisions |
| Correct evidence present, unsupported answer | Prompt/model generation issue | Hold evidence constant; compare prompt and model versions separately |
| Wrong tool or arguments | Planning, context retention or tool-schema issue | Replay the conversation and validate arguments before execution |
| Tool HTTP error or schema mismatch | Connector/backend issue | Reproduce directly against the service contract |
| Answer claims success, backend unchanged | Result interpretation or failed action | Check request ID, status, commit outcome and retries |
| Judge changes while assistant output is fixed | Evaluator drift or unreliable rubric | Re-score fixed labeled examples and compare dimensions |

## Production observability

Correlate conversation, turn, retrieval, model, tool and service operations. Track model and prompt versions, retrieval-index version, document IDs, latency, token usage when available, retry counts and evaluator identity. Keep user text and retrieved content out of telemetry by default; use access-controlled sampling and redaction when content capture is justified.

This project writes custom JSON spans. It does not install the OpenTelemetry SDK, export OTLP, or claim schema compliance. Use [OpenTelemetry's GenAI conventions](https://opentelemetry.io/docs/specs/semconv/gen-ai/) as a production integration reference; the project currently links to the maintained GenAI conventions repository. Pin the convention version you adopt because naming and stability can change.

**Can traces distinguish a bad prompt from a bad model?** They narrow the failure location but usually cannot prove causality alone. Use fixed-input replays, versioned configurations, controlled comparisons and targeted human review.

**Why not log everything?** Raw conversations can contain sensitive data and attacker-controlled material. Capture the minimum evidence required for diagnosis, preserve secure correlation IDs and control retention/access.

Exercise: compare `returns` under `retrieval` and `hallucination` defects. Write a three-sentence defect report containing the failed criterion, trace evidence and one falsifiable next experiment.

Next: [CI and release gates](08-ci-and-release-gates.md).

