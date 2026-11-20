# Agentic AI testing

An agentic QA pipeline chooses its next action from the evidence collected so far. Evaluate three things independently: the system under test, the agent's decision and the side effect its tool actually produced. A correct explanation paired with the wrong scheduled job is a pipeline failure.

## Worked release: a provider changes an order event

A provider proposes making a formerly required field optional. Its unit tests pass. A receipt consumer may still require the field, and a support assistant retrieves order records to answer customer questions. Start with the changed contract, provider commit, known consumer versions and the release policy. The following is a synthetic design exercise.

| Stage | Agent responsibility | Evidence required to proceed |
|---|---|---|
| Plan | Map the field to known consumers and affected journeys. State where the dependency graph is incomplete. | Versioned impact list and risk rationale |
| Authorize | Route the proposed action to the owner when risk or scope exceeds delegated authority. | Approval tied to the plan, commit and permitted environment |
| Execute | Run independent checks in parallel. Keep unavailable consumers visible. | Per-check artifact, exact revision, outcome and coverage gaps |
| Repair | Propose a bounded change after diagnosing the failure. | Failing fixture before repair, passing fixture after repair and an unaffected control |
| Learn | Store the validated failure signature, repair scope and conditions for reuse. | Reviewed evidence and a regression case that detects recurrence |
| Decide | Report blocking failures, missing evidence and the owner of the next action. | Reproducible verdict linked to the run and policy version |

## Route tests across the delivery lifecycle

| Change signal | Agent-selected checks | Decision rule for this exercise |
|---|---|---|
| Pricing or eligibility rule | Unit boundaries, functional decision table, acceptance example | Block on a reproducible mismatch with the approved rule. |
| Endpoint or event schema | API behavior, integration replay, cross-repository consumer contracts | A failing required consumer blocks. A missing consumer run leaves compatibility unresolved. |
| Checkout UI or text | Browser journey, accessibility, localization, device compatibility | Require the intended state transition and required keyboard journey. Route subjective usability findings to a reviewer. |
| Schema migration or batch job | Reconciliation by key, duplicate checks, rollback fixture | Hold when totals agree but records are assigned to the wrong customer. |
| Permission or logging policy | Role-matrix fixtures, privacy and log-redaction assertions | A denied action must leave state intact and omit restricted fields from output. |
| Capacity or retry policy | Load, endurance, reliability and recovery checks | Keep latency and recovery criteria separate. A fast retry that commits twice fails correctness. |
| Retriever, model or prompt | Retrieval relevance, grounded answers, task success, fairness and robustness slices | Inspect slice sizes and labeled failures. Route sparse or disputed evidence to review. |
| Broad or poorly specified change | Smoke suite, risk-selected regression, exploratory charter | Record which paths were exercised and assign unresolved acceptance questions to a person. |

## Resolve conflicting evidence

Suppose the provider passes 40 checks, the receipt consumer fails one contract, and the assistant evaluation has no examples with the field absent. A majority vote would mask the known break. Block on the required consumer contract, add a labeled missing-field case for the assistant, and rerun both on the proposed fix. A passing rerun on a different provider revision cannot close the original evidence gap.

For variable model outputs, repeat a fixed evaluation to observe verdict dispersion. Record the distribution alongside the fixture and model version. Repeating a run cannot supply a missing consumer schema. Classify that gap as missing scope and obtain the artifact. When complete evidence still permits competing interpretations, route the case to the domain owner and retain the adjudication.

For a conformal escalation policy, separate calibration cases from evaluation cases. Inspect empirical coverage, escalation rate and sample count within each uncertainty class. Treat a new model, a changed consumer population or a scarcely represented class as a reason to reassess calibration. A nominal coverage level is a population-level target, not a probability that one release is safe.

## Test the orchestrator before trusting its verdict

Replay synthetic tool results with a scheduler that records calls. Deliver a completion event twice: the next job should be scheduled once. Return a stale artifact: the agent should reject the revision mismatch. Exhaust the repair budget: the run should stop and name a reviewer. Withhold approval: verify from the call log that the restricted action never ran. Score the selected action and the recorded tool calls separately.

## Capstone

Submit an impact map, risk-routed plan, three evidence artifacts from different testing areas, one negative control and a final decision record. Include a deliberately missing artifact. A peer should be able to replay the decision, name the blocking condition and identify the smallest new observation that could change the verdict.

## Research reading path

Kavita Jadhav's six-paper IJECS series (2026) addresses six engineering decisions. The exercises here are teaching scenarios.

| Decision | Paper and reference implementation |
|---|---|
| Which actions need human approval before execution? | [1. Autonomous CI/CD Quality Assurance Using LangGraph Multi-Agent Orchestration and Risk-Proportionate Human-in-the-Loop Control](https://doi.org/10.18535/ijecs/v15i06.5563) · [Code](https://github.com/K11-Software-Solutions/k11techlab-agentic-ai-qa-system) |
| Which model behaviors should gate a pull request? | [2. LLM Safety as a Quality Gate: Integrating Bias, Fairness, and Robustness Evaluation into CI/CD via GitHub Apps](https://doi.org/10.18535/ijecs.v15i07.5576) · [Code](https://github.com/K11-Software-Solutions/llm-eval-agent-app) |
| When has a proposed repair earned reuse? | [3. Beyond Static Gates: Closing the Detect–Fix–Learn Loop in Agentic CI/CD Quality Assurance](https://doi.org/10.18535/ijecs.v15i07.5577) |
| Which consumers need revalidation after a provider change? | [4. Cross-Repository Contract Testing and Change Impact Analysis in Microservice CI/CD: An Agentic AI Approach](https://doi.org/10.18535/ijecs.v15i07.5599) · [Code](https://github.com/K11-Software-Solutions/k11techlab-microservice-qa-system) |
| How much variability lies behind a verdict? | [5. Beyond Binary Verdicts: Aleatoric Uncertainty Quantification in Agentic CI/CD Quality Pipelines](https://doi.org/10.18535/ijecs.v15i07.5614) |
| Which uncertainty source should trigger which review? | [6. Beyond a Single Threshold: Uncertainty Source Classification and Type-Stratified Conformal Prediction for Agentic CI/CD](https://doi.org/10.18535/ijecs.v15i08.5684) |

[Autonomous QA pipelines module](../../../../QA%20Automation%20Mastery%20System/modules/15-autonomous-qa-pipelines.md) · [Agentic performance lab](../../../../practice-qa-projects/performance-testing-jmeter/10-agentic-qa-for-performance.md) · [AI-augmented testing](../ai-augmented-testing/README.md) · [Fundamentals guide](../README.md)
