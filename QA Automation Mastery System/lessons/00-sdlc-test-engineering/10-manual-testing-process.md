# Manual testing process

[00 · Module topics](../../modules/00-sdlc-test-engineering.md) · [Curriculum index](../../README.md)

**Category** · QE PROCESS QUALITY

Manual testing is a discipline, not the absence of automation. It has its own flow: analyze the requirement, design cases with techniques (equivalence, boundary, decision tables), prepare data, execute against a build, and log defects with severity/priority through a defect lifecycle. Where it wins: UX judgment, one-off verifications, accessibility feel, and anything a human notices that an assertion can't encode.

## Worked reference

````text
MANUAL TESTING — the structured process

  requirement → design cases → prep data → execute → report

A GOOD TEST CASE
  id · title · preconditions · STEPS (numbered, exact) ·
  expected result per step · actual · status · evidence
  designed with TECHNIQUES, not vibes:
  equivalence partitions · boundaries · decision tables ·
  state transitions (see Foundations)

THE DEFECT LIFECYCLE
  new → triaged → assigned → fixed → retest → closed
                       └→ rejected / duplicate / deferred

  SEVERITY  how bad is the impact?   (crash vs typo)
  PRIORITY  how soon must it be fixed? (business call)
  — a cosmetic bug on the pricing page can be
    low severity, high priority

A DEFECT REPORT THAT GETS FIXED
  title states the failure, not 'it broke' · exact steps ·
  expected vs actual · environment/build · evidence
  (screenshot, HAR, logs) · severity + your reasoning

WHERE MANUAL WINS (and should stay manual)
  UX judgment · exploratory work · accessibility feel ·
  one-time migration checks · anything run once
WHERE IT LOSES
  regression — repetitive, error-prone, slow → automate
````

## Exercise

Take one feature and run the full manual flow: 6 designed cases, an execution record, and one defect report written well enough that a dev reproduces it on the first try.
