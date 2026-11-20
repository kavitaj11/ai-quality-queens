# Distributed systems fundamentals

[12 · Module topics](../../modules/12-microservices-testing.md) · [Curriculum index](../../README.md)

**Category** · DISTRIBUTED SYSTEMS FUNDAMENTALS

Microservices are distributed systems, and distributed systems have inescapable properties QA must design around: the network is unreliable, there is no global clock, failures are partial, and consistency is a trade-off (CAP: pick 2 of consistency, availability, partition-tolerance). Most services choose availability + eventual consistency, which is exactly why UI-then-DB assertions race.

## Worked reference

````text
DISTRIBUTED SYSTEMS FUNDAMENTALS

THE 8 FALLACIES (assumptions that bite you)
  the network is reliable · latency is zero · bandwidth is
  infinite · the network is secure · topology never changes ·
  one admin · transport cost is zero · the network is homogeneous
  -> every one is FALSE; tests must handle each.

CAP THEOREM (pick 2 under a network partition)
  Consistency · Availability · Partition-tolerance
  most microservices = AP -> EVENTUAL consistency
  (a write isn't instantly visible everywhere)

KEY REALITIES FOR QA
  • PARTIAL failure: one service down, others up
  • no GLOBAL clock: ordering + timestamps are hard
  • eventual consistency: read-after-write may be stale
    -> POLL/retry in tests, don't assert immediately
  • idempotency + retries: the same message may arrive twice
  • observability is mandatory: logs, metrics, distributed traces
````

## Exercise

For one feature spanning 2 services, list the ways it can partially fail (one service down, slow, or returning stale data) and what the user should see in each.
