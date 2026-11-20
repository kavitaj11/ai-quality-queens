# Lesson 3 · Design reliable test scripts

> **Learning objective** · Write a check that fails for the intended defect and explains the failure.

## Arrange, act, assert

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

## Turn one example into a diagnostic suite

Keep the input and expected charge together as test data. Run each row independently through the same assertion. Give the exact-threshold row a descriptive case ID so CI can identify the affected behavior even when several rows fail. For the discount case from lesson 2, record both original and effective subtotals.

| Observation | First diagnostic step |
|---|---|
| Only the 5000-cent case fails | Inspect threshold inclusivity and confirm units. |
| Unit cases pass but the API returns 5 instead of 500 | Compare the response contract and currency representation. |
| The browser shows a previous cart's quote | Check cart identity and the condition used to wait for the new quote. |
| Setup cannot create a cart | Report the setup failure separately; the pricing assertion never ran. |

## Prove the assertion is sensitive

Use the practice mutation below as a negative control. Capture a report such as `free-at-threshold: subtotal=5000 expectedFee=0 actualFee=500`. Restore the comparison and rerun the same inputs. Preserve both reports with the change revision. If all rows stay green under the mutation, investigate whether the test ran, whether it called the intended implementation and whether its assertion compared independent values.

## Practice and self-check

Temporarily change the qualifying rule from `>= 5000` to `> 5000` in a practice copy. Predict the failing case, run it, then restore the rule. A useful boundary suite fails at 5,000 while the neighboring 4,999 and 5,001 cases keep their expected results.

[Explore the Java boundary example](../../test-automation-in-java/examples/java/ShippingTest.java)

[Previous: Risk-based strategy](test-automation-strategy.md) · [Study sequence](README.md#study-sequence) · [Next: Framework design](test-automation-framework.md)
