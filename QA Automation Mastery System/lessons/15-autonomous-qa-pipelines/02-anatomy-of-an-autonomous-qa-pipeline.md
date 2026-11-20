# Anatomy of an autonomous QA pipeline

[15 · Module topics](../../modules/15-autonomous-qa-pipelines.md) · [Curriculum index](../../README.md)

**Category** · PIPELINE ARCHITECTURE

The reference architecture: event sources (PRs, deploys, prod alerts) feed a planner agent that reads the diff and risk signals; executor agents run suites and exploratory passes via MCP tools; a triage agent clusters failures and separates product bugs from infra flakes; a healing agent proposes fixes as PRs. Everything flows through an audit log, and deterministic CI gates remain the spine.

## Worked reference

````text
AUTONOMOUS QA PIPELINE — reference architecture

  EVENTS: PR opened · deploy · nightly · prod alert
     │
     ▼
  ┌─ PLANNER AGENT ─────────────────────────┐
  │ reads diff + coverage + failure history │
  │ picks: which suites, which exploratory  │
  │ passes, which envs — emits a TEST PLAN  │
  └─────────────────┬────────────────────┘
            ▼                      ▼
  ┌─ EXECUTORS ──────┐   ┌─ EXPLORATORY AGENT ─┐
  │ scripted suites  │   │ agentic loop on the │
  │ (CI shards,      │   │ changed surface via │
  │  ephemeral envs) │   │ Playwright MCP      │
  └────────┬────────┘   └─────────┬─────────┘
           └───────┬─────────────┘
                   ▼
  ┌─ TRIAGE AGENT ──────────────────────────┐
  │ cluster failures · flake vs product bug │
  │ · root-cause hypothesis · dedupe issues  │
  └──────────────────┬───────────────────┘
            ▼                      ▼
  ┌─ HEALING AGENT ───┐   ┌─ REPORTER ────────┐
  │ locator/data     │   │ check runs · bug  │
  │ fixes as PRS —   │   │ tickets · dash-   │
  │ never direct     │   │ boards · baselines│
  │ commits          │   └──────────────────┘
  └──────────────────┘
  ══ AUDIT LOG: every agent decision + tool call ══

  DESIGN RULES
  • agents COMMUNICATE through artifacts (plans,
    reports, PRs) — reviewable, replayable
  • deterministic gates stay the spine; agents
    route work INTO them
  • healing agent output is always a PR — the
    existing code-review gate is the safety net
````

## Exercise

Draw this diagram for your org: which boxes exist today, which are humans, and which single box would you agentify first?
