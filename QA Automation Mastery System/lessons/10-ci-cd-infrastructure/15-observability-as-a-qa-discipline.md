# Observability as a QA discipline

[10 · Module topics](../../modules/10-ci-cd-infrastructure.md) · [Curriculum index](../../README.md)

**Category** · DEVOPS FOR QA

Monitoring answers 'is it broken?'; observability answers 'why?'. The three pillars — metrics, logs, traces (OpenTelemetry) — are also QA tools: synthetic monitors are tests running in production forever, SLOs and error budgets are quality bars expressed numerically, and trace ids injected into test requests link every failure to its distributed cause. The suite proves it worked before deploy; observability proves it keeps working after.

## Worked reference

````text
OBSERVABILITY FOR QA — testing after the deploy

  THE THREE PILLARS (OpenTelemetry = the standard)
  METRICS   numbers over time: error rate, p50/p99
            latency, saturation — cheap, alertable
  LOGS      structured events — the detail, searchable
            when correlated by trace id
  TRACES    one request's path across services —
            the distributed stack trace

  SYNTHETIC MONITORING = TESTS THAT NEVER STOP
  your smoke journeys, run every 5 min against prod
  from real regions, with test-tagged data
  • catches what unit tests can't: cert expiry, DNS,
    quota exhaustion, third-party outages
  • the SAME code as your suite — one journey
    definition, two runtimes (CI + monitor)

  SLOs — QUALITY BARS AS NUMBERS
  SLI  measured: % requests < 800ms and 2xx
  SLO  target:   99.9% over 30 days
  ERROR BUDGET   the 0.1% you may burn —
  budget healthy → ship features
  budget burned  → ship reliability (and your
  release gate TIGHTENS: freeze risky deploys)

  QA WIRING THAT PAYS OFF
  • inject trace ids into every TEST request →
    a failing test links straight to its trace
  • deploy markers on dashboards → 'error rate
    rose at 14:02' meets 'v1.5 deployed 14:01'
  • alerts reviewed like flaky tests: every page
    is actionable or it gets deleted (alert
    fatigue = ignored red suite, same disease)

  THE SHIFT
  pre-deploy suite: 'it worked in staging'
  observability:    'it works, for real users,
                     right now' — QA owns both
````

## Exercise

Define an SLO for one critical journey (e.g. checkout p99 < 800ms, 99.9% success). What synthetic monitor validates it, and what does its alert page say?
