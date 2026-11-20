# Tools and technologies

Use three views of the same order to connect a business requirement to a browser observation and a saved record. Start with a written rule and synthetic test data.

## Study sequence

| Lesson | Question it helps answer |
|---|---|
| 1. [Cucumber and Gherkin](cucumber-and-gherkin.md) | Can a product owner recognize the business rule in the example? |
| 2. [Selenium WebDriver](selenium-webdriver.md) | Can a customer complete the interaction and see its outcome? |
| 3. [SQL for testers](sql-for-testers.md) | Did the application save the expected state for this order? |

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

[Related mastery module](../../../../QA%20Automation%20Mastery%20System/modules/08-frameworks-bdd.md) · [Fundamentals training guide](../README.md)
