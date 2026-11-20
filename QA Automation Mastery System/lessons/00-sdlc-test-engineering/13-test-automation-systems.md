# Test automation systems

[00 · Module topics](../../modules/00-sdlc-test-engineering.md) · [Curriculum index](../../README.md)

**Category** · QE PROCESS QUALITY

At scale, automation stops being a folder of scripts and becomes a SYSTEM with users (every engineer), an architecture (runner, drivers, data, environments, reporting), and its own SDLC. Treat it as an internal product: layered design, versioned releases, docs and onboarding, SLOs for suite runtime and flake rate, and a roadmap — because a suite nobody trusts or can extend is technical debt with a green checkmark.

## Worked reference

````text
TEST AUTOMATION AS A SYSTEM — an internal product

  ┌─ TESTS ──────────────────────────────────┐
  │ specs · scenarios · eval datasets           │
  ├─ FRAMEWORK LAYER ────────────────────────┤
  │ page objects / API clients · fixtures ·     │
  │ data builders · custom assertions · waits   │
  ├─ PLATFORM LAYER ─────────────────────────┤
  │ runner + parallelism · env/config mgmt ·    │
  │ test data service · secrets · browser grid  │
  ├─ DELIVERY LAYER ─────────────────────────┤
  │ CI integration · tiering · reporting ·      │
  │ dashboards · flake tracking · alerts        │
  └────────────────────────────────────────────┐
  users: EVERY engineer · owner: QE platform team

RUN IT LIKE A PRODUCT
  versioned releases + changelog · docs + onboarding
  ('first test in a day') · deprecation policy ·
  roadmap driven by user pain, not tool fashion

SLOs FOR THE SYSTEM ITSELF
  PR suite runtime < 10 min · flake rate < 1% ·
  time-to-first-test for a new hire < 1 day ·
  triage time per red build < 15 min

SMELLS
  only one person can add tests · copy-paste setup in
  every spec · green builds nobody believes · a
  'framework rewrite' every 18 months
````

## Exercise

Diagram your (or a hypothetical) automation system's layers, then write 3 SLOs for it — e.g. PR suite < 10 min, flake rate < 1%, new-engineer first test < 1 day.
