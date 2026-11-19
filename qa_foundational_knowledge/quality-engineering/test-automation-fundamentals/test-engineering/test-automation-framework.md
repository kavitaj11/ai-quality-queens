# Design a test automation framework

> **Learning objective** · Trace a test through setup, execution, reporting and cleanup.

## ◈ Follow one checkout test

A runner selects the checkout test. A fixture creates a cart in the configured environment. A page object enters the subtotal and reads the displayed fee. The test compares that fee with the business expectation. Teardown closes the browser and removes the cart, including when the assertion fails.

| Component | Responsibility | Design question |
|---|---|---|
| Configuration | Supply base URL, browser and timeouts | Can the same suite run locally and in CI? |
| Fixtures and clients | Create data and call services | Does each parallel worker own its records? |
| Page objects | Expose user actions and observable state | Can a locator change be fixed in one place? |
| Runner and reporting | Select suites and record outcomes | Can someone diagnose a failure from the report? |

## Design the failure path

For a failed checkout, capture the expected fee, observed fee, build, browser and relevant screenshot. A correlation ID can connect the test to service logs. Preserve the first attempt when a retry succeeds. Otherwise a flaky dependency can make the trend report look healthier than the user experience. Link a reproducible failure to its issue or test-management record.

## ✎ Practice

Two workers pass independently but fail together when deleting the same cart. Which component should change? Give the fixture a unique cart per worker and delete only the ID it created. A longer timeout would leave the ownership problem in place.

---

[Return to the training guide](../README.md)
