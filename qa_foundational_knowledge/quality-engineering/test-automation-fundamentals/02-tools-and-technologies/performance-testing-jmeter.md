# Apache JMeter

**Hands-on project** · [Apache JMeter lab](../../../../practice-qa-projects/performance-testing-jmeter/README.md)

**QA Automation Mastery** · [11 · Non-Functional Testing](../../../../QA%20Automation%20Mastery%20System/modules/11-non-functional-testing.md)

At what workload does a customer journey breach its target?

## From a user journey to a workload model

A JMeter test plan describes who sends requests, in what sequence and at what pace. A thread group models concurrent users; samplers perform operations; timers shape pacing; assertions check response correctness. Correlation carries a server-generated value from one response into a later request. A script can report short response times while repeatedly loading an error page, so establish that the journey succeeds before interpreting timing data.

Read latency together with throughput, errors and sample count. The 95th percentile describes the response-time boundary at or below which roughly 95% of the measured samples fall. It says little about a rarely used slow endpoint if every endpoint is pooled. Report the transaction, load level and measurement window, and separate warm-up from the steady workload when comparing runs.

## What to test

Model one journey and correlate its dynamic identifiers across requests. Validate the business response with one user before increasing concurrency. Compare endpoint percentiles only across equivalent measurement windows.

## Practice assignment

Follow the setup in the linked lab. Record the workload, sample count, error rate and p95 for two runs. If p95 rises, name a server-side measurement that would help distinguish queueing from slower execution.

[Tools and technologies study guide](README.md) · [All practice QA projects](../../../../practice-qa-projects/)
