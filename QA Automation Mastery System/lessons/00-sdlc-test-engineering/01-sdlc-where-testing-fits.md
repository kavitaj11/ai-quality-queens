# SDLC & where testing fits

[00 · Module topics](../../modules/00-sdlc-test-engineering.md) · [Curriculum index](../../README.md)

**Category** · TEST ENGINEERING & PROCESS

The Software Development Life Cycle is the frame every QA activity hangs on. Know the phases (requirements to design to build to test to release to maintain) and the classic models: Waterfall (sequential, test-at-end) vs Agile/iterative (test continuously). Full-stack QA understands the whole pipeline: frontend, API, data, and infra all have failure modes you own.

## Worked reference

````text
THE SDLC & WHERE QA LIVES

  REQUIREMENTS -> DESIGN -> BUILD -> TEST -> RELEASE -> MAINTAIN
       ^          ^         ^        ^         ^           ^
  review for   testable   unit +   system   release     prod
  testability  design,    API      +E2E,    gate,       monitoring,
  & clarity    test plan  tests    regress  deploy      bug triage

MODELS
  WATERFALL   sequential; testing is a phase near the END.
              late feedback -> expensive bugs. Rare for new work.
  V-MODEL     each build phase has a matching test level
              (unit-code, integration-design, system-requirements).
  AGILE       short iterations; testing happens EVERY sprint,
              automated, continuous. The modern default.
  DEVOPS/CICD test runs on every commit; quality is everyone's job.

FULL-STACK QA OWNS FAILURE MODES ACROSS:
  UI (browser, responsive)   ·  API (contracts, auth, errors)
  DATA (DB state, migrations) ·  INFRA (deploys, config, scaling)

WHY IT MATTERS
  the EARLIER a defect is found, the cheaper it is to fix.
  a requirement bug caught at review costs ~1x; the same bug in
  production can cost 100x. this drives shift-left (next).
````

## Exercise

Map a feature you know through the SDLC phases and mark where a QA activity happens in each, from requirement review to production monitoring.
