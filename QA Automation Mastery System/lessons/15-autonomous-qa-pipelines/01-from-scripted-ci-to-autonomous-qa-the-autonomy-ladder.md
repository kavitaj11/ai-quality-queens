# From scripted CI to autonomous QA: the autonomy ladder

[15 · Module topics](../../modules/15-autonomous-qa-pipelines.md) · [Curriculum index](../../README.md)

**Category** · PIPELINE ARCHITECTURE

Autonomy arrives in levels, not a leap. L0: scripted suites in CI. L1: AI assists (generation, triage) with humans applying. L2: agents act on failures — heal, quarantine, re-run — humans review after. L3: agents plan what to test from diffs and risk, execute, and file results; humans set policy and audit. Each level trades human effort for verification burden — you graduate a capability only when you can measure it's trustworthy.

## Worked reference

````text
THE AUTONOMY LADDER — QA pipelines by level

  L0  SCRIPTED      suites run on triggers; humans
                    write, fix, triage everything
  L1  ASSISTED      AI drafts tests + failure
                    summaries; HUMAN APPLIES all
                    changes (most teams today)
  L2  SUPERVISED    agents ACT on failures: retry,
      AUTONOMY      cluster, heal locators, quarantine
                    flakes, open PRs — humans review
                    the agent's PRs after the fact
  L3  DIRECTED      agents PLAN from code diffs +
      AUTONOMY      risk signals: choose what to test,
                    generate/execute, file triaged
                    bugs; humans set policy + audit

  WHAT CHANGES PER LEVEL
              effort↓     new burden↑
  L1          authoring   reviewing drafts
  L2          triage      auditing actions (logs!)
  L3          planning    verifying coverage claims

  GRADUATION RULE — promote one capability at a time
  measure at level N before enabling N+1:
    heal accept-rate > 90%?  → let agent open PRs
    triage agreement > 85%?  → let agent quarantine
  no metric = no promotion. Autonomy without
  measurement is just hoping at scale.

  THE INVARIANT AT EVERY LEVEL
  deterministic gates (build, unit, contract tests)
  stay deterministic — agents feed them, never
  replace them. The merge gate must not hallucinate.
````

## Exercise

Place your current pipeline on the ladder. For the next level up, name the ONE capability you'd automate first and the metric that proves it's safe to.
