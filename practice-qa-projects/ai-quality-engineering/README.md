# AI Quality Engineering: Conversational and Agentic Systems

Verification figures below describe the reference lab runs. For checks performed on this copy and remaining environment requirements, see [validation notes](../VALIDATION.md).

**Level:** intermediate–advanced · **Study time:** 6–8 hours plus exercises  
**Last reviewed:** November 18, 2026 · **Next review:** February 18, 2027

Build and explain an evaluation pipeline for a conversational support assistant that retrieves policy documents, handles multi-turn booking requests, and calls a backend service.

The [runnable project](project/README.md) uses Python's standard library and a real loopback HTTP service. It runs without credentials, installs, GPUs, or paid model calls. The default assistant is explicitly a **rule-based test double with seeded response variation**. An optional **live LLM-as-a-judge adapter** sends the synthetic conversations to the OpenAI API when you choose it. The offline lab verifies evaluation machinery, not the quality or safety of a production LLM.

## Start here

From the repository root, using Python 3.11 or newer:

```bash
cd practice-qa-projects/ai-quality-engineering/project
python -m unittest discover -s tests -v
python -m aiqe run --repeats 3 --seed 7 --output artifacts/current
python -m aiqe compare --baseline data/baseline.json --candidate artifacts/current/report.json
python -m aiqe calibrate --judge fixture
```

Open `artifacts/current/summary.md` for the result, `report.json` for per-turn evidence, and `traces.jsonl` for correlated spans. All paths in later chapters assume this `project/` directory unless stated otherwise. Use `python3` if that is your machine's Python command.

**Verified locally:** 24 automated tests; 20 golden conversations × 3 seeds = 60 passing rollouts; all 10 deliberate defect modes rejected. The real judge's transport, schema handling, and failure paths were tested with mocked HTTP responses. No paid live model call was made. See [verification notes](VERIFICATION.md) for the exact scope.

## Chapters

| Chapter | What you build or investigate |
|---|---|
| [1. Architecture and setup](01-architecture-and-setup.md) | Separate the system under test, independent oracles, judge, backend, and release gate |
| [2. Golden and synthetic datasets](02-golden-and-synthetic-data.md) | Curated multi-turn cases, personas, provenance, review and split hygiene |
| [3. Metrics and non-determinism](03-metrics-and-nondeterminism.md) | Faithfulness, completeness, hallucination proxies, outcome checks, repeats and uncertainty |
| [4. LLM-as-a-judge and calibration](04-llm-judge-and-calibration.md) | Rubrics, strict structured results, agreement, MAE, weighted kappa and abstention |
| [5. Adversarial safety and guardrails](05-adversarial-safety.md) | Direct/indirect injection, jailbreaks, PII canaries, authorization and over-refusal |
| [6. Multi-turn tools and backend contracts](06-multiturn-tools-and-backends.md) | Consent, state transitions, duplicate prevention, schema drift and failures |
| [7. Traces and failure localization](07-traces-and-triage.md) | Follow one conversation through retrieval, generation, tools and connectors |
| [8. CI gates and change impact](08-ci-and-release-gates.md) | Absolute thresholds, checked-in baselines, negative controls and release decisions |
| [9. Technical practice and exercises](09-practice-and-exercises.md) | Architecture answers, debugging drills, a practical lab assessment and a study plan |

## Capabilities and implementation

| Requirement | Concrete evidence in this project |
|---|---|
| Automated AI evaluation pipelines | `aiqe/pipeline.py`, `scoring.py`, JSON/Markdown reports and per-category gates |
| Faithfulness, completeness, hallucination, task completion | Lexical grounding proxies explicitly separated from the live semantic rubric and backend-state oracles |
| Rubric-based LLM-as-a-judge | `judge.py`, `data/rubric.json`, 16 illustrative calibration records and development/holdout agreement reports |
| Guardrail teaming and safety assessments | Six adversarial conversations plus benign controls, privacy canaries and ten injected defect modes |
| Multi-turn dialogues and tool use | Pending appointment, changed constraints, confirmation, cancellation and repeated confirmation |
| Backend service integration | Real HTTP booking service; consent, authorization, idempotency, 503 and malformed-response scenarios |
| Trace-based root-cause investigation | Trace IDs, parent span IDs, propagated `traceparent`, sanitized attributes and triage hypotheses |
| Synthetic data generation | Seeded persona expansion, scenario-aware fingerprints, lineage and an explicit unreviewed-candidate boundary |
| CI/CD regression gates | [GitHub Actions workflow](ci/ai-qe.yml), baseline comparison and failure artifacts |

## Evaluation boundaries and limitations

The assistant is a transparent test double, the support policies are fictional, and all examples use synthetic information. Phrase matching is deliberately narrow; it is not a factuality classifier. The canary/email detector is not comprehensive PII detection. The trace recorder is custom JSON, not a production OpenTelemetry exporter. Calibration labels and fixture predictions are teaching examples, not a human study or measured model benchmark. The tiny strict baseline is an engineering regression check, not a production safety certification.

To evaluate a real assistant, replace the `DemoAgent.reply(message)` boundary, retain independent backend verification, and map your retrieval/tool telemetry into the evaluator. Chapter 9 specifies the work and acceptance criteria rather than pretending that integration already exists.


