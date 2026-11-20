# Cross-repo impact analysis with LangGraph

[15 · Module topics](../../modules/15-autonomous-qa-pipelines.md) · [Curriculum index](../../README.md)

**Category** · AUTONOMOUS QA CHALLENGES

In a many-repo org, a PR's blast radius crosses repository boundaries: a provider API change breaks consumers that live elsewhere. An impact-analysis graph solves it: fan out over dependent repos (via GitHub MCP), assess each in parallel (contracts, client usage of the changed surface), aggregate into a risk-ranked test plan, and gate low-confidence verdicts through a human interrupt. LangGraph fits because fan-out/fan-in (map-reduce over repos), shared typed state, and checkpointed long runs are exactly its shape.

## Worked reference

````text
CROSS-REPO IMPACT ANALYSIS — a LangGraph pipeline

  PR in repo A (provider)
        │
  ┌─────▼──────────┐   what changed? (GitHub MCP:
  │ DIFF ANALYZER  │   diff → changed endpoints,
  └─────┬──────────┘   schemas, events, exports)
  ┌─────▼──────────┐   who depends on it? (dep
  │ DEP RESOLVER   │   graph · contract registry ·
  └─────┬──────────┘   org-wide code search)
        │  fan-out: Send() one branch PER repo
   ┌────┼──────┬─────────┐
   ▼    ▼      ▼         ▼
  ┌────────────────────────────┐  parallel ASSESSORS
  │ repo B │ repo C │ repo D…  │  each: does it call
  │        │        │          │  the changed surface?
  └───┬────┴───┬────┴───┬──────┘  contract still met?
      └────────┼────────┘         which suites cover it?
               ▼  fan-in
  ┌────────────────────────┐  risk-ranked verdicts:
  │ AGGREGATOR             │  B: MUST TEST (contract
  │ dedupe · rank · plan   │  diff) · C: unaffected ·
  └───────────┬────────────┘  D: UNSURE (0.4 conf)
              ▼
     interrupt() — human reviews
     ONLY low-confidence repos
              ▼
  trigger targeted suites in affected repos
  (GitHub MCP: dispatch workflows / check runs)

  WHY LANGGRAPH SPECIFICALLY
  • Send() fan-out = map-reduce over N repos, N
    unknown until runtime
  • typed shared state accumulates verdicts safely
    from parallel branches
  • checkpointing: org-wide analysis is slow — resume,
    don't restart
  • interrupt() = the human gate as a first-class node

  STATE (the artifact between nodes)
  {pr, changed_surface[], dependents[],
   verdicts: {repo, risk, evidence, confidence}[],
   test_plan[]}

  GUARDRAILS THAT APPLY DOUBLY HERE
  read-only creds for analysis; dispatching suites in
  OTHER teams' repos is a gated, logged action —
  and verdict quality is eval-able: did 'unaffected'
  repos ever break? that's plan recall, per repo.
````

## Exercise

Pick a shared API in your org. Sketch the graph: what the diff-analyzer extracts, what each per-repo assessor checks, and what evidence puts a repo in 'must test' vs 'unaffected'.
