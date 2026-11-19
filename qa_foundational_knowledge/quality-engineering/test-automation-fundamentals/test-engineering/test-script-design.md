# Design reliable test scripts

> **Learning objective** · Write a check that fails for the intended defect and explains the failure.

## ◈ Arrange, act, assert

For a shipping boundary test, arrange a cart worth exactly 5,000 cents, request the quote once, and assert the fee in cents. Keep the expected value visible beside the input. Computing the expectation with the same function used by the application would allow a shared defect to pass.

```java
int subtotalCents = 5000;
int actualFee = Shipping.fee(subtotalCents);
if (actualFee != 0) {
    throw new AssertionError("Expected free shipping at 5000 cents, got " + actualFee);
}
```

## Make the result reproducible

Use an independent fixture for each test. In browser checks, choose a stable locator and wait for the observable outcome. For an asynchronous quote, the presence of the result panel alone says little about whether the new amount has arrived. Keep navigation and selectors in page objects and the business expectation in the test.

In CI, run the same command and dependency versions used locally. When a check fails only on a remote browser, retain the browser version, screenshot and first failed assertion before retrying.

## ✎ Practice

Temporarily change the qualifying rule from `>= 5000` to `> 5000` in a practice copy. Predict the failing case, run it, then restore the rule. A useful boundary suite fails at 5,000 while the neighboring 4,999 and 5,001 cases keep their expected results.

[Explore the Java boundary example](../../test-automation-in-java/examples/java/ShippingTest.java)

---

[Return to the training guide](../README.md)
