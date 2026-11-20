# QA Automation Mastery System

[![QA Automation Mastery — Choose your move. Master the game of AI quality assurance. Open the learning planner.](assets/mastery-banner.svg)](planner.md)

Follow the [18-week beginner planner and AI QA mastery track](planner.md) for a weekly sequence, or choose a module below for focused practice. Each week covers one module in curriculum order. Each module links to its categorized lessons and a practice assignment. Browse all **269 topic entries**, or use the [19 review flashcards](flashcards.md) for recall practice.

| Schedule | Module | Description |
|---|---|---|
| Week 1 | [00 · SDLC & Test Engineering](modules/00-sdlc-test-engineering.md) | Trace a requirement through the SDLC and STLC. Distinguish strategy from a release plan, and connect exploratory testing, automation and model evaluation to release evidence. |
| Week 2 | [01 · Foundations](modules/01-foundations.md) | Use the test pyramid, boundary analysis and equivalence classes. Connect browser behavior to the DOM, HTTP and authorization. Compare Python, JavaScript, TypeScript and Java as automation stacks. |
| Week 3 | [02 · Test Harness Setup](modules/02-test-harness-setup.md) | Use Git, pinned dependencies, fixtures, data factories and teardown to make a suite reproducible. Inspect failures with DevTools and database queries before adding PR checks. |
| Week 4 | [03 · Language Deep Dives](modules/03-language-deep-dives.md) | Study generators and context managers in Python, promises and closures in JavaScript, narrowing and generics in TypeScript, and interfaces, streams and collections in Java. |
| Week 5 | [04 · Computational Logic](modules/04-computational-logic.md) | Use sets, maps, search and complexity analysis to reason about test utilities. Apply pairwise coverage, invariants and probability to test design. Connect confusion matrices and vector similarity to evaluation. |
| Week 6 | [05 · Networking](modules/05-networking.md) | Follow DNS resolution, TCP, TLS and an HTTP exchange. Read headers, cookies, cache behavior and browser CORS messages. Compare polling with WebSockets and server-sent events. |
| Week 7 | [06 · Selenium](modules/06-selenium.md) | Follow a WebDriver command from the test to the browser. Practice locators, explicit waits, frames, windows and alerts. Use page objects, listeners and Grid to organize and diagnose parallel runs. |
| Week 8 | [07 · Playwright](modules/07-playwright.md) | Separate browser, context and page lifetimes. Use semantic locators, web-first assertions, fixtures and controlled network responses. Read traces and distribute tests across projects and shards. |
| Week 9 | [08 · Frameworks & BDD](modules/08-frameworks-bdd.md) | Compare pytest fixtures and parametrization with TestNG lifecycle methods and DataProviders. Connect Gherkin scenarios to step definitions and Allure reports. Explore Robot Framework and UFT as alternative approaches. |
| Week 10 | [09 · API Testing](modules/09-api-testing.md) | Validate status, headers, response shape and business outcomes using requests, REST Assured or Playwright. Explore request chaining, schema validation, Pact contracts and resilience. |
| Week 11 | [10 · CI/CD & Infrastructure](modules/10-ci-cd-infrastructure.md) | Run suites in GitHub Actions or Jenkins with disposable Docker environments. Examine cloud browser coverage, suite selection, flaky-test triage, observability and progressive delivery. |
| Week 12 | [11 · Non-Functional Testing](modules/11-non-functional-testing.md) | Plan performance, accessibility, compatibility, usability and recovery checks. Define a workload and interpret response-time distributions. Include localization and security requirements in acceptance criteria. |
| Week 13 | [12 · Microservices Testing](modules/12-microservices-testing.md) | Map service boundaries, consumer contracts and dependencies. Use service virtualization with WireMock to make downstream behavior reproducible. |
| Week 14 | [13 · Test Automation System Design](modules/13-test-automation-system-design.md) | Compare Java, Python, TypeScript and .NET framework designs. Separate business language, test fixtures, clients and reporting. Plan how multiple stacks can exchange results. |
| Week 15 | [14 · AI-Augmented QA](modules/14-ai-augmented-qa.md) | Explore assisted test generation, flaky-test triage, self-healing locators, Healenium and visual testing platforms. Evaluate the evidence behind a proposed repair. |
| Week 16 | [15 · Autonomous QA Pipelines](modules/15-autonomous-qa-pipelines.md) | Trace a detect–fix–learn loop through planning, execution and review. Study agent tests, MCP tools, audit records, LangGraph orchestration and cross-repository impact analysis. |
| Week 17 | [16 · AI Testing & LLM Evaluation](modules/16-ai-testing-llm-evaluation.md) | Build evaluation datasets and rubrics, compare deterministic checks with model judges, and evaluate retrieval separately from generation. Connect eval results to CI gates or GitHub App checks. |
| Week 18 | [17 · Interview Prep](modules/17-interview-prep.md) | Practice coding, framework design and evidence-based QA stories. Apply them to a financial trading transfer, SQL validation, failure diagnosis, regression selection and handoff. |

The lesson pages include the default displayed reference or example. For alternate language tabs and interactive quizzes, the [interactive source artboard](https://claude.ai/artifact/HM3XUAEuQ29DSb9ckCWt6s#artboard-787b4c38acdc) provides the original interactive experience.

## Interactive dashboard and GitHub Pages

The [dashboard source](web/) builds a static site from this curriculum. It supports topic search, lesson completion, weekly planning, and flashcard practice. This browser stores your progress on this device.

To preview from the repository root with Python 3:

```sh
python3 "QA Automation Mastery System/web/build.py" _site
python3 -m http.server 8000 --directory _site
```

Open http://localhost:8000. To publish, choose **Settings → Pages → Build and deployment → GitHub Actions**. Run [Deploy QA Automation Mastery to Pages](https://github.com/kavitaj11/ai-quality-queens/actions/workflows/mastery-pages.yml). Later changes to this curriculum rebuild the site automatically. The deployment job shows the published URL.
