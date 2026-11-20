# Test engineering

Learn to turn a business rule into a repeatable test and use the result to make a release decision. Start here if you can describe a feature but are unsure what to automate first.

## Study sequence

| Lesson | What to produce |
|---|---|
| 1. [Test automation basics](test-automation-basics.md) | Write one rule with inputs, an expected result and repeatable test data. |
| 2. [Risk-based strategy](test-automation-strategy.md) | Rank the ways the rule could fail. Choose the test level that gives the clearest evidence. |
| 3. [Test script design](test-script-design.md) | Separate setup, action and assertion. Make the failure message identify the input that failed. |
| 4. [Framework design](test-automation-framework.md) | Decide where fixtures, clients, assertions and failure evidence belong. |
| 5. [Entry and exit criteria](test-entry-and-exit-criteria.md) | Define when execution can begin and what evidence is needed for release. |

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
