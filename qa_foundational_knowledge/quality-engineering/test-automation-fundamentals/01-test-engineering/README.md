# Test engineering

Learn to turn a business rule into a repeatable test and use the result to make a release decision. Start here if you can describe a feature but are unsure what to automate first.

## How to use this guide

Carry one checkout change through all five lessons. You can complete the design exercises with a text editor. The optional Java example in lesson 3 illustrates an executable assertion. For the core case, use one eligible item, USD integer cents, a $5 shipping fee and a $50 threshold. Lesson 2 introduces a discount as a change request.

Keep a small evidence log with the rule version, input, expected result, observed result, build and decision. Write expectations before inspecting the implementation. Each lesson adds something a teammate would need to reproduce or challenge your final recommendation.

## Study sequence

| Lesson | What to produce | Mastery module | Hands-on project |
|---|---|---|---|
| 1. [Test automation basics](01-test-automation-basics.md) | Write one rule with inputs, an expected result and repeatable test data. | [01 · Foundations](../../../../QA%20Automation%20Mastery%20System/modules/01-foundations.md) | [Set up the Selenium project](../../../../practice-qa-projects/selenium-java/01-architecture-and-setup.md) |
| 2. [Risk-based strategy](02-test-automation-strategy.md) | Rank the ways the rule could fail. Choose the test level that gives the clearest evidence. | [00 · SDLC & Test Engineering](../../../../QA%20Automation%20Mastery%20System/modules/00-sdlc-test-engineering.md) | [Select TestNG groups for a run](../../../../practice-qa-projects/selenium-java/05-testng-data-driven-and-parallel.md) |
| 3. [Test script design](03-test-script-design.md) | Separate setup, action and assertion. Make the failure message identify the input that failed. | [01 · Foundations](../../../../QA%20Automation%20Mastery%20System/modules/01-foundations.md) | [Design observable waits](../../../../practice-qa-projects/selenium-java/03-waits-and-synchronization.md) |
| 4. [Framework design](04-test-automation-framework.md) | Decide where fixtures, clients, assertions and failure evidence belong. | [13 · Test Automation System Design](../../../../QA%20Automation%20Mastery%20System/modules/13-test-automation-system-design.md) | [Build a page-object framework](../../../../practice-qa-projects/selenium-java/04-page-objects-and-framework-design.md) |
| 5. [Entry and exit criteria](05-test-entry-and-exit-criteria.md) | Define when execution can begin and what evidence is needed for release. | [00 · SDLC & Test Engineering](../../../../QA%20Automation%20Mastery%20System/modules/00-sdlc-test-engineering.md) | [Inspect CI execution and reports](../../../../practice-qa-projects/selenium-java/07-api-setup-ci-grid-and-ai.md) |

## Worked decision

Suppose checkout grants free shipping on a subtotal of $50 or more before tax. Test $49.99, $50.00 and $50.01 at the pricing layer. The exact threshold distinguishes “at least” from “greater than.” Add one browser check to show that the displayed total reflects the pricing decision.

## Build a boundary test from the rule

For this exercise, paid shipping costs $5 and the cart contains one eligible item. Store money as integer cents in the fixture. Keep taxes, coupons and currency conversion outside this example so a failure points to the threshold rule.

| Subtotal in cents | Expected shipping in cents | Reason for this case |
|---:|---:|---|
| 4999 | 500 | Last amount below the threshold |
| 5000 | 0 | First amount that qualifies |
| 5001 | 0 | First amount above the threshold |

Use each row as input to the same pricing test. Assert the shipping charge separately from the final total. Include the subtotal, expected charge and actual charge in the failure message. At the browser layer, use one qualifying cart and check the shipping line a customer sees.

## Read the run and make a decision

Suppose the three actual charges are 500, 500 and 0 cents. Two cases passed, yet a customer spending exactly $50 would pay $5 too much. Record the input and the offending charge, assign the pricing rule to an owner, and rerun all three boundaries after the change. The below-threshold rerun catches a fix that accidentally makes every order ship free.

## Practice assignment

Write the three expected shipping outcomes before running the checks. Assume the exact-threshold check then fails and the browser check passes. Draft a release note naming the affected customer, the failing input and the evidence you would request after a fix. A reviewer should be able to reproduce the failure from your note.

[Related mastery module](../../../../QA%20Automation%20Mastery%20System/modules/00-sdlc-test-engineering.md) · [Fundamentals training guide](../README.md)
