# Agentic AI testing

Design the decision rule for an agent that can choose its next test action. Start with the allowed actions and the observations each one requires.

**Learning objective** · Define what evidence permits a test agent to continue, stop or request review.

## Worked example: Design a bounded performance experiment

A test agent can launch a workload, read its results and choose the next load level. The decision rule is part of the test design. Define the permitted environment, maximum load, required metrics and stop conditions before the first run. Keep the run ID, observations, decision and reason together so another engineer can reconstruct the sequence.

| Observed state | Example decision | Evidence to retain |
|---|---|---|
| Required metrics present and within limits | Advance to the next approved load level | Current load, p95 and error rate |
| Latency exceeds the agreed limit | Stop escalation and summarize the breach | Threshold, observed value and run ID |
| Required measurement missing | Mark the run inconclusive and request review | Missing field and collection status |

## Guided practice

An agent records 0% errors at 100 users but receives no latency samples. Should it advance to 200? Under the rule above, the missing measurement prevents a pass. Write a decision record that identifies the missing samples and the collection check needed before another run. Evaluate the agent on whether it chose the correct action and whether the record explains why.

## Independent assignment

Build three synthetic run records: one within limits, one above the latency limit and one missing latency. Predict the decision for each before evaluating the agent. Check both the action and the reason recorded. Treat escalation on incomplete evidence as a failed case.

[Continue to the performance testing lab](../../../../practice-qa-projects/performance-testing-jmeter/10-agentic-qa-for-performance.md)

[Related mastery module](../../../../QA%20Automation%20Mastery%20System/modules/15-autonomous-qa-pipelines.md) · [Fundamentals training guide](../README.md)
