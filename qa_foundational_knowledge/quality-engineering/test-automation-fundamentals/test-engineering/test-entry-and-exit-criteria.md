# Use entry and exit criteria

> **Learning objective** · Decide whether a test phase can start or finish using evidence.

## ◈ Agree on evidence before execution

Entry criteria establish the conditions for a meaningful run. Exit criteria define what evidence the next decision requires. Set thresholds with the people accountable for the release. A pass rate alone can hide an untested critical journey.

| Phase | Example entry criteria | Example exit evidence |
|---|---|---|
| Component or segment | Deployable build, known inputs and agreed interface | Boundary and error paths exercised, defects triaged |
| End-to-end | Integrated services, usable accounts and traceable transactions | Priority journeys completed across system boundaries |
| Regression | Change scope known and baseline suite available | Impacted areas covered and failures explained |
| User acceptance | Business scenarios and representative data approved | Business owner records acceptance or outstanding risks |

## Read the denominator

A run reports 95 passes, 3 failures and 2 blocked cases. Of 100 planned checks, 98 executed and 95 passed. The pass rate among executed checks is 95/98, about 96.9%. If a blocked case covers refund settlement, report that gap explicitly. Severity describes impact, while the decision to proceed also depends on exposure, workarounds and ownership.

## ✎ Practice

All executed tests pass, but the payment sandbox was unavailable. Can the end-to-end phase close? Check the agreed exit evidence. If payment completion is required, record the blocked journey and its owner. An exception requires an explicit decision and a plan to obtain the missing evidence.

---

[Return to the training guide](../README.md)
