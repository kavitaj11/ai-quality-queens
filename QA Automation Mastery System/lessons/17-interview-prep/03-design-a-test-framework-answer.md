# “Design a test framework” answer

[17 · Module topics](../../modules/17-interview-prep.md) · [Curriculum index](../../README.md)

**Category** · DESIGN & BEHAVIORAL

A layered story: config → driver/client factory → page objects & API clients → test layer → data → reporting → CI. Practice drawing it in 5 minutes with trade-offs for each layer.

## Worked reference

````text
# The 5-minute framework diagram
# config/     env, browsers, secrets (never in code)
# core/       driver factory, api client, waits
# pages/      page objects (UI)   clients/ (API)
# tests/      intent-level, tagged smoke/regression
# data/       builders + factories, no shared state
# reports/    allure + CI artifacts
# ci/         PR smoke gate, nightly full matrix
````

## Exercise

Whiteboard your framework twice: once for a UI-heavy monolith, once for a microservices platform. Note what changes.
