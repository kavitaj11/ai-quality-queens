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
## ✎ Rehearsal

Give a two-minute answer about a missed milestone. Ask a listener to write down the constraint, the decision you owned and the outcome. If they can identify only the team’s activities, revise one sentence to specify your action. If the outcome was never measured, name the observable evidence you do have.

---

[Return to the training guide](../README.md)
