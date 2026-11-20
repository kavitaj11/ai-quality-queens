# Agile automation & shift-left

[00 · Module topics](../../modules/00-sdlc-test-engineering.md) · [Curriculum index](../../README.md)

**Category** · TEST ENGINEERING & PROCESS

In Agile, testing is continuous and automated: QA is embedded in the sprint, not a downstream gate. Shift-LEFT means moving testing earlier (test at requirement/design/commit time) to catch defects when they are cheap; shift-RIGHT adds testing in production (monitoring, canary, feature flags). The automation pyramid + CI make this possible.

## Worked reference

````text
AGILE AUTOMATION & SHIFT-LEFT / SHIFT-RIGHT

QA IN THE SPRINT (embedded, not a phase)
  grooming    review stories for testability + acceptance criteria
  planning    size test work with dev work
  in-sprint   write automated tests AS features are built (ATDD/BDD)
  every PR    unit + integration + smoke run in CI (fast feedback)
  done        Definition of Done INCLUDES automated tests passing

SHIFT-LEFT  (test earlier = cheaper defects)
  <-- requirements review · static analysis · unit tests at commit
      · API tests before UI · TDD/BDD · dev-owned quality
  the whole point: catch bugs at ~1x cost, not 100x in prod.

SHIFT-RIGHT (test in production, safely)
  monitoring & alerting · synthetic checks · canary / blue-green
  releases · feature flags · A/B · chaos testing -->
  real-world signals emulation cannot produce.

ENABLERS
  the test pyramid (fast checks low) + CI/CD (run on every commit)
  + trunk-based dev + flags = continuous, low-risk delivery.

MINDSET
  quality is everyone's job -- QA coaches the team and owns the
  automation system, rather than being the last gate before release.
````

## Exercise

For one user story, list the definition-of-done quality gates from grooming to production. Where does each test type run?
