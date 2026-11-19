# Selenium WebDriver with Java: Hands-On Tutorial

Verification figures below describe the reference lab runs. For checks performed on this copy and remaining environment requirements, see [validation notes](../VALIDATION.md).

**Level:** beginner to intermediate · **Time:** about 5–6 hours · **Last reviewed:** November 2026 (Selenium 4.45, TestNG 7.12, Cucumber 7.18, Java 17+)

Build a real Selenium framework step by step against the **K11 Practice Shop**, a small web app that runs on your machine. Each chapter teaches one skill, shows the framework code that uses it, and ends with the **review questions** for that skill. The project has been run end to end: **14 tests (10 TestNG + 4 Cucumber scenarios) pass**, serially and in parallel.

## Chapters

| # | Chapter | Technical topics |
|---|---|---|
| 1 | [Architecture and setup](01-architecture-and-setup.md) | How WebDriver works, Selenium Manager, Grid |
| 2 | [Locators](02-locators.md) | Locator strategy, CSS vs XPath, stable test IDs |
| 3 | [Waits and synchronization](03-waits-and-synchronization.md) | Implicit vs explicit vs fluent waits, stale elements, flaky tests |
| 4 | [Page Object Model and framework design](04-page-objects-and-framework-design.md) | Framework layers, ThreadLocal driver, config, a real parallel bug |
| 5 | [TestNG: data-driven, groups and parallel](05-testng-data-driven-and-parallel.md) | DataProvider, groups, suites, listeners, parallel execution |
| 6 | [BDD with Cucumber](06-bdd-with-cucumber.md) | Gherkin, step definitions, hooks, sharing state |
| 7 | [API setup, CI/CD, Grid and AI-assisted testing](07-api-setup-ci-grid-and-ai.md) | Fast stable tests, pipelines, cross-browser, self-healing |
| 8 | [Review questions and exercises](08-review-questions-and-exercises.md) | Rapid-fire Q&A, debugging scenarios, coding tasks |

## Quick start

```bash
# Terminal 1: the app under test
python ../practice-apps/shop-web/shop_web.py           # http://127.0.0.1:8091

# Terminal 2: the tests (Java 17+ and Maven 3.9+)
cd project
mvn test                                    # everything: TestNG + Cucumber
mvn test -Dsuite=testng-smoke.xml           # smoke group only
mvn test -Dsuite=testng-parallel.xml        # parallel on 3 threads
mvn test -Dheadless=false -Dbrowser=edge    # watch it run in Edge
```

## Project layout

```
project/
├── pom.xml                         Selenium, TestNG, Cucumber, AssertJ
├── testng.xml / testng-smoke.xml / testng-parallel.xml
└── src/test/
    ├── java/com/k11/shop/
    │   ├── config/Config.java      settings from -D properties or environment variables
    │   ├── driver/DriverFactory.java   ThreadLocal WebDriver, local or Grid
    │   ├── api/ShopApi.java        test setup through the API
    │   ├── pages/                  BasePage + one page object per page
    │   ├── tests/                  TestNG tests (BaseTest takes screenshots on failure)
    │   └── bdd/                    Cucumber runner, hooks, step definitions
    └── resources/features/checkout.feature
```

Next: [1. Architecture and setup](01-architecture-and-setup.md)
