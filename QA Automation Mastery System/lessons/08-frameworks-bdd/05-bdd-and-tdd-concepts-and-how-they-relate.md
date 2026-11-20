# BDD and TDD: concepts and how they relate

[08 · Module topics](../../modules/08-frameworks-bdd.md) · [Curriculum index](../../README.md)

**Category** · FOUNDATION

TDD (red-green-refactor: write a failing test, make it pass, clean up) drives DESIGN at the unit level — it's a developer discipline. BDD extends the same 'test-first' instinct outward to BEHAVIOR, in language stakeholders can review, and is often TDD's outside-in counterpart: outer BDD scenarios describe the feature; inner TDD cycles build the units that satisfy it. They compose; neither replaces the other.

## Worked reference

````text
TDD & BDD — how they relate, not compete

  TDD — RED → GREEN → REFACTOR (developer-level)
  1. write a FAILING unit test for behavior that
     doesn't exist yet
  2. write the MINIMUM code to pass it
  3. REFACTOR with the safety net of a green test
  repeat — design emerges from the tests, not
  before them

  BDD — OUTSIDE-IN, STAKEHOLDER-READABLE
  start from a Gherkin SCENARIO describing the
  feature's observable behavior (Given/When/Then) —
  written WITH product/business, before code exists

  HOW THEY NEST (outside-in development)
  BDD scenario (feature-level, RED — no code yet)
    └─ unit A: TDD red→green→refactor
    └─ unit B: TDD red→green→refactor
    └─ ...until the outer BDD scenario goes GREEN
  the BDD scenario is the ACCEPTANCE criterion;
  TDD cycles are how you EARN it, unit by unit

  THE COMMON THREAD
  both say: write the test (expectation) BEFORE the
  code that satisfies it — at a different altitude
  each. Neither is 'the modern one'; they answer
  different questions (does this UNIT work right? /
  does this FEATURE behave right?)

  MISCONCEPTION TO KILL
  'BDD is just Cucumber' — Cucumber is ONE tool for
  BDD's discipline; you can practice BDD's outside-
  in thinking with plain code and no Gherkin at all
````

## Exercise

Pick one small feature. Write its BDD scenario first (outside-in, stakeholder-readable). Then TDD the implementation unit-by-unit until the scenario passes.
