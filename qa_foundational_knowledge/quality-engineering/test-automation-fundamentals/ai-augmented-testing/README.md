# AI-augmented testing

An assistant can suggest cases, summarize failures and identify gaps. The engineer supplies the **oracle**: the rule, contract, observation or human judgment that determines whether a result is acceptable. By the end of this guide you should be able to reject a plausible generated assertion and replace it with one that detects a real failure.

## Choose evidence for the testing question

Use these examples at unit, component, integration, system or acceptance level. Smoke, exploratory and regression testing describe how you select or investigate cases and can apply across the rows.

| Testing area | Useful assistant task | Evidence the reviewer should require |
|---|---|---|
| Unit and functional | Derive boundary cases from a pricing rule | Hand-calculated outputs for values below, at and above the threshold |
| API and service integration | Generate a decision table for order retries | Status, response identity and exactly one persisted order after a repeated request |
| Consumer contracts | Identify consumers of a changed event field | Versioned contract and replay result for each affected consumer |
| UI and end-to-end | Propose a replacement for a missing locator | Accessible role, intended business action and resulting order state |
| Data, ETL and migrations | Suggest reconciliation checks | Counts by business key, duplicate/null rates and control totals before and after migration |
| Accessibility and usability | Draft keyboard journeys and observation prompts | Actual focus order, announced errors and observations from a person completing the task |
| Compatibility and localization | Select browser, viewport and locale combinations | Affected user path, layout evidence and correct currency, date and time-zone behavior |
| Security and privacy | Review permission-matrix coverage and logged fields | Expected allow/deny outcomes on synthetic records and redaction in captured logs |
| Performance and scalability | Summarize latency or throughput under load | Workload, sample count, endpoint, percentile and agreed limit |
| Reliability and recovery | Propose recovery invariants | Recovery time, reconciled state and duplicate counts after a controlled interruption |
| Exploratory and acceptance | Turn unresolved requirements into session charters | Session notes, observed behavior and the product owner's acceptance decision |
| AI, LLM and RAG | Draft a rubric and group failures by slice | Labeled examples, source support, retrieval relevance, subgroup counts and human adjudication |

## Worked review: the assertion that always passes

A service accepts an order submission twice with the same idempotency key. The assistant proposes asserting that both responses are successful. Run the proposed check against two synthetic traces. Trace A returns order 81 twice and persists one row. Trace B returns orders 81 and 82 and persists two rows. Both traces satisfy the proposed assertion. Only A satisfies the business rule. Revise the check to compare order identity and count committed records for the key. Save B as a counterexample when evaluating later drafts.

Apply the same reasoning to a UI repair. If a renamed button is matched by its position, the click might select “Save draft” instead of “Submit order.” Require a confirmed order and the expected confirmation message before accepting the locator change. For exploratory work, the assistant can suggest the question; a tester records what actually happened.

## Worked review: distinguish a measurement from an explanation

At 200 users, checkout p95 is 836 ms against an 800 ms target. The 36 ms excess is 4.5% of the target. A claim of a 4.5% regression needs a comparable earlier run. A claim of database saturation needs telemetry from that interval. Change the input to 780 ms: a faithful summary should report 20 ms of headroom. Keep both inputs as regression cases for the reporting assistant.

## Evaluate the assistant itself

Build a held-out set of reviewed cases, including correct changes, known regressions and genuinely ambiguous requirements. Compare a human-authored baseline with assistant-supported review on the same cases. Track missed regressions, false alarms, unsupported claims and reviewer time separately. Record prompt, model, fixture and rubric versions. A faster review that misses the duplicate-order trace exposes a trade-off that an average score can hide.

## Practice assignment

Choose three rows from different layers. For each, submit a risk, generated candidate, independent oracle, failing fixture and reviewer decision. Exchange fixtures with another learner. A completed review explains why the corrected check distinguishes the valid and invalid outcomes and which uncertainty still needs a person.

[Research reading path and six papers](../agentic-ai-testing/README.md#research-reading-path) · [AI-augmented QA module](../../../../QA%20Automation%20Mastery%20System/modules/14-ai-augmented-qa.md) · [Performance reporting lab](../../../../practice-qa-projects/performance-testing-jmeter/09-ai-augmented-performance-testing.md) · [Fundamentals guide](../README.md)
