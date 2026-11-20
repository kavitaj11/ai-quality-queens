# Ephemeral test environments & IaC

[10 · Module topics](../../modules/10-ci-cd-infrastructure.md) · [Curriculum index](../../README.md)

**Category** · CLOUD ENVIRONMENTS

The cloud's killer feature for QA: environments as code (Terraform, CloudFormation, CDK) mean a fresh, production-shaped environment per PR — spun up for the test run, destroyed after. No more shared-staging collisions, stale data, or 'who broke the env'. The trade: env creation is now code you must test, and cost discipline is a QA concern.

## Worked reference

````text
EPHEMERAL ENVIRONMENTS — a fresh env per PR

  PR opened ──▶ IaC apply (terraform/CDK)
                  app + DB + queues, prod-shaped
            ──▶ seed test data (factories/fixtures)
            ──▶ run the suite against it
            ──▶ TEAR DOWN (always — even on failure)
  merge/close ─▶ env gone; next PR gets a clean one

WHY IT BEATS SHARED STAGING
  • no test-data collisions between teams/runs
  • no drift — env is rebuilt from code every time
  • parallel PRs = parallel envs, not a queue
  • the env definition is REVIEWED like code

WHAT IT COSTS YOU
  • spin-up time (minutes) — keep a warm pool or
    slim the env for PR runs, full-shape nightly
  • cloud spend: orphaned envs are the #1 leak —
    tag everything, TTL auto-reap, budget alerts
  • IaC itself needs tests (does apply converge?
    does teardown leave nothing behind?)

DATA SEEDING PATTERNS
  empty + factories (fastest, most isolated) ·
  anonymized prod snapshot (realistic, heavier) ·
  golden dataset restore (repeatable perf baselines)

QA OWNS
  env parity checklist · seed strategy · teardown
  verification · 'env not warm yet' vs real failures
  (tag infra flakes separately — see Networking).
````

## Exercise

Sketch the lifecycle of a PR environment for your app: what gets created, how test data is seeded, what the suite runs against, and what guarantees teardown happens.
