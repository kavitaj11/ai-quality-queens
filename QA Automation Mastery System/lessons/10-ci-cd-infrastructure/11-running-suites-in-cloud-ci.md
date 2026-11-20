# Running suites in cloud CI

[10 · Module topics](../../modules/10-ci-cd-infrastructure.md) · [Curriculum index](../../README.md)

**Category** · CLOUD TESTING PLATFORMS

Where the suite itself executes: CI runners (GitHub Actions, GitLab, CodeBuild) spin containers on demand — your suite runs in a clean image, in parallel shards, against an ephemeral env or a device cloud. Cloud-native patterns: containerized browsers for consistency, sharding by test file, artifacts (traces, videos, HARs) shipped to storage, and spot instances for cheap nightly loads.

## Worked reference

````text
RUNNING SUITES IN CLOUD CI — the execution topology

  push/PR ──▶ CI (Actions/GitLab/CodeBuild)
               │ clean container per job —
               │ no 'works on the old runner' drift
         ┌─────┼─────┬─────┐
       shard 1 shard 2 ... shard N   (test files split;
         │       │           │        Playwright --shard,
         └───────┴─────┬─────┘        pytest-split)
                       ▼
          browsers: in-container (fast, PR gate)
          or device cloud endpoint (matrix, nightly)
                       ▼
          artifacts → S3/storage: traces, videos,
          HARs, JUnit XML → dashboards

  PATTERNS THAT PAY OFF
  • containerized browser images pinned by version —
    same Chrome everywhere, forever
  • shard by FILE, balance by duration history
  • retry-with-trace only on failure (cheap triage)
  • spot/preemptible instances for nightly full
    matrix — 60-90% cheaper, retries absorb evictions
  • cache deps (npm/pip/maven) or pay minutes for
    downloads every run

  COST = MINUTES × PARALLELISM
  the 8-minute gate with 5 shards costs the same
  minutes as 40 sequential — you buy SPEED, not
  savings. Savings come from tiering (smoke on PR,
  full nightly) and killing redundant runs.
````

## Exercise

Design the CI topology for a 40-minute suite that must finish in 8: how many shards, what runs where (local browsers vs device cloud), and where artifacts land.
