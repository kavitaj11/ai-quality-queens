# Lesson 5 · Use entry and exit criteria

> **Learning objective** · Decide whether a test phase can start or finish using evidence.

## Agree on evidence before execution

Entry criteria establish the conditions for a meaningful run. Exit criteria define what evidence the next decision requires. Set thresholds with the people accountable for the release. A pass rate alone can hide an untested critical journey.

| Phase | Example entry criteria | Example exit evidence |
|---|---|---|
| Component or segment | Deployable build, known inputs and agreed interface | Boundary and error paths exercised, defects triaged |
| End-to-end | Integrated services, usable accounts and traceable transactions | Priority journeys completed across system boundaries |
| Regression | Change scope known and baseline suite available | Impacted areas covered and failures explained |
| User acceptance | Business scenarios and representative data approved | Business owner records acceptance or outstanding risks |

## Read the denominator

A run reports 95 passes, 3 failures and 2 blocked cases. Of 100 planned checks, 98 executed and 95 passed. The pass rate among executed checks is 95/98, about 96.9%. If a blocked case covers refund settlement, report that gap explicitly. Severity describes impact, while the decision to proceed also depends on exposure, workarounds and ownership.

## Apply criteria to a candidate build

Agree before execution that the discounted quote must match its invoice and that every threshold check must pass on the release candidate. Record the build, environment, rule version and owner beside each criterion. A previous build's successful invoice test can support investigation, but it cannot establish what the current candidate stored.

| Evidence on the candidate | Interpretation | Next action |
|---|---|---|
| Exact-threshold quote charges 500 cents | Confirmed violation of the approved rule | Hold, fix, then rerun the boundary family. |
| Unit boundaries pass and billing is unavailable | Cross-system outcome is unknown | Restore the dependency and reconcile one traceable transaction. |
| Corrected build passes both boundaries and invoice reconciliation | Required pricing evidence is available | Review the remaining criteria and record the release owner's decision. |

## Write a decision another engineer can audit

Example: “Hold candidate B17. Cart C42 qualified for free shipping at 5000 cents, but the quote charged 500. Billing reconciliation was blocked. The pricing owner will correct the comparison; QA will rerun all three boundaries and reconcile the invoice on the replacement build.” This separates the observed defect from the missing observation and gives each an exit condition.

## Practice and self-check

All executed tests pass, but the payment sandbox was unavailable. Can the end-to-end phase close? Check the agreed exit evidence. If payment completion is required, record the blocked journey and its owner. An exception requires an explicit decision and a plan to obtain the missing evidence.

[Previous: Framework design](test-automation-framework.md) · [Study sequence](README.md#study-sequence) · [Finish the guide assignment](README.md#practice-assignment)
