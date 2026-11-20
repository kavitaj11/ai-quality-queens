# Designing MCP servers for QA

[15 · Module topics](../../modules/15-autonomous-qa-pipelines.md) · [Curriculum index](../../README.md)

**Category** · MCP INFRASTRUCTURE & GUARDRAILS

MCP servers are the tool layer agents stand on — and tool DESIGN is what makes agents reliable. Wrap your QA stack (run suites, seed data, fetch reports, manage envs) as small, typed, idempotent tools with structured errors. Good tools constrain agents into safe, effective action; vague tools ('run_command') invite creative disaster.

## Worked reference

````text
MCP SERVER DESIGN — tools that make agents reliable

  A QA MCP SURFACE (typical)
  READ-ONLY                MUTATING (gated)
  list_suites()            run_suite(id, env, shard?)
  get_run(id)              seed_data(fixture, env)
  get_failure_history(t)   create_env(ttl) → env_id
  get_coverage(path)       destroy_env(env_id)
  get_heal_report(run)     quarantine(test_id, reason,
                             expiry)  — note: expiry
                             REQUIRED by schema

  DESIGN RULES (same as good API design, stricter)
  • SMALL + TYPED: strict JSON schemas; enums over
    free strings; no 'run_command(str)' escape hatch
  • IDEMPOTENT where possible: re-seeding the same
    fixture is a no-op, not a duplicate
  • STRUCTURED ERRORS: {error, retryable, hint} —
    'env not warm, retry in 30s' lets the agent
    recover; a stack trace makes it hallucinate
  • PAGINATE + SUMMARIZE returns: a 50MB report
    blows the context window; return aggregates +
    a fetch-details tool
  • tool DESCRIPTIONS are prompts — write them like
    docs for a junior engineer, with when-to-use

  TEST THE SERVER LIKE ANY API (it is one)
  contract tests per tool · schema-fuzz the inputs ·
  verify error paths return structured errors ·
  load-test list endpoints agents will hammer

  COMPOSITION
  playwright-mcp (browser) + qa-tools (your stack) +
  reporting-mcp (dashboards) — agents compose them;
  each server stays single-purpose and testable
````

## Exercise

Spec a qa-tools MCP server: 6 tools, their JSON schemas, which are read-only vs mutating, and what each returns on failure.
