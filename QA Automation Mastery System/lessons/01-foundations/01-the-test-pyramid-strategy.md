# The test pyramid & strategy

[01 · Module topics](../../modules/01-foundations.md) · [Curriculum index](../../README.md)

**Category** · TESTING FUNDAMENTALS

Unit, integration, and E2E layers — why cost and flakiness grow as you climb. A good strategy pushes checks as low as possible and reserves the UI layer for critical journeys. Strategy decision #1 is your language: each tab maps that stack's tools layer-by-layer, with its strategy, trade-offs, and AI compatibility.

## Worked reference

````text
THE TEST PYRAMID  —  push checks as LOW as possible

        /\        E2E / UI   ~10%   slow · flaky · costly
       /  \                  full journeys, real browser
      /----\     INTEGRATION ~20%   services + real DB/queue
     /      \                 module-to-module contracts
    /--------\   UNIT         ~70%   fast · stable · cheap
   /__________\               one function, deps mocked

  Cost & flakiness GROW as you climb; speed & count SHRINK.
  Reserve the UI layer for critical journeys (login, checkout,
  signup). Everything provable lower, prove lower.
  Anti-pattern: the "ice-cream cone" (mostly manual + E2E).


STRATEGY DECISION #1 — YOUR LANGUAGE

  Each stack maps its own tools layer-by-layer. Switch the tabs
  below (Python / JavaScript / TypeScript / Java) to compare
  tools, strategy, trade-offs, and AI fit for each.


┌─ PYTHON ───────────────────────────────────────────────────┐
  Unit  pytest+pytest-mock · Integration requests, schemathesis
  E2E   Playwright/Selenium · BDD pytest-bdd, behave
  Perf  Locust · Data Faker, pydantic · Reports allure-pytest
  Strategy  API-first: requests tests ~10x faster than UI;
            reserve Playwright for critical journeys.
  + fastest to write, gentle ramp from manual QA
  - slower runtime; second-class in JS-heavy teams
  AI  best LLM support — the language of AI tooling
  Pick when  API-heavy testing, mixed-skill team

┌─ JAVASCRIPT ───────────────────────────────────────────────┐
  Unit  Jest/Vitest · Integration supertest, Pactum, MSW
  E2E   Playwright Test/Cypress · BDD Cucumber.js
  Perf  k6 · Data @faker-js/faker · Reports PW HTML, Allure
  Strategy  one language with the app: devs own unit/integration,
            QA owns E2E — components in Vitest, journeys in PW.
  + frontend devs contribute tests; huge ecosystem
  - forgotten await = flake; no compile-time safety
  AI  strong — AI test agents target JS/TS first
  Pick when  web-app team already lives in JS

┌─ TYPESCRIPT ───────────────────────────────────────────────┐
  Unit  Vitest · Integration supertest + zod (typed contracts)
  E2E   Playwright Test (best-in-class TS) · BDD playwright-bdd
  Perf  k6 · Data faker + Partial<T> builders · Reports PW, diffs
  Strategy  types are the first gate: tsc --noEmit fails broken
            page objects before a browser opens; then unit, then E2E.
  + compile-time safety at suite scale
  - build/tooling overhead; steeper ramp
  AI  excellent — types catch hallucinated APIs
  Pick when  modern web E2E, long-lived suites

┌─ JAVA ─────────────────────────────────────────────────────┐
  Unit  JUnit 5/TestNG + Mockito, AssertJ
  Integration  REST Assured, WireMock, Testcontainers
  E2E  Selenium 4+Selenide/Playwright Java · BDD Cucumber-JVM
  Perf  JMeter, Gatling · Data Datafaker, records · Reports Allure
  Strategy  enterprise pattern: Testcontainers spins real
            DBs/queues; Selenium Grid covers cross-browser E2E.
  + deepest Selenium/REST Assured ecosystem; most SDET jobs
  - verbose; slowest to prototype
  AI  good — Healenium (self-healing) is Java-first
  Pick when  enterprise/backend-heavy Java shop
````

## Exercise

List 10 checks for an app you know and assign each to a pyramid layer. Then pick your primary automation language using the tab criteria — write down your 3 reasons.
