# Behavioral and hypothetical interview questions

> **Learning objective** · Explain a decision, your contribution and the evidence of its outcome.

For a behavioral question, choose a specific decision from your experience. Explain what you owned, what the team did, and what evidence showed the outcome. For a hypothetical question, state your assumptions and explain how you would check them.

## How would you plan a cross-team testing session?

Start with the decision the session should support. For example, a billing team may need to confirm that a customer can correct an invoice after a partial payment. Identify who can explain the billing rule, who can prepare the account, and who can investigate a discrepancy. Reserve the environment and agree on the expected balance before the session. Finish by recording the observed balance, any unresolved questions, and an owner for each follow-up.

## Describe two professional goals and how you evaluated progress.

Choose goals with observable outcomes. These examples illustrate the level of detail an interviewer can explore.

| Example goal | Action | Evidence to discuss |
|---|---|---|
| Make it easier for a second team to adopt an automation framework | Pilot a shared test-data fixture with one service, then ask another team to use it from the documentation alone. | Time to the first passing test, setup steps that required help, and changes made after the pilot. |
| Apply a new programming skill to a recurring QA task | Write a script that prepares test files and compare its output with reviewed sample files. | Preparation time, handling of malformed inputs, and whether another engineer can run the script independently. |

In an interview, report the results you actually observed. If no baseline was recorded, describe the evidence available and what you would measure next time.

## Describe a missed automation deadline. What would you change?

Consider a billing regression suite that depends on an upstream batch and a shared test account. A useful answer identifies when the dependency first put the estimate at risk, who needed that information, and what options were available. For example, the team could verify calculation rules at the service layer while reserving the browser test for the complete billing journey.

Explain which decision was yours and when you communicated the revised scope. Close with a concrete change to future planning, such as running one representative scenario with realistic data before estimating the remaining suite.
## Scenario question bank

For a hypothetical, state your assumptions and the observation that would change your answer. For an experience question, distinguish your contribution from the team's work.

### Test design and exploratory testing

| Interview question | What to cover in your answer |
|---|---|
| A requirement says search results should be ‘relevant.’ How would you make that testable? | Identify whose task matters, collect representative queries and agree on examples of useful and misleading results. |
| A form accepts quantities from 1 to 99. You have time for five checks. Which inputs earn a place? | State the risk behind each choice. Explain the coverage you give up when choosing between missing input, invalid types and boundary values. |
| Tell us about a defect you found by exploring a feature after its scripted checks passed. | Describe your starting hypothesis, the observation that changed your direction and the regression check you kept. |

### Automation and distributed systems

| Interview question | What to cover in your answer |
|---|---|
| A browser test fails once in twenty CI runs and always passes locally. What would you collect before changing it? | Compare first-attempt traces, worker data, build versions and the state the assertion actually observed. |
| Two parallel tests delete each other’s fixtures. How would you prove your repair works? | Trace ownership of the record IDs. Force overlapping execution and verify cleanup after both success and partial setup. |
| An order request times out after the server commits it. How would you test a retry? | Define the business invariant. Check the returned identity, persisted rows and downstream events for the same request key. |
| A provider makes an event field optional. Its own suite passes. What evidence would you ask consumers for? | Identify the affected versions, replay an event with the field absent and distinguish a passing consumer from one that never ran. |
| An automated accessibility scan passes, but a keyboard user cannot finish checkout. How would you narrow the failure? | Trace focus, activation and error recovery through the actual journey. Explain which observations require a person. |

### Release judgment and collaboration

| Interview question | What to cover in your answer |
|---|---|
| Tell us about a time you changed your release recommendation after another engineer challenged it. | Explain your original reasoning, the new evidence and how you communicated the revised decision. |
| A migration preserves the total row count. What could still be wrong, and how would you find it? | Consider duplicates, missing keys, incorrect ownership and per-key reconciliation. Give a counterexample with equal counts. |
| All executed tests pass, but the required recovery test is blocked. What would you tell the release owner? | Separate observed results from missing evidence. Name the exposure, available options and owner of the decision. |

### AI quality and agent evaluation

| Interview question | What to cover in your answer |
|---|---|
| An assistant generates thirty tests that all pass. How would you decide which deserve to enter the suite? | Select a known defect, check oracle independence and see which tests distinguish the broken behavior from the intended result. |
| A support assistant gives the right answer but cites an unrelated document. Which parts of the system would you evaluate separately? | Inspect retrieved passages, answer accuracy and citation support on labeled examples. |
| An agent reports that it stopped a pipeline, yet another job ran. How would you test the discrepancy? | Compare the stated decision with the scheduler receipt and event sequence. Replay duplicate or delayed events with a fake scheduler. |
| An evaluation improves overall while a small subgroup regresses. What would you ask before approving the change? | Inspect subgroup counts, case-level changes, rubric consistency and variability across runs. Identify when human review is warranted. |

## ✎ Rehearsal

Give a two-minute answer about a missed milestone. Ask a listener to write down the constraint, the decision you owned and the outcome. If they can identify only the team’s activities, revise one sentence to specify your action. If the outcome was never measured, name the observable evidence you do have.

---

[Return to the training guide](../README.md)
