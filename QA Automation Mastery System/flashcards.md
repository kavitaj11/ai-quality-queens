# Flashcards

[Curriculum index](README.md)

Open each question to check your answer.

## Selenium

<details>
<summary>Difference between implicit and explicit waits?</summary>

Implicit: global poll for element presence, set once. Explicit: per-condition wait (clickable, visible…) with its own timeout. Never mix — combined they cause unpredictable delays.

</details>

<details>
<summary>What is StaleElementReferenceException?</summary>

The element reference points to a DOM node that was removed or re-rendered. Fix: re-locate the element after the DOM changes, or wrap in a retry/wait.

</details>

<details>
<summary>findElement vs findElements?</summary>

findElement throws NoSuchElementException if nothing matches; findElements returns an empty list. Use findElements to assert absence without exceptions.

</details>

## Playwright

<details>
<summary>What makes Playwright assertions 'web-first'?</summary>

expect() polls the condition until it passes or times out, so assertions double as waits — no explicit synchronization needed.

</details>

<details>
<summary>How does storageState speed up suites?</summary>

Log in once in a setup project, save cookies/localStorage to a file, and every test's context starts pre-authenticated — skipping the login UI thousands of times.

</details>

<details>
<summary>page.route() vs API mocking at the backend?</summary>

route() intercepts in the browser context — no backend changes, per-test control, works on any environment. Ideal for error-state UI testing.

</details>

## pytest

<details>
<summary>Fixture scopes in pytest?</summary>

function (default), class, module, package, session. Wider scope = shared instance = faster but risk of state leaking between tests.

</details>

<details>
<summary>What does conftest.py do?</summary>

Shares fixtures and hooks with all tests in its directory tree, without imports. Nested conftest files override outer ones.

</details>

## TypeScript

<details>
<summary>Why prefer TS over JS for test suites?</summary>

Typed page objects and fixtures catch broken call sites at compile time; tsc --noEmit in CI fails the build before a single browser launches.

</details>

## Java

<details>
<summary>TestNG annotation execution order?</summary>

@BeforeSuite → @BeforeTest → @BeforeClass → @BeforeMethod → @Test → @AfterMethod → @AfterClass → @AfterTest → @AfterSuite.

</details>

## API

<details>
<summary>PUT vs PATCH?</summary>

PUT replaces the whole resource (idempotent); PATCH applies a partial modification. Sending PATCH twice may not be idempotent depending on the operation.

</details>

<details>
<summary>What is consumer-driven contract testing?</summary>

Consumers publish the exact requests/responses they rely on (e.g. Pact); providers verify against those in CI. Catches integration breaks without integrated environments.

</details>

## CI/CD

<details>
<summary>What is test sharding?</summary>

Splitting one suite across N machines by file/test, each running 1/N. Playwright: --shard=1/4. Cuts wall-clock time roughly linearly.

</details>

<details>
<summary>Retry vs fixing flaky tests?</summary>

Retries are a containment measure that hides signal. Budget them (1 retry, tracked), quarantine repeat offenders, and root-cause: race, data, environment, or ordering.

</details>

## Design

<details>
<summary>Why do POM methods return page objects?</summary>

It encodes navigation in types (login() → DashboardPage), enables fluent chains, and makes invalid flows fail at write time instead of runtime.

</details>

## Microservices

<details>
<summary>Why thin E2E in microservices?</summary>

Cross-service E2E is slow, flaky, and hard to provision. Contracts + per-service integration cover the interactions; keep 2-3 E2E smoke journeys.

</details>

## AI

<details>
<summary>Danger of self-healing locators?</summary>

Silent healing can mask real regressions — a renamed or moved element might be a genuine user-facing change. Heals must be logged and reviewed.

</details>

<details>
<summary>How does Healenium heal a broken locator?</summary>

A proxy around WebDriver stores successful locator/DOM history; on NoSuchElement it substitutes the highest-scoring historical match (tree comparison) and logs the heal for review.

</details>

<details>
<summary>What is Applitools Visual AI?</summary>

ML-based visual assertions (Eyes SDK): compares renders like a human — ignoring anti-aliasing noise while catching real layout/content regressions — instead of brittle pixel-diffs.

</details>
