# Reliability, resilience & recovery

[11 · Module topics](../../modules/11-non-functional-testing.md) · [Curriculum index](../../README.md)

**Category** · OTHER NON-FUNCTIONAL TYPES

These test how the system behaves over time and under failure. Reliability/stability (does it stay up under sustained use — soak tests, MTBF), resilience/chaos (does it survive a dependency dying — kill a pod, add latency), recovery/failover (does it come back cleanly — backups, DB failover, retries), and availability (uptime SLOs). Increasingly owned by SRE + QA together.

## Worked reference

````text
RELIABILITY · RESILIENCE · RECOVERY

RELIABILITY / STABILITY
  stays healthy over TIME + sustained load
  soak tests (hours) · memory-leak detection · MTBF
  (mean time between failures)

RESILIENCE / CHAOS ENGINEERING
  survives dependency failures. Inject faults on purpose:
  kill a pod/instance · add network latency · drop a DB ·
  exhaust CPU/disk · simulate a region outage
  tools: Chaos Monkey · Gremlin · Litmus · toxiproxy
  assert: graceful degradation, no data loss, alerts fire

RECOVERY / FAILOVER / DR
  comes back cleanly after failure: DB failover, backup restore,
  retry + circuit-breaker behavior, RTO/RPO targets met

AVAILABILITY
  uptime SLO (e.g. 99.9%) · health checks · redundancy
  measured in production (shift-right) via monitoring
````

## Exercise

For one service, design a chaos experiment (kill its DB connection mid-request) and define the expected graceful behavior + recovery time to assert.
