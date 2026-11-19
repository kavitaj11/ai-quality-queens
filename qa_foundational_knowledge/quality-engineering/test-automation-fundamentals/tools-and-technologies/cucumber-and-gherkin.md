# Cucumber and Gherkin

> **Learning objective** · Express one business decision as executable examples.

## ◈ Describe the decision in domain language

A feature groups related behavior. Each scenario establishes context with **Given**, performs the action with **When**, and states an observable result with **Then**. A scenario outline applies that sequence to each row in its Examples table.

```gherkin
Feature: Shipping charges
  Scenario Outline: Quote shipping at the free-shipping boundary
    Given a subtotal of <subtotal> cents
    When shipping is calculated
    Then the fee is <fee> cents

    Examples:
      | subtotal | fee |
      | 4999     | 499 |
      | 5000     | 0   |
```

Step definitions connect these phrases to executable code. The Given step stores the subtotal for this scenario. When invokes the calculation, and Then compares the result with the explicit fee. Keep scenario state isolated so the examples can run in either order.

## Choose the right Gherkin construct

Use a **Background** for short preconditions shared by every scenario in the feature. A **Data Table** supplies several records to one step, such as three line items in a single cart. An **Examples** table runs the whole outline once per row. Tags such as `@smoke` let the runner select a subset. Doc strings carry multiline inputs such as a response body.

## ✎ Practice

Add a 5,001-cent row. Expect a zero fee and reuse the existing steps. Then consider how to describe a cart with two items whose combined value reaches the threshold. That is one cart with two input records, a suitable use for a Data Table.

[Inspect the runnable Cucumber-JVM project](../../test-automation-in-java/examples/cucumber)

---

[Return to the training guide](../README.md)
