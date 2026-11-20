# AI-augmented testing

Use an AI assistant to draft a report from measured results. The tester owns the interpretation and release recommendation.

**Learning objective** · Review an AI-generated test report against the measurements that support it.

## Worked example: Analyze a performance summary

In the linked lab, checkout p95 reaches 836 ms at 200 users against an 800 ms target. An assistant can draft a summary, but the reviewer must check the metric, endpoint, load level and units. Checkout exceeds its target by 36 ms. That observation establishes a breach. It does not establish its cause.

Give the assistant aggregate metrics, the threshold and the comparison run. Ask for three short sections: observed changes, possible explanations and the next measurement. Check each number against the input. If the draft attributes the breach to CPU saturation, look for CPU measurements before retaining that claim.

## Guided practice

Review this draft sentence: “The checkout database ran out of CPU at 200 users.” Rewrite it using only the evidence above. A defensible version is “At 200 users, checkout p95 reached 836 ms, 36 ms above target. Collect CPU utilization and connection-pool wait time over the same interval to investigate the slowdown.”

## Independent assignment

Create a three-column review sheet with the draft claim, its supporting measurement and your decision to accept, revise or remove it. Include one supported claim, one proposed cause and one question that needs another measurement.

[Continue to the performance testing lab](../../../../practice-qa-projects/performance-testing-jmeter/09-ai-augmented-performance-testing.md)

[Related mastery module](../../../../QA%20Automation%20Mastery%20System/modules/14-ai-augmented-qa.md) · [Fundamentals training guide](../README.md)
