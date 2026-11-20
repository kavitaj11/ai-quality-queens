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

## Practice assignment

Write the three expected shipping outcomes before running the checks. Assume the exact-threshold check then fails and the browser check passes. Draft a release note naming the affected customer, the failing input and the evidence you would request after a fix. A reviewer should be able to reproduce the failure from your note.

[Related mastery module](../../../../QA%20Automation%20Mastery%20System/modules/00-sdlc-test-engineering.md) · [Fundamentals training guide](../README.md)
