# Cucumber and Gherkin

**Hands-on project** · [BDD with Cucumber lab](../../../../practice-qa-projects/selenium-java/06-bdd-with-cucumber.md)

**QA Automation Mastery** · [08 · Frameworks & BDD](../../../../QA%20Automation%20Mastery%20System/modules/08-frameworks-bdd.md)

> **Learning objective** · Express one business decision as executable examples.

## How an executable specification works

Behavior-driven development begins with a conversation about an observable business rule. Gherkin gives that conversation a readable structure. **Given** describes the starting state, **When** identifies the event or decision under examination, and **Then** states the outcome a stakeholder can recognize. Agree on that outcome before writing automation. Otherwise a team can automate a misunderstanding and still get a passing result.

Cucumber connects each step to a step definition. The definition prepares a fixture, performs an action or checks an observation. Keep UI selectors and service calls inside that implementation so a change in delivery mechanism does not force a rewrite of the business rule. Use a Scenario Outline when several inputs exercise the same decision. Give genuinely different rules their own scenarios so a failure names the behavior that changed.

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
