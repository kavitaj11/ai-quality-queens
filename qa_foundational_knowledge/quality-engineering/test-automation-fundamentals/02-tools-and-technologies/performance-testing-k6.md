# Grafana k6

**Hands-on project** · [Grafana k6 lab](../../../../practice-qa-projects/performance-testing-k6/README.md)

**QA Automation Mastery** · [11 · Non-Functional Testing](../../../../QA%20Automation%20Mastery%20System/modules/11-non-functional-testing.md)

Did the system meet its latency and error targets at the intended arrival rate?

## Workload delivery and pass criteria

In k6, a scenario defines how iterations are scheduled. With a fixed number of virtual users, each user starts another iteration after completing the previous one. A slower system therefore reduces the rate of new work. An arrival-rate scenario schedules starts at a target rate. It needs enough available virtual users to sustain that schedule as response times rise. Choose the model that represents how users or upstream services generate demand.

A check records whether an observation met an expectation. A threshold applies a pass criterion to an aggregated metric over the run. A failed check alone does not make the run exit unsuccessfully; a threshold can turn a required check rate into a gate. Review delivered work alongside latency. Dropped iterations mean some scheduled starts did not occur, which limits the claim you can make about behavior at the target load.

## What to test

Choose an arrival-rate workload for independently arriving requests or a fixed-user workload for repeated user journeys. Check response correctness and use thresholds to express run-level acceptance criteria.

## Practice assignment

Follow the setup in the linked lab. Compare target and achieved traffic. Record dropped iterations alongside latency. Explain why low latency during an under-delivered workload leaves the capacity question open.

[Tools and technologies study guide](README.md) · [All practice QA projects](../../../../practice-qa-projects/)
