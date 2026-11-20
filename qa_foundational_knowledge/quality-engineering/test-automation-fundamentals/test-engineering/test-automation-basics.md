# Lesson 1 · Test automation basics

> **Learning objective** · Choose a test level and data setup for a specific business risk.

## Start with one rule

A shop charges 500 cents for shipping on subtotals below 5,000 cents. At exactly 5,000 cents, shipping becomes free. A shopper could lose money if the implementation uses the wrong comparison operator. This gives the test a purpose and an independent source for its expected result.

| Level | What to exercise | What a passing result establishes |
|---|---|---|
| Unit | Call the fee function with 4,999 and 5,000 | The calculation handles both sides of the threshold |
| Service | Submit a quote with a known cart | The endpoint returns the expected fee and units |
| Browser | Enter the qualifying subtotal and request a quote | The shopper can see the correct message |

## Build repeatable test data

Keep fixed inputs for exact boundaries. Generate unique account identifiers when tests create records in parallel. When a test requires an existing cart, create it through a fixture or API and retain its ID for cleanup. Storing examples in JSON or a database answers where data lives. Choosing who creates, owns and removes each record answers whether a test can run independently.

## Separate the rule from the implementation

Write the expected charge in cents beside each input. The business rule supplies the expected value, also called the test oracle. If the test calls the production fee function to calculate both sides of its assertion, the same error can occur twice and the comparison will still pass.

| Input class | Representative subtotal | Expected fee | Why include it? |
|---|---:|---:|---|
| Ordinary paid order | 3200 | 500 | Establish the usual paid path |
| Last paid cent | 4999 | 500 | Catch a threshold applied one cent early |
| First free cent | 5000 | 0 | Catch an exclusive comparison |
| Above threshold | 5001 | 0 | Detect an implementation that grants free shipping only at equality |

A negative subtotal raises a different question: is it invalid input, a return or a credit? Record that question for the rule owner before adding a guessed expectation.

## Choose the cheapest useful observation

A unit result isolates arithmetic. A service response can expose a cents-to-dollars conversion error even when arithmetic passes. A browser check can reveal that a correct zero fee is displayed as “Shipping unavailable.” Assign each suspected failure to the layer where it first becomes observable. Hand the resulting case list to the strategy author.

## Practice and self-check

A change translates “Free shipping” incorrectly while the fee remains zero. Which check should fail? The browser assertion on the displayed message should detect the defect. A fee calculation test can correctly remain green.

[Previous: Guide](README.md) · [Study sequence](README.md#study-sequence) · [Next: Risk-based strategy](test-automation-strategy.md)
