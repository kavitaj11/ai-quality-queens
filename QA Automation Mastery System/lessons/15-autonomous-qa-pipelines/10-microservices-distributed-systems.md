# Microservices & distributed systems

[15 · Module topics](../../modules/15-autonomous-qa-pipelines.md) · [Curriculum index](../../README.md)

**Category** · AUTONOMOUS QA CHALLENGES

Autonomous QA gets hardest where the system is most distributed. One user-facing failure fans out into dozens of service-level symptoms; the triage agent must correlate across services via distributed traces, respect contract boundaries (which service broke the contract?), route findings to the right OWNING team, and orchestrate multi-service ephemeral envs without booting the whole company. The fixes: traces as agent context, contract tests as the localization tool, and a service-ownership map the agents can query.

## Worked reference

````text
AUTONOMOUS QA × DISTRIBUTED SYSTEMS — why it's harder

  THE FAN-OUT PROBLEM
  checkout fails (1 user symptom)
    ├─ gateway: 504s          ├─ orders: timeouts
    ├─ payments: circuit open └─ inventory: fine
  40 red tests, 4 services, ONE root cause —
  naive per-test triage files 40 bugs on 4 teams

  WHAT THE TRIAGE AGENT NEEDS (context is everything)
  • DISTRIBUTED TRACES (OpenTelemetry): the trace id
    ties symptoms into one causal chain — give agents
    a get_trace(request_id) tool, not just logs
  • DEPLOY TIMELINE: which service shipped in the
    failure window (usually the answer)
  • CONTRACT RESULTS: consumer-driven contracts
    (Pact) localize blame — provider broke the
    contract vs consumer assumed too much
  • OWNERSHIP MAP: service → team → escalation path,
    queryable (a tool), so ONE deduped issue lands
    on the RIGHT board

  ENVIRONMENT ORCHESTRATION AT SCALE
  full-stack ephemeral env per PR stops scaling past
  ~10 services. Agents plan the MINIMAL env:
    changed service REAL + direct deps REAL +
    everything else VIRTUALIZED (contract stubs)
  planner reads the dependency graph to pick which —
  that decision is itself eval-able (plan recall)

  ASYNC MAKES 'GREEN' AMBIGUOUS
  queues/eventual consistency → a check can pass
  before the event lands. Agents need poll-with-
  deadline assertion tools ('order appears in ledger
  within 30s'), not instant asserts — or the healing
  agent 'fixes' tests by adding sleeps (audit for it)

  FLAKE ATTRIBUTION SHARPENS
  infra flake (pod eviction, DNS, cold start) vs
  product bug vs DEPENDENCY's bug — memory records
  (detect-fix-learn) must tag WHICH SERVICE'S history
  a precedent belongs to, or learning cross-
  contaminates between services

  RULE
  the agent pipeline mirrors the org: contracts at
  boundaries, traces for causality, one finding per
  root cause, routed to the owner — same discipline
  as Microservices Testing (phase 12), executed by
  agents.
````

## Exercise

Take one real cross-service incident you know: list the symptoms each service showed, and design what context (traces, contracts, deploy history) an agent would need to name the culprit service in one shot.
