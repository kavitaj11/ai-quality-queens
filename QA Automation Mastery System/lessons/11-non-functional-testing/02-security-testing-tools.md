# Security testing tools

[11 · Module topics](../../modules/11-non-functional-testing.md) · [Curriculum index](../../README.md)

**Category** · SECURITY TESTING

Dependency analysis identifies packages with published advisories. Static analysis highlights risky patterns in source code, and secret scanning flags potential credential exposure. Triage a result by checking the affected version, the relevant code path, the owner and the available remediation.

## Choose a tool for the question

Use dependency analysis, such as Dependabot alerts, to compare resolved package versions with published advisories. Use static analysis, such as CodeQL, when reviewing a source-code pattern flagged in a change. Use secret scanning when reviewing a potential credential exposure. Each produces a lead that needs different evidence.

A dependency finding needs the package version and its path into the build. A static finding needs the relevant code and review context. For a secret alert, record its location and follow the team's credential-response process without copying the value into a ticket.

## Worked example: triage a dependency alert

The following package and advisory are fictional. A learner is reviewing the order service before release.

````text
Package              example-renderer
Affected versions    2.1.0 through 2.1.3
First fixed version  2.1.4
Manifest range       ^2.1.0
Resolved version     2.1.2 in the release lockfile
Dependency path      order-service → receipt-export → example-renderer
````

The manifest permits a fixed version, but the release lockfile selects an affected one. Assign the update to the receipt-export maintainer. After the change, inspect the resolved dependency in the release build, rerun the scan, and generate a synthetic receipt to check that export still works.

## Practice and self-check

Suppose the follow-up scan shows zero alerts, but its log says it scanned a development manifest. Can you close the release finding? Keep it open until the release dependency has been checked. Record the build identifier, resolved version, scan scope, regression result and owner so a reviewer can repeat the decision.
