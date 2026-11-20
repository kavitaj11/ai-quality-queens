# Lesson 2 · Build a risk-based test strategy

**QA Automation Mastery** · [00 · SDLC & Test Engineering](../../../../QA%20Automation%20Mastery%20System/modules/00-sdlc-test-engineering.md)  
**Hands-on project** · [Select TestNG groups for a run](../../../../practice-qa-projects/selenium-java/05-testng-data-driven-and-parallel.md)

> **Learning objective** · Turn a release objective into coverage, ownership and evidence.

## Frame the decision

Suppose a checkout release changes how discounted subtotals qualify for free shipping. First establish which subtotal the business rule uses and which systems calculate, display and store the fee. An unresolved rounding rule affects the expected result at every layer. Resolve it with the product owner before estimating the boundary cases.

| Risk | Planned coverage | Owner and evidence |
|---|---|---|
| Wrong fee near the threshold | Unit cases just below, at and above it | Developer supplies calculation results on each change |
| Quote and invoice disagree | Service test follows one cart through billing | QA engineer retains cart ID, response and invoice amount |
| Checkout slows under load | Representative traffic mix in a performance environment | Performance engineer reports latency and errors against agreed limits |

## Define execution and communication

Document the target build, dependencies, test accounts and exclusions. If the payment sandbox is unavailable, a stub can support component testing. Record that the cross-system journey is still pending. Schedule fast checks on pull requests, broader regression on an integrated build, and a smoke check after deployment. Assign someone to investigate each failure and communicate any remaining exposure to the release owner.

## Turn a change request into a coverage plan

The product owner confirms that eligibility uses the subtotal **after discount and before tax**. A cart starts at 5100 cents and receives a 200-cent discount. Its qualifying subtotal is 4900 cents, so shipping costs 500 cents. A suite using only carts without discounts could pass while the service still uses the wrong subtotal.

| Stage | Coverage for this change | Evidence needed before moving on |
|---|---|---|
| Pull request | Pricing boundaries and the discount counterexample | Case ID, rule version and actual fee |
| Integrated candidate | Quote and invoice for the same discounted cart | Both amounts, cart ID and service revisions |
| Release smoke | One representative checkout with the displayed charge | Candidate build and observed customer journey |

## Decide what to defer

Suppose the billing sandbox is down. A fixture can confirm the quote payload and a contract check can confirm its shape. Neither observes the fee that billing actually persists. Record invoice reconciliation as blocked, give it an owner and identify the event that triggers a rerun. If traffic patterns are unchanged, a team might defer a broad endurance run while protecting the changed pricing cases. Explain the assumption and revisit it if the implementation adds a remote call per quote.

## Practice and self-check

The release date moves forward one week. Which checks would you protect? Prioritize the boundary calculation and quote-to-invoice journey because they cover the changed rule and financial impact. Record what was deferred, why, and who accepted that decision.

[Previous: Automation basics](01-test-automation-basics.md) · [Study sequence](README.md#study-sequence) · [Next: Test script design](03-test-script-design.md)
