# Lesson 4 · Design a test automation framework

> **Learning objective** · Trace a test through setup, execution, reporting and cleanup.

## Follow one checkout test

A runner selects the checkout test. A fixture creates a cart in the configured environment. A page object enters the subtotal and reads the displayed fee. The test compares that fee with the business expectation. Teardown closes the browser and removes the cart, including when the assertion fails.

| Component | Responsibility | Design question |
|---|---|---|
| Configuration | Supply base URL, browser and timeouts | Can the same suite run locally and in CI? |
| Fixtures and clients | Create data and call services | Does each parallel worker own its records? |
| Page objects | Expose user actions and observable state | Can a locator change be fixed in one place? |
| Runner and reporting | Select suites and record outcomes | Can someone diagnose a failure from the report? |

## Design the failure path

For a failed checkout, capture the expected fee, observed fee, build, browser and relevant screenshot. A correlation ID can connect the test to service logs. Preserve the first attempt when a retry succeeds. Otherwise a flaky dependency can make the trend report look healthier than the user experience. Link a reproducible failure to its issue or test-management record.

## Trace one run across component boundaries

1. **Runner** selects the case and records the build and worker ID.
2. **Fixture** creates a uniquely identified cart and registers cleanup immediately after creation.
3. **Service client or page object** submits the quote request and returns the observed fee and cart ID.
4. **Test** compares the returned fee with the expected value from its case table.
5. **Reporter and teardown** retain the first failure, then release the browser and remove the owned cart. Report cleanup errors separately so the original failure remains visible.

## Diagnose framework failures

| Symptom | Evidence to inspect | Design change to try |
|---|---|---|
| Parallel workers overwrite one cart | Compare fixture IDs and cleanup receipts | Allocate records per invocation, including retries. |
| A selector change breaks twelve tests | Identify where the same locator was defined | Centralize the checkout action; keep each business assertion in its test. |
| A retry passes after a timeout | Compare both attempt IDs, trace and dependency timing | Report a recovered retry separately and investigate the first attempt. |
| Setup creates a cart but browser launch fails | Check whether teardown ever received the cart ID | Register cleanup at allocation time and test partial setup failure. |

Exercise the framework itself with a fake cart client. Make it record created and deleted IDs, then simulate an assertion failure. The expected record contains one deletion for the owned cart and zero deletions for another worker's cart.

## Practice and self-check

Two workers pass independently but fail together when deleting the same cart. Which component should change? Give the fixture a unique cart per worker and delete only the ID it created. A longer timeout would leave the ownership problem in place.

[Previous: Test script design](test-script-design.md) · [Study sequence](README.md#study-sequence) · [Next: Entry and exit criteria](test-entry-and-exit-criteria.md)
