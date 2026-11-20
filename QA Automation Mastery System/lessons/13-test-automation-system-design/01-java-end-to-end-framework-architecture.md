# Java end-to-end framework architecture

[13 · Module topics](../../modules/13-test-automation-system-design.md) · [Curriculum index](../../README.md)

**Category** · FRAMEWORK BLUEPRINTS

The enterprise-standard stack, designed in layers: Maven/Gradle for build + profiles, TestNG for suites/parallelism/groups, Selenium behind a page-object layer, a config layer (env-aware properties), data providers, a driver factory (thread-local for parallel), utilities (waits, API clients for setup), and Allure/Extent reporting. The design goal: test classes read like scenarios; everything mechanical lives a layer down.

## Worked reference

````text
JAVA E2E FRAMEWORK — the layered blueprint

  ┌─ TEST LAYER ───────────────────────────┐
  │ TestNG classes — read like scenarios;   │
  │ @Test(groups={smoke,regression})        │
  ├─ BUSINESS LAYER ───────────────────────┤
  │ page objects + flows (LoginFlow.as())   │
  ├─ CORE ────────────────────────────────┤
  │ DriverFactory (ThreadLocal—parallel!)   │
  │ ConfigManager (env props) · waits ·      │
  │ API client (data setup via REST, not UI)│
  ├─ DATA ────────────────────────────────┤
  │ @DataProvider · builders · fixtures/json │
  ├─ REPORTING ───────────────────────────┤
  │ Allure/Extent + listeners (screenshots  │
  │ on fail) · retry analyzer               │
  └─ BUILD: Maven profiles · CI entrypoint ──┘

  RULES THAT KEEP IT CLEAN
  • tests never touch WebDriver directly — only
    pages/flows do (one place to heal locators)
  • state setup via API, assert via UI — 10x faster
  • config resolves: CLI arg > env var > properties
  • ThreadLocal driver or parallel runs corrupt
    each other — the classic framework bug
````

## Exercise

Design the package structure for a Java E2E framework testing a 3-env (dev/stage/prod-mirror) web app. Where do env configs, test data, and the driver factory live — and what does a test class import?
