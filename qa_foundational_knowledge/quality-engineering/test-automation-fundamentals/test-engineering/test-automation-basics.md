# Test automation basics

> **Learning objective** · Choose a test level and data setup for a specific business risk.

## ◈ Start with one rule

A shop charges 499 cents for shipping on subtotals below 5,000 cents. At exactly 5,000 cents, shipping becomes free. A shopper could lose money if the implementation uses the wrong comparison operator. This gives the test a purpose and an independent source for its expected result.

| Level | What to exercise | What a passing result establishes |
|---|---|---|
| Unit | Call the fee function with 4,999 and 5,000 | The calculation handles both sides of the threshold |
| Service | Submit a quote with a known cart | The endpoint returns the expected fee and units |
| Browser | Enter the qualifying subtotal and request a quote | The shopper can see the correct message |

## Build repeatable test data

Keep fixed inputs for exact boundaries. Generate unique account identifiers when tests create records in parallel. When a test requires an existing cart, create it through a fixture or API and retain its ID for cleanup. Storing examples in JSON or a database answers where data lives. Choosing who creates, owns and removes each record answers whether a test can run independently.

## ✎ Practice

A change translates “Free shipping” incorrectly while the fee remains zero. Which check should fail? The browser assertion on the displayed message should detect the defect. A fee calculation test can correctly remain green.

---

[Return to the training guide](../README.md)
