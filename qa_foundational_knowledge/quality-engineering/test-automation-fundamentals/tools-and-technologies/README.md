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

## Practice assignment

Produce one scenario, one browser assertion and one database assertion for this rule. Explain how you would interpret a passing browser check alongside a row that still says “draft.” If persistence is asynchronous, specify the agreed time limit before treating the mismatch as a failure.

[Related mastery module](../../../../QA%20Automation%20Mastery%20System/modules/08-frameworks-bdd.md) · [Fundamentals training guide](../README.md)
