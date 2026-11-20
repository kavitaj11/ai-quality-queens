# Guardrails, audit & human-on-the-loop

[15 · Module topics](../../modules/15-autonomous-qa-pipelines.md) · [Curriculum index](../../README.md)

**Category** · MCP INFRASTRUCTURE & GUARDRAILS

Autonomy is earned through containment: least-privilege credentials per agent role, staging-only network paths, budgets (steps, tokens, minutes, spend), schema-enforced action gates (quarantine requires expiry; heal requires a PR), and an audit log of every decision and tool call. Humans move from in-the-loop (approve each action) to on-the-loop (monitor dashboards, own policy, handle escalations) — and the system must degrade gracefully to L1 when trust metrics dip.

## Worked reference

````text
GUARDRAILS — autonomy is earned through containment

  IDENTITY + ACCESS (per agent role, never shared)
  • own service identity — actions attributable
  • least privilege: triage agent reads reports,
    CANNOT touch envs; healer opens PRs, CANNOT
    merge them
  • staging-only network paths; prod creds don't
    exist in the agent's world

  BUDGETS (hard stops, not suggestions)
  steps/episode · tokens/day · env-minutes · $ —
  exceeded → halt + escalate, never 'keep trying'

  ACTION GATES (enforced by tool schemas, not vibes)
  quarantine → requires reason + expiry + owner
  heal       → always a PR through code review
  env create → TTL mandatory; reaper deletes at TTL
  bug filing → dedupe check first (agents love
               filing the same bug 40 times)

  AUDIT LOG — the non-negotiable
  every decision: {agent, trigger, inputs-hash,
  tool calls + args, outcome, cost} — replayable,
  queryable. 'Why did CI quarantine my test?' must
  have an answer in one query.

  HUMAN-ON-THE-LOOP (not in-the-loop)
  dashboards: accept-rates · agreement · spend ·
  false-quarantines — with alert thresholds
  AUTO-DEMOTION: metric dips below floor → that
  capability drops a level (acts → suggests) and
  pages its owner. Trust is continuously re-earned.

  INJECTION SURFACE (see AI Testing phase)
  agents read test output, DOM, tickets — all
  attacker-reachable text. Treat tool RESULTS as
  data, never instructions; red-team the pipeline
  with poisoned failure messages before an
  attacker does.
````

## Exercise

Write the guardrail policy for a healing agent: what it may touch, its budgets, what requires human approval, what's logged, and the metric threshold that auto-demotes it.
