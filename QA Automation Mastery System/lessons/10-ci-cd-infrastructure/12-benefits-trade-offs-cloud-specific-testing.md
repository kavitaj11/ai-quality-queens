# Benefits, trade-offs & cloud-specific testing

[10 · Module topics](../../modules/10-ci-cd-infrastructure.md) · [Curriculum index](../../README.md)

**Category** · CLOUD TESTING PLATFORMS

The honest scorecard. Benefits: elastic scale (500 parallel sessions on demand), the real device/browser matrix, no lab maintenance, prod-shaped envs per PR. Trade-offs: recurring cost, tunnel/network complexity, data leaving your network (compliance), vendor lock-in, and new flake sources. Plus what only exists in the cloud: IAM/permission testing, region failover, quotas and throttling.

## Worked reference

````text
CLOUD TESTING — the honest scorecard

BENEFITS
  scale       500 parallel sessions for an hour,
              then zero — impossible on-prem
  coverage    real Safari-on-iOS, old Androids,
              every Chrome back-version
  zero lab    no device drawer, no OS updates,
              no dead batteries
  parity      prod-shaped ephemeral envs per PR
  evidence    videos/logs/HARs attached to every run

TRADE-OFFS
  cost        per-minute meters + parallel-session
              tiers; idle sessions still bill
  network     tunnels to private staging = new
              moving part, new flake source
  data        test data leaves your network —
              compliance/PII review required;
              anonymize seeds
  lock-in     capability payloads + platform
              features creep into the suite;
              keep the endpoint swappable
  latency     remote browsers are slower per
              command than local containers

ONLY-IN-THE-CLOUD TEST CONCERNS
  IAM         least-privilege actually enforced?
              (the test that tries the FORBIDDEN
              action is the valuable one)
  quotas      API rate limits + service quotas —
              load tests hit them before users do
  regions     failover drills · data residency ·
              latency per region
  config      security groups, env vars, secrets —
              most cloud outages are CONFIG bugs;
              test config changes like code changes

RULE OF THUMB
  cloud for the MATRIX and SCALE; local/containers
  for the fast inner loop. Hybrid is the norm.
````

## Exercise

Write the one-page business case for adopting a device cloud at your org: costs, what it replaces, compliance answers, and the 3 new failure modes you'll monitor.
