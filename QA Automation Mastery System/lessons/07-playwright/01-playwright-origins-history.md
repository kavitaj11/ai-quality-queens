# Playwright: origins & history

[07 · Module topics](../../modules/07-playwright.md) · [Curriculum index](../../README.md)

**Category** · PLAYWRIGHT CORE

Playwright was created at Microsoft in 2020 by the same core team that built Puppeteer at Google — they set out to fix the things that made end-to-end testing flaky and slow. Knowing its lineage explains its design: it drives browsers over a fast bidirectional protocol (like Puppeteer/CDP), but adds auto-waiting, cross-browser engines, and a real test runner.

## Worked reference

````text
PLAYWRIGHT — A TIMELINE

2017  Puppeteer (Google)
      • Node library driving Chromium over the DevTools Protocol
      • fast + event-driven, but Chromium-only, no test runner

2020  Playwright 1.0 (Microsoft)
      • built by ex-Puppeteer engineers who left Google
      • goal: kill flakiness — AUTO-WAITING built into every action
      • ONE API drives Chromium, Firefox, AND WebKit (Safari engine)
      • Microsoft ships patched browser builds so behavior is stable

2021  @playwright/test — the official test runner
      • fixtures, parallelism, retries, HTML report — batteries included
      • Trace Viewer: time-travel debugging of a failed run

2021+ Language bindings
      • Python, Java, and .NET join JS/TS
      • same engine + protocol under every language

2022+ Codegen, component testing, UI Mode, sharding
      • record-and-generate tests; watch-mode UI runner
      • network interception & mocking as a first-class feature

2023+ Rapid adoption
      • fastest-growing E2E tool; strong AI-agent + MCP support


DESIGN DNA (why it feels different from Selenium)
  • bundled, version-matched browsers  → no driver-binary hell
  • persistent bidirectional connection → fast + can push events
  • auto-wait on every action           → far less flakiness
  • context isolation per test          → clean state, fast parallel
  • batteries included                  → runner, trace, mocking, report
````

## Exercise

Explain to a teammate why Playwright's team chose to bundle browser builds and drive them over a persistent connection instead of the W3C WebDriver HTTP model.
