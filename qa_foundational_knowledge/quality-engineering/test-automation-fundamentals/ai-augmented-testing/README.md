# AI-augmented testing

Use an AI assistant to draft a report from measured results. The tester owns the interpretation and release recommendation.

**Learning objective** · Review an AI-generated test report against the measurements that support it.

## Worked example: Analyze a performance summary

In the linked lab, checkout p95 reaches 836 ms at 200 users against an 800 ms target. An assistant can draft a summary, but the reviewer must check the metric, endpoint, load level and units. Checkout exceeds its target by 36 ms. That observation establishes a breach. It does not establish its cause.

Give the assistant aggregate metrics, the threshold and the comparison run. Ask for three short sections: observed changes, possible explanations and the next measurement. Check each number against the input. If the draft attributes the breach to CPU saturation, look for CPU measurements before retaining that claim.

## Guided practice

Review this draft sentence: “The checkout database ran out of CPU at 200 users.” Rewrite it using only the evidence above. A defensible version is “At 200 users, checkout p95 reached 836 ms, 36 ms above target. Collect CPU utilization and connection-pool wait time over the same interval to investigate the slowdown.”

## Audit the assistant's draft

Use this small evidence packet before reading the review below. The workload is 200 users, checkout p95 is 836 ms, and the agreed upper limit is 800 ms. The packet contains one run and no database measurements.

| Draft claim | Review decision | Evidence or next step |
|---|---|---|
| Checkout p95 exceeded its limit by 4.5%. | Keep | Divide the 36 ms excess by the 800 ms limit. |
| Performance regressed by 4.5% since the last release. | Remove | A threshold is available. A comparable prior run is missing. |
| Connection-pool contention could explain the delay. | Qualify | Label this a hypothesis. Collect pool wait times for the same interval. |

## Repeat the review on a second input

Change the measured p95 to 780 ms while keeping the limit at 800 ms. Ask the assistant to regenerate its summary. Its conclusion should now say that the observed p95 was 20 ms below the limit. If it repeats the earlier breach, save both inputs and outputs as a regression case for the reporting workflow. Record the prompt and model version alongside the case so another reviewer can investigate a changed result.

## Independent assignment

Create a three-column review sheet with the draft claim, its supporting measurement and your decision to accept, revise or remove it. Include one supported claim, one proposed cause and one question that needs another measurement.

[Continue to the performance testing lab](../../../../practice-qa-projects/performance-testing-jmeter/09-ai-augmented-performance-testing.md)

[Related mastery module](../../../../QA%20Automation%20Mastery%20System/modules/14-ai-augmented-qa.md) · [Fundamentals training guide](../README.md)
