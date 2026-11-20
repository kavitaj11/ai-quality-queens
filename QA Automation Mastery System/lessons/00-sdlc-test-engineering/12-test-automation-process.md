# Test automation process

[00 · Module topics](../../modules/00-sdlc-test-engineering.md) · [Curriculum index](../../README.md)

**Category** · QE PROCESS QUALITY

Automation is an investment decision with a pipeline. Select candidates by ROI (frequency × stability × cost-of-manual vs cost-to-build+maintain), design with the right pattern (page objects, fixtures, data builders), implement to code standards, review like production code, wire into CI, and maintain deliberately — with quarantine rules and deletion as a first-class outcome.

## Worked reference

````text
TEST AUTOMATION PROCESS — select → build → run → maintain

1. SELECT — automate by ROI, not by order found
   ROI ≈ (runs/yr × manual minutes) / (build + yearly upkeep)
   GOOD: stable, repetitive, high-frequency, data-heavy,
         critical-path regression
   BAD:  one-offs · churning UI · UX judgment ·
         anything cheaper to check by hand forever

2. DESIGN
   right LEVEL first (unit > API > UI — the pyramid) ·
   page objects / clients · test data builders ·
   independent tests (any order, parallel-safe)

3. IMPLEMENT + REVIEW
   code standards · semantic locators · no sleeps ·
   PR review like production code — because it is

4. RUN
   CI on every commit · tiered (smoke/PR/nightly) ·
   results visible to the whole team, not a QA silo

5. MAINTAIN — the phase everyone underestimates
   quarantine flaky (owner + expiry) · fix root causes ·
   DELETE tests whose feature or value is gone ·
   refactor suites like any aging codebase

ANTI-PATTERNS
  automate 100% of manual cases 1:1 · UI tests for
  logic testable at the API · metrics that reward
  test COUNT · nobody owns maintenance
````

## Exercise

Score 10 manual regression cases on frequency, stability, and manual cost; pick the 4 you'd automate first and defend the two you'd never automate.
