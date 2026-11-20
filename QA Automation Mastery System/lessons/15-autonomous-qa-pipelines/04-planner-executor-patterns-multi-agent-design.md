# Planner–executor patterns & multi-agent design

[15 · Module topics](../../modules/15-autonomous-qa-pipelines.md) · [Curriculum index](../../README.md)

**Category** · AGENT ORCHESTRATION

One giant agent doing everything is unreliable and undebuggable. Split by role: a planner decomposes goals into bounded tasks; executors do one task each with only the tools that task needs; a critic/verifier checks outputs before they count. Handoffs happen through structured artifacts (JSON plans, reports), not chat — so every step is inspectable, replayable, and testable on its own.

## Worked reference

````text
MULTI-AGENT DESIGN — roles, not one giant agent

  PLANNER    goal → bounded TASK LIST (JSON)
             sees: diff, coverage map, history
             tools: read-only
  EXECUTORS  one task each — run suite · explore
             surface · generate tests for gap
             tools: ONLY what the task needs
  CRITIC     checks executor output BEFORE it
             counts: does the repro actually
             reproduce? does the healed locator
             match the intended element?
  REPORTER   aggregates → check run + tickets

  WHY SPLIT
  • small agents = small prompts = fewer failure
    modes; each role testable in isolation
  • least-privilege tools PER ROLE (executor that
    only reads can't break staging)
  • a critic catches the classic agent failure:
    confident completion of the wrong task

  HANDOFFS = ARTIFACTS, NOT CHAT
  plan.json → task → result.json → verdict.json
  • schema-validate EVERY artifact (an agent that
    emits invalid JSON fails fast, visibly)
  • artifacts are replayable: re-run an executor
    from its input without re-running the planner

  FAILURE CONTAINMENT
  task timeout + step budget per agent · retries
  with backoff · circuit breaker: N failures →
  degrade to L1 (assist mode) and page a human —
  the pipeline falls back to scripted, never stalls
````

## Exercise

Design the agent team for 'regression-test this PR': planner, N executors, critic. Define each one's input artifact, output artifact, and tool allow-list.
