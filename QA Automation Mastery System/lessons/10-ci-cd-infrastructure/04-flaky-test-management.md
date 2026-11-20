# Flaky test management

[10 · Module topics](../../modules/10-ci-cd-infrastructure.md) · [Curriculum index](../../README.md)

**Category** · QUALITY AT SCALE

Quarantine lanes, retry budgets, and root-cause categories (race, data, environment, order-dependence). A flaky test ignored is a suite nobody trusts.

## Worked reference

````text
# Common root causes → fixes
# race condition   → explicit/auto waits, not sleeps
# shared test data → unique data per test (uuid)
# order dependence → pytest -p no:randomly reveals it
# environment      → containerize; pin versions

import uuid

def unique_email():
    return f"qa+{uuid.uuid4().hex[:8]}@test.dev"
````

## Exercise

Instrument your suite to log every retry; triage one week of retries into root-cause buckets.
