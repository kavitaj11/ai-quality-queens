# Progressive delivery: canary, blue-green & feature flags

[10 · Module topics](../../modules/10-ci-cd-infrastructure.md) · [Curriculum index](../../README.md)

**Category** · DEVOPS FOR QA

Modern deploys are themselves a testing strategy. Blue-green keeps two prod environments and flips traffic (instant rollback); canary sends 1→5→50% of real traffic to the new version while comparing error rates; feature flags decouple deploy from release and let you test in production safely. QA's role shifts: define the canary's success metrics, keep the flag matrix tested, and own the smoke checks that gate each stage.

## Worked reference

````text
PROGRESSIVE DELIVERY — the deploy IS a test

  BLUE-GREEN
  [blue v1.4 ← 100% traffic]   [green v1.5 idle]
  deploy to green → smoke suite AGAINST GREEN →
  flip router → blue idles as instant rollback
  QA owns: the smoke suite that authorizes the flip

  CANARY
  v1.5 gets 1% → 5% → 25% → 100% of REAL traffic
  at each step compare canary vs baseline:
    error rate · p99 latency · business metric
    (checkout conversion!)
  regression detected → AUTO-ROLLBACK
  QA owns: which metrics gate, thresholds, the
  probe tests that exercise critical paths at 1%

  FEATURE FLAGS — deploy ≠ release
  code ships DARK; flag turns it on per cohort
  (internal → beta → 10% → all)
  QA owns:
  • the FLAG MATRIX: test on AND off states of
    flags that interact (2 flags = 4 states; 10
    flags = pick pairs by risk, not 1024 combos)
  • flag hygiene: expired flags are dead code +
    untested paths — expiry dates, like quarantine
  • kill-switch drills: does OFF actually roll
    back cleanly, or does state corrupt?

  TESTING IN PRODUCTION (safely)
  synthetic journeys against prod (test creds,
  test data tagged) · shadow traffic replay ·
  chaos drills on staging first
  prod observability = the assertion layer:
  deploy verified by dashboards, not hope

  ROLLBACK IS A FEATURE — TEST IT
  the rollback path is code too: practice it,
  time it (MTTR), verify data migrations survive
  a down-rev (backward-compatible schemas)
````

## Exercise

Design the canary policy for a checkout service: which metrics gate promotion at each traffic step, what auto-rolls-back, and which smoke tests run at 1%?
