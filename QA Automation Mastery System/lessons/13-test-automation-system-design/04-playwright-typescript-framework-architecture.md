# Playwright–TypeScript framework architecture

[13 · Module topics](../../modules/13-test-automation-system-design.md) · [Curriculum index](../../README.md)

**Category** · FRAMEWORK BLUEPRINTS

Playwright's native home: @playwright/test is the runner, fixtures, assertions, and parallelism in one package. Architecture via playwright.config.ts (projects = browser × env matrix), custom fixtures extending `test` (typed DI — the conftest equivalent), storage-state auth in globalSetup, and web-first assertions that auto-retry. TypeScript's types make page objects and test data self-documenting; trace viewer + UI mode are the debugging story.

## Worked reference

````text
PLAYWRIGHT–TYPESCRIPT — the all-in-one blueprint

  repo/
  ├─ playwright.config.ts   THE architecture file:
  │    projects: [setup, chromium, firefox, mobile]
  │    use: {baseURL, trace: 'on-first-retry'}
  │    webServer: boot the app for local runs
  ├─ fixtures.ts            test.extend<{...}>() —
  │                         typed DI, replaces conftest
  ├─ pages/                 page objects (thin; locators
  │                         are getByRole/getByLabel)
  ├─ api/                   APIRequestContext helpers —
  │                         seed via API, assert via UI
  ├─ .auth/                 storage-state per role
  └─ tests/                 specs read like scenarios

  PROJECTS = the matrix, declared not scripted
  setup project (logs in, saves state)
    ◀─ dependencies ─ chromium · firefox · mobile
  PR run:      --project=chromium (fast gate)
  nightly:     all projects (full matrix)

  FIXTURES: typed composition
  base test → extend {adminPage} → extend {cartSeeded}
  each fixture: setup, yield, teardown — and the
  TYPES flow into every test signature

  WHAT TS BUYS OVER PY/JAVA HERE
  first-party runner (no pytest/TestNG glue) ·
  compile-time locator/page contract checks ·
  UI mode + trace viewer built in · one npm install

  WATCH FOR
  no test.step discipline → unreadable traces ·
  hard-coded waits creeping in despite auto-wait ·
  fixture chains too deep to reason about (>3 levels)
````

## Exercise

Design the playwright.config.ts projects for: chromium+firefox on PR (staging), full matrix nightly, and an 'auth-setup' project the others depend on.
