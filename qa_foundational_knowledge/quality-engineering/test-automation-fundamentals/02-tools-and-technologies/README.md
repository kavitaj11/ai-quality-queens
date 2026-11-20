# Tools and technologies

Use three views of the same order to connect a business requirement to a browser observation and a saved record. Start with a written rule and synthetic test data.

Start with the three foundational lessons, then choose a browser, mobile, performance, cloud or AI lab for your testing goal. Setup instructions and prerequisites are in the [Practice QA Projects catalog](../../../../practice-qa-projects/).

## Study sequence

| Lesson | Question it helps answer | Hands-on project | QA Automation Mastery |
|---|---|---|---|
| 1. [Cucumber and Gherkin](cucumber-and-gherkin.md) | Can a product owner recognize the business rule in the example? | [BDD with Cucumber lab](../../../../practice-qa-projects/selenium-java/06-bdd-with-cucumber.md) | [08 · Frameworks & BDD](../../../../QA%20Automation%20Mastery%20System/modules/08-frameworks-bdd.md) |
| 2. [Selenium WebDriver](selenium-webdriver.md) | Can a customer complete the interaction and see its outcome? | [Selenium with Java practice project](../../../../practice-qa-projects/selenium-java/) | [06 · Selenium](../../../../QA%20Automation%20Mastery%20System/modules/06-selenium.md) |
| 3. [SQL for testers](sql-for-testers.md) | Did the application save the expected state for this order? | [SQL and data pipeline testing lab](../../../../practice-qa-projects/data-quality-testing-lab/) | [02 · Test Harness Setup](../../../../QA%20Automation%20Mastery%20System/modules/02-test-harness-setup.md) · [Database validation](../../../../QA%20Automation%20Mastery%20System/lessons/02-test-harness-setup/14-database-validation.md) |
| 4. [Playwright with Python and pytest](playwright-python.md) | Did the browser render the state returned by the service? | [Hands-on lab](../../../../practice-qa-projects/playwright-python/README.md) | [07 · Playwright](../../../../QA%20Automation%20Mastery%20System/modules/07-playwright.md) |
| 5. [Playwright with Java and APIRequestContext](playwright-java.md) | Does an API result agree with the browser and stored record? | [Hands-on lab](../../../../practice-qa-projects/playwright-java/README.md) | [09 · API Testing](../../../../QA%20Automation%20Mastery%20System/modules/09-api-testing.md) |
| 6. [Appium with Java](appium-java.md) | Does the mobile journey work in the intended context? | [Hands-on lab](../../../../practice-qa-projects/appium-java/README.md) | [08 · Frameworks & BDD](../../../../QA%20Automation%20Mastery%20System/modules/08-frameworks-bdd.md) |
| 7. [Apache JMeter](performance-testing-jmeter.md) | At what workload does a customer journey breach its target? | [Hands-on lab](../../../../practice-qa-projects/performance-testing-jmeter/README.md) | [11 · Non-Functional Testing](../../../../QA%20Automation%20Mastery%20System/modules/11-non-functional-testing.md) |
| 8. [Grafana k6](performance-testing-k6.md) | Did the system meet its latency and error targets at the intended arrival rate? | [Hands-on lab](../../../../practice-qa-projects/performance-testing-k6/README.md) | [11 · Non-Functional Testing](../../../../QA%20Automation%20Mastery%20System/modules/11-non-functional-testing.md) |
| 9. [AWS and Google Cloud storage testing](cloud-testing-aws-gcp.md) | Do provider adapters preserve the same storage contract? | [Hands-on lab](../../../../practice-qa-projects/cloud-testing-aws-gcp/README.md) | [10 · CI/CD & Infrastructure](../../../../QA%20Automation%20Mastery%20System/modules/10-ci-cd-infrastructure.md) |
| 10. [AI evaluation and agent traces](ai-quality-engineering.md) | Did the agent take the expected action and support its answer? | [Hands-on lab](../../../../practice-qa-projects/ai-quality-engineering/README.md) | [16 · AI Testing & LLM Evaluation](../../../../QA%20Automation%20Mastery%20System/modules/16-ai-testing-llm-evaluation.md) |

## Worked example

A customer confirms a draft order once. Capture the draft order ID during setup. In Gherkin, describe the transition from draft to confirmed. In Selenium, wait for the confirmation for that ID. In SQL, retrieve the matching order and check its status. A confirmation banner alone cannot tell you which order was saved.

## Follow one order across three layers

Write the customer-facing example first. The order ID connects the observations across layers.

```gherkin
Scenario: A customer confirms a draft order
  Given order "QA-104" is a draft owned by the customer
  When the customer confirms the order
  Then the confirmation shows order "QA-104"
```

For a Selenium implementation, arrange a fresh draft for each run and substitute its generated ID in the expectation. Click Confirm once. Wait for the confirmation element to show that ID and capture its displayed text if the wait expires. Repeated clicks during a wait can create a second submission.

Use this standalone SQLite fixture to practice the persistence check. The second row deliberately has the same status as the first.

```sql
CREATE TABLE orders (id TEXT PRIMARY KEY, status TEXT NOT NULL);
INSERT INTO orders VALUES ('QA-104', 'draft'), ('QA-105', 'draft');
UPDATE orders SET status = 'confirmed' WHERE id = 'QA-104';
SELECT id, status FROM orders ORDER BY id;
```

The expected rows are `QA-104 | confirmed` and `QA-105 | draft`. In an integrated test, the application performs the update and the test only reads the resulting rows. A query that counts all confirmed orders could pass because another test confirmed a different order.

## Practice assignment

Produce one scenario, one browser assertion and one database assertion for this rule. Explain how you would interpret a passing browser check alongside a row that still says “draft.” If persistence is asynchronous, specify the agreed time limit before treating the mismatch as a failure.

[Fundamentals training guide](../README.md)
