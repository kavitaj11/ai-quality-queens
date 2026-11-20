# The STLC: test lifecycle

[00 · Module topics](../../modules/00-sdlc-test-engineering.md) · [Curriculum index](../../README.md)

**Category** · TEST ENGINEERING & PROCESS

The Software Testing Life Cycle is the sequence a testing effort follows in parallel with development: requirement analysis, test planning, test design (cases + data), environment setup, execution, closure. Each phase has entry/exit criteria and deliverables. Automation slots into design (write scripts) and execution (run in CI).

## Worked reference

````text
SOFTWARE TESTING LIFE CYCLE (STLC)

  1. REQUIREMENT ANALYSIS
     what is testable? clarify with BA/dev · build the RTM
     (Requirement Traceability Matrix) -- req to test coverage
  2. TEST PLANNING
     scope, effort, tools, roles -> the test plan
  3. TEST DESIGN
     write test CASES + test DATA; design automation scripts;
     apply techniques (equivalence, boundary, decision table)
  4. ENVIRONMENT SETUP
     test env, data, access ready; smoke it before execution
  5. TEST EXECUTION
     run manual + automated; log defects; re-test fixes;
     update the RTM with pass/fail
  6. TEST CLOSURE
     exit criteria met? metrics, lessons learned, sign-off

  Each phase: ENTRY criteria -> activities -> EXIT criteria +
  DELIVERABLE. Runs alongside dev, not after it.

WHERE AUTOMATION FITS
  design -> script the cases  ·  execution -> run them in CI
  closure -> coverage + trend reports feed the next cycle.
````

## Exercise

For one feature, produce the STLC deliverable of each phase: RTM, test plan, test cases, env checklist, execution report, closure summary.
