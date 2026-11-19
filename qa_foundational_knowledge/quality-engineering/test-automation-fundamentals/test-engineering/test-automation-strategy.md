# Build a risk-based test strategy

> **Learning objective** · Turn a release objective into coverage, ownership and evidence.

## ◈ Frame the decision

Suppose a checkout release changes how discounted subtotals qualify for free shipping. First establish which subtotal the business rule uses and which systems calculate, display and store the fee. An unresolved rounding rule affects the expected result at every layer. Resolve it with the product owner before estimating the boundary cases.

| Risk | Planned coverage | Owner and evidence |
|---|---|---|
| Wrong fee near the threshold | Unit cases just below, at and above it | Developer supplies calculation results on each change |
| Quote and invoice disagree | Service test follows one cart through billing | QA engineer retains cart ID, response and invoice amount |
| Checkout slows under load | Representative traffic mix in a performance environment | Performance engineer reports latency and errors against agreed limits |

## Define execution and communication

Document the target build, dependencies, test accounts and exclusions. If the payment sandbox is unavailable, a stub can support component testing. Record that the cross-system journey is still pending. Schedule fast checks on pull requests, broader regression on an integrated build, and a smoke check after deployment. Assign someone to investigate each failure and communicate any remaining exposure to the release owner.

## ✎ Practice

The release date moves forward one week. Which checks would you protect? Prioritize the boundary calculation and quote-to-invoice journey because they cover the changed rule and financial impact. Record what was deferred, why, and who accepted that decision.

---

[Return to the training guide](../README.md)
