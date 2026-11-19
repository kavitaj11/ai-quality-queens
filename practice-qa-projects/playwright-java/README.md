# Playwright for Java Framework: Hands-On Tutorial

Verification figures below describe the reference lab runs. For checks performed on this copy and remaining environment requirements, see [validation notes](../VALIDATION.md).

**Level:** intermediate · **Time:** about 6 hours · **Last reviewed:** November 2026 (Playwright 1.58, Cucumber 7.15, JUnit 5.10, Java 17+)

A complete test framework in **Java + Playwright + Cucumber BDD**, covering reusable UI and API automation, schema validation, database checks, and CI integration:

| Requirement | Where it is in this framework |
|---|---|
| Proficiency in Java; clean, reusable code | Records, BigDecimal money, typed service objects, JUnit 5 extension |
| Playwright | UI tests with page objects, retrying assertions, tracing ([Chapter 2](02-locators-waiting-and-assertions.md)) |
| REST API testing in Java with `APIRequestContext` | `ShopApi` service object, standalone and browser-shared clients ([Chapter 4](04-api-testing-with-apirequestcontext.md)) |
| Cucumber BDD: Gherkin, step definitions, hooks | 3 feature files, 4 step classes, hooks, PicoContainer DI ([Chapter 6](06-cucumber-bdd.md)) |
| Reusable service/page objects | `pages/` and `services/` ([Chapter 3](03-page-objects-and-service-objects.md)) |
| JSON parsing and schema validation | Jackson records + JSON Schema files ([Chapter 5](05-json-parsing-and-schema-validation.md)) |
| Clean, data-driven tests | Scenario Outlines, DataTables, `@CsvFileSource` |
| SQL and database testing | JDBC checks against the app's SQLite database ([Chapter 7](07-sql-and-database-testing.md)) |
| Git and CI (Jenkins, GitLab CI) | `Jenkinsfile`, `.gitlab-ci.yml` ([Chapter 8](08-ci-debugging-and-reliability.md)) |

**Verified:** the full suite (**20 tests: 12 Cucumber scenarios, 7 data-driven/API tests, 1 UI test**) passes in about 37 seconds, and a deliberate negative control fails the build with traces and screenshots for every failure.

## Chapters

| # | Chapter | Technical topics |
|---|---|---|
| 1 | [Architecture and setup](01-architecture-and-setup.md) | Playwright vs Selenium, Browser/Context/Page, project setup |
| 2 | [Locators, waiting and assertions](02-locators-waiting-and-assertions.md) | Role locators, strict mode, retrying assertions, the JS-regex trap |
| 3 | [Page objects and service objects](03-page-objects-and-service-objects.md) | Framework layers, reusable objects, shared state |
| 4 | [API testing with APIRequestContext](04-api-testing-with-apirequestcontext.md) | Standalone vs browser-shared clients, auth tests, API setup for UI |
| 5 | [JSON parsing and schema validation](05-json-parsing-and-schema-validation.md) | Jackson, records, BigDecimal, JSON Schema |
| 6 | [Cucumber BDD and data-driven tests](06-cucumber-bdd.md) | Gherkin, DataTables, hooks, PicoContainer, JUnit 5 runner, tags |
| 7 | [SQL and database testing](07-sql-and-database-testing.md) | JDBC, PreparedStatement, reconciliation, audit checks |
| 8 | [CI, debugging and reliability](08-ci-debugging-and-reliability.md) | Jenkins, GitLab CI, traces, negative controls |
| 9 | [Review questions and exercises](09-review-questions-and-exercises.md) | Rapid-fire Q&A, debugging scenarios, coding tasks |

## Quick start

```bash
# Terminal 1: the app under test (now with a SQLite database)
python ../practice-apps/shop-web/shop_web.py

# Terminal 2: the tests (Java 17+, Maven 3.9+). The first run downloads Playwright's browsers.
cd project
mvn test                                         # everything
mvn test -Dcucumber.filter.tags="@smoke"         # smoke scenarios
mvn test -Dtest=ProductApiTest                   # one JUnit class
mvn test -Dheadless=false -DslowMo=300           # watch it run
```

## Project layout

```
project/
├── pom.xml                       Playwright, Cucumber (JUnit 5 + PicoContainer), Jackson, networknt, sqlite-jdbc
├── Jenkinsfile / .gitlab-ci.yml
└── src/test/
    ├── java/com/k11/pwj/
    │   ├── RunCucumberTest.java   JUnit 5 @Suite running the features
    │   ├── config/                Config: -D properties → env vars → defaults
    │   ├── support/               BrowserManager, ShopSession, TestContext, Hooks, PlaywrightExtension
    │   ├── pages/                 LoginPage, ProductsPage, ProductPage, CartPage, OrderPage, Money
    │   ├── services/ShopApi.java  REST service object over APIRequestContext
    │   ├── models/                Product, ProductSearch, Order (Java records)
    │   ├── json/                  Json (ObjectMapper), SchemaValidator
    │   ├── db/ShopDb.java         JDBC queries for verification
    │   ├── steps/                 Login, Shopping, OrderApi, Database step definitions
    │   └── tests/                 ProductApiTest (data-driven), CheckoutUiTest
    └── resources/
        ├── features/              login.feature, shopping.feature, orders_api.feature
        ├── schemas/               order.schema.json, product-search.schema.json
        ├── testdata/              product-search.csv
        └── junit-platform.properties
```

Next: [1. Architecture and setup](01-architecture-and-setup.md)
