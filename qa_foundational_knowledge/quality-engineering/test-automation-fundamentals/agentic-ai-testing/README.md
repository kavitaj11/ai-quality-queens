# Agentic AI testing

> **Learning objective** · Define what evidence permits a test agent to continue, stop or request review.

## ◈ Design a bounded performance experiment

A test agent can launch a workload, read its results and choose the next load level. The decision rule is part of the test design. Define the permitted environment, maximum load, required metrics and stop conditions before the first run. Keep the run ID, observations, decision and reason together so another engineer can reconstruct the sequence.

| Observed state | Example decision | Evidence to retain |
|---|---|---|
| Required metrics present and within limits | Advance to the next approved load level | Current load, p95 and error rate |
| Latency exceeds the agreed limit | Stop escalation and summarize the breach | Threshold, observed value and run ID |
| Required measurement missing | Mark the run inconclusive and request review | Missing field and collection status |

## ✎ Practice

An agent records 0% errors at 100 users but receives no latency samples. Should it advance to 200? Under the rule above, the missing measurement prevents a pass. Write a decision record that identifies the missing samples and the collection check needed before another run. Evaluate the agent on whether it chose the correct action and whether the record explains why.

[Continue to the performance testing lab](../../../../practice-qa-projects/performance-testing-jmeter/10-agentic-qa-for-performance.md)

---

[Return to the training guide](../README.md)
