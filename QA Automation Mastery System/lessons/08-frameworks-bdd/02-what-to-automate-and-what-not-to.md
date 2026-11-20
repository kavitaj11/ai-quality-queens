# What to automate — and what not to

[08 · Module topics](../../modules/08-frameworks-bdd.md) · [Curriculum index](../../README.md)

**Category** · FOUNDATION

Automation has a cost per test (build + maintain + flake-debug); not everything clears that bar. Automate: stable, repeated, high-risk, data-heavy, regression-prone flows. Don't automate: one-off checks, still-changing UI, pure exploratory/usability judgment, and anything whose automation cost exceeds its lifetime value. The test pyramid is this decision applied at the architecture level.

## Worked reference

````text
WHAT TO AUTOMATE — the decision framework

  AUTOMATE WHEN                NOT WHEN
  runs repeatedly (regression)  one-off / exploratory
  stable UI/API                 UI still churning weekly
  high business risk            low risk, rarely touched
  data-combination heavy        needs human judgment
                                (usability, 'does this
                                 feel right?')
  fast feedback matters         automation cost > the
  (PR gate candidate)           value of catching it

  THE ROI GATE FOR EACH CANDIDATE
  automate if:
    (manual runs × manual cost) over test lifetime
    > (build cost + maintenance cost)
  a test run twice ever, by hand, in 2 minutes,
  isn't worth 3 hours of automation + upkeep

  THE PYRAMID IS THIS DECISION, SYSTEMATIZED
  unit     cheap, fast, deterministic → automate ALL
  integration  moderate cost → automate the risky joins
  E2E      expensive, flaky-prone → automate only the
           critical user journeys, keep the count LOW

  WHAT STAYS MANUAL/EXPLORATORY ON PURPOSE
  new features (nothing stable to assert against yet)
  · visual/UX judgment · rare edge combinations ·
  anything cheaper to eyeball than to script

  THE ANTI-PATTERN
  '100% automation' as a vanity metric — it just
  means a bloated, slow, flaky suite full of tests
  nobody needed automated
````

## Exercise

Take your team's manual test case list. Sort each into automate-now / automate-later / never — and justify the never column out loud.
