# 9. Technical practice and exercises

## Architecture design summary

Define the quality contract around user journeys and observable outcomes. Evaluate groundedness, completeness, safety, context retention and authorized task completion separately. Combine reviewed golden conversations, deterministic checks, independent backend oracles and a calibrated rubric-based LLM judge. Retain prompt, model, retrieval and connector versions with trace evidence. In CI, use absolute risk thresholds and compatible baseline comparisons, with human review for high-impact or uncertain failures.

## Technical review questions

**How do you measure hallucination?** Define the claim unit and admissible evidence first. Separate unsupported claims from contradicted claims and retrieval failures. Report the denominator and uncertainty. Use semantic adjudication with human-calibrated labels; this lab's sentence matcher only demonstrates the plumbing.

**How do you calibrate an LLM judge?** Create independently rated examples across score levels and failure categories, resolve disagreements, tune the rubric on development data, and measure agreement/MAE/weighted kappa on a frozen holdout. Inspect false acceptance of severe defects, not just mean agreement. Version the judge with its rubric and datasets.

**How do you test an agent that uses tools?** Validate planning, arguments, authorization, consent, ordering, retry behavior and actual backend state. Check intermediate side effects. Repeat confirmations and simulate lost acknowledgments. A plausible final response does not establish task success.

**What makes non-deterministic testing reliable?** Stable scenario definitions and oracles, repeated samples with all failures retained, versioned inputs, paired comparisons, slice-level results, uncertainty estimates and explicit review rules. Randomness should affect sampling, not the definition of correctness.

**How do you locate failures?** Follow a trace from retrieval to generation to tools and downstream services. Reproduce the earliest mismatch at that boundary; hold other inputs fixed to distinguish competing causes.

**How would you prevent evaluator gaming?** Keep labels out of model inputs, treat evaluated content as untrusted, use independent action/data oracles, blind model identity, retain hidden holdouts, and test the judge with answer-borne scoring instructions.

## A 90-minute practical assessment

| Time | Task | Acceptance criterion |
|---|---|---|
| 0–15 min | Run the baseline and explain two metrics | Correct denominators and acknowledged proxy limitations |
| 15–35 min | Add a new multi-turn interruption scenario | Independent per-turn state oracle; documented product decision |
| 35–50 min | Introduce and detect an unauthorized action | Failure demonstrated by stored state, not only output text |
| 50–65 min | Inspect the failure trace | Correct boundary identified; one controlled follow-up experiment |
| 65–80 min | Evaluate a judge disagreement | Explain rubric ambiguity, annotation needs and holdout implications |
| 80–90 min | Give a release recommendation | Evidence, severity, uncertainty and owner of the next action |

## Extension projects

**Real assistant adapter:** implement the same message boundary against an authorized development endpoint. Preserve session IDs, collect actual retrieval/tool telemetry, and use an independent read-only backend verifier. Contract-test transport errors and schema changes. Never pass test expectations to the assistant. Record model/prompt/index versions from the deployment rather than hard-coded lab values.

**Semantic evaluation:** replace illustrative calibration labels with independent human ratings, expand the holdout and test judge-injection robustness. Show both per-dimension performance and severe-error acceptance rate before using the judge for release decisions.

**Distributed resilience:** add commit-then-timeout, delayed consistency and concurrent confirmation. Prove exactly-once business effects through idempotency and reconciliation, rather than assuming transport retries are safe.

**Observability:** export real SDK spans to an OTLP collector and connect conversation, retrieval, model and service operations. Verify redaction, retention and access controls with synthetic privacy fixtures.

## Five-session study plan

Session 1: setup, architecture and metrics. Session 2: golden datasets and multi-turn/backend tests. Session 3: adversarial cases and benign controls. Session 4: judge contracts, calibration and trace triage. Session 5: CI, the practical assessment and an architecture walkthrough.

Return to the [tutorial index](README.md).
