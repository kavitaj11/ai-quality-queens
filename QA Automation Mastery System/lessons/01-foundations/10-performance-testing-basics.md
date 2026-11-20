# Performance testing basics

[01 · Module topics](../../modules/01-foundations.md) · [Curriculum index](../../README.md)

**Category** · WEB & HTTP ESSENTIALS

Load (expected traffic), stress (find the ceiling), spike, and soak. The metrics that matter: p95/p99 latency — never averages — throughput, and error rate under load. Locust, k6, and Gatling make perf tests code you can gate in CI; Playwright adds frontend vitals.

## Worked reference

````text
PERFORMANCE TESTING BASICS — theory

THE TEST TYPES (by load shape)
  Load    behavior under EXPECTED traffic — the baseline
  Stress  push PAST the ceiling to find the break point
  Spike   sudden burst of users
  Soak    sustained load for hours — surfaces memory leaks

METRICS THAT MATTER
  ✓ p95 / p99 latency   — the slow tail users actually feel
  ✗ AVERAGE latency     — hides the tail; never gate on it alone
  throughput            requests/sec the system sustains
  error rate            % failed under load (watch it climb)
  saturation            CPU/mem/connections at the limit

HOW TO RUN IT
  script the busiest endpoints, define a load SHAPE (ramp →
  steady → down), set THRESHOLDS, and gate CI on them:
     e.g. p95 < 500ms  AND  error rate < 1%
  Backend load: Locust (Py) · k6 (JS) · JMeter / Gatling (Java)
  Frontend perf: Core Web Vitals (LCP/INP/CLS) in the E2E suite

DISCIPLINE
  test a realistic environment (prod-like data + infra), warm up
  first, run twice and compare, and TREND results over releases —
  regressions matter more than absolute numbers.

  → The Locust / k6 / Gatling / Web-Vitals code lives in the
    Non-Functional Testing phase.
````

## Exercise

Script your busiest endpoint: 50 VUs for 2 minutes, gate on p95 < 500ms and error rate < 1%. Run it twice and compare.
