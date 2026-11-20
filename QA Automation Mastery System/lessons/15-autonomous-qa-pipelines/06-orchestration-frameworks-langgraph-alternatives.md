# Orchestration frameworks: LangGraph & alternatives

[15 · Module topics](../../modules/15-autonomous-qa-pipelines.md) · [Curriculum index](../../README.md)

**Category** · AGENT ORCHESTRATION

How the planner–executor design actually gets wired. LangGraph models the pipeline as a state graph — nodes are agents/tools, edges are routing decisions, state is a shared typed object — with checkpointing, retries, and human-approval interrupts built in. Alternatives: CrewAI (role-based crews, fastest start), AutoGen (conversation-driven), OpenAI Agents SDK / Claude Agent SDK (thin, provider-native handoffs), Temporal + plain code (durable workflows, agents as activities). The graph/workflow shape is the point; the framework is a choice.

## Worked reference

````text
ORCHESTRATION FRAMEWORKS — wiring the agent team

  LANGGRAPH — pipeline as a STATE GRAPH
  ┌────────┐   plan    ┌──────────┐  results ┌────────┐
  │ planner├──────────▶│ executors├─────────▶│ triage │
  └────────┘           └──────────┘          └───┬────┘
       ▲      conditional edges:                 │
       │      flaky→heal · bug→report ·      ┌───▼────┐
       └──────replan if plan_recall low ─────│ router │
                                             └───┬────┘
              ┌─────────┐    interrupt():        │
              │  human  │◀── quarantine needs ───┘
              │ approval│    approval (L2 gate)
              └─────────┘
  state: typed object flowing through nodes
    {diff, plan, results[], clusters[], verdicts[]}
  built-in: CHECKPOINTING (resume a crashed run) ·
  retries · interrupts (human gate as a node) ·
  replay from any checkpoint (your audit log!)

  THE ALTERNATIVES
  CrewAI      role-based 'crews', least code to a
              working team; less control over routing
  AutoGen     agents converse to coordinate —
              flexible, harder to make deterministic
  OpenAI Agents SDK / Claude Agent SDK
              thin provider-native loops + handoffs;
              fewest moving parts, some lock-in
  Temporal /  durable workflow engine, agent calls
  plain code  as activities — max control + battle-
              tested retries; you write more code

  CHOOSING FOR A QA PIPELINE
  • need human-approval gates + resumability +
    replayable audit? → LangGraph or Temporal
  • prototyping the agent team's roles? → CrewAI
  • simple single-agent + tools loop? → provider SDK
  the invariant regardless: artifacts between nodes
  stay schema-validated (previous lesson) — the
  framework routes; it doesn't replace contracts
````

## Exercise

Sketch your QA pipeline as a LangGraph: nodes, the state object's fields, which edges are conditional, and where the human-interrupt node goes.
