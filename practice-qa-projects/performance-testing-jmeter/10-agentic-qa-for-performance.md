# 10. Agentic QA for Performance

An **agent** doesn't just answer a question: it takes actions in a loop until it reaches a goal.

```
          ┌────────────────────────────────────────────┐
          ▼                                            │
   PLAN next step ──► ACT (run test) ──► OBSERVE (results, server metrics) ──► DECIDE
                                                                      │
                                         continue ◄───────────────────┤
                                         stop: SLO broke (knee) ◄─────┤
                                         stop: guardrail hit ◄────────┘
```

For performance testing, the obvious job for an agent is the repetitive step test from Chapter 7: run a load, look at the results, decide whether to go higher, and write up the findings.

## Run the agent

```bash
python demo-app/shop_api.py                      # terminal 1
python scripts/perf_agent.py --start-users 25 --step-duration 60 --p95-ms 800 --max-error-pct 1
```

It asks before every step (`Run step with 50 users for 60s? [y/N]`). Add `--yes` once you trust it.

This is the real output from running it against the demo app:

```
=== PLAN: step 1 at 25 users ===
=== OBSERVE: 23.15 req/s, p95 83 ms, server pool waiting max 1 ===
=== DECIDE: continue (SLO met and throughput still scaling with load) ===
=== PLAN: step 2 at 50 users ===
=== OBSERVE: 46.13 req/s, p95 84 ms, server pool waiting max 5 ===
=== DECIDE: continue (SLO met and throughput still scaling with load) ===
=== PLAN: step 3 at 100 users ===
=== OBSERVE: 89.11 req/s, p95 110 ms, server pool waiting max 20 ===
=== DECIDE: continue (SLO met and throughput still scaling with load) ===
=== PLAN: step 4 at 200 users ===
=== OBSERVE: 118.64 req/s, p95 790 ms, server pool waiting max 101 ===
=== DECIDE: stop-knee (SLO broke at 200 users: 05 Checkout: p95 836 ms > 800.0 ms) ===
```

It writes `agent_report.md` with the step table, the verdict ("highest load that met the SLO: **100 users**") and its decision log. That is the table you saw in Chapter 7.

## How the agent decides

Open `scripts/perf_agent.py`, function `decide()`. The rules are plain and readable:

1. If the SLO failed → **stop**: the previous step is the capacity.
2. If users grew 1.5× or more but throughput grew less than half as much → **stop**: the system is saturating even though the SLO still holds.
3. If the step limit or the user cap would be exceeded → **stop**: guardrail.
4. Otherwise → **continue** with users × 2.

The decision logic is deliberately **deterministic**: same results, same decision, no API key, easy to review. An LLM is used only where language helps: with `--ai`, Claude writes a narrative diagnosis from the evidence the agent gathered (Chapter 9). The verdict never depends on the model. That split is a good default for any QA agent: **rules decide pass/fail, AI explains.**

## Guardrails: what makes an agent safe to run

An agent that generates load can do real damage. `perf_agent.py` shows the minimum set of guardrails:

| Guardrail | In the code |
|---|---|
| **Allowed targets only** | Refuses any host except `127.0.0.1` / `localhost` unless you pass `--allow-host` |
| **Hard limits** | `--max-users` (default 400) and `--max-steps` (default 6) |
| **Human approval** | Asks before every step unless `--yes` |
| **Stop conditions** | Stops on SLO breach or saturation; never "keeps trying" |
| **Audit trail** | Writes every decision and its reason to `agent_report.md` |
| **Clean measurement** | Resets server metrics before each step so steps don't blur together |

When you design or review any QA agent, ask: what can it touch, how much can it do, who approves, when does it stop, and how will a person review what it did?

## From scripts to agent platforms

This agent is one Python file so you can read all of it. In practice, the same loop is built with:

- **An LLM with tools** (tool use / function calling): the model chooses which tool to call (run test, read metrics, write report) and your code executes it, with the same guardrails enforced in code, not in the prompt.
- **MCP (Model Context Protocol) servers** that expose testing tools to AI assistants such as Claude Code. For example, [k11techlab-performance-qa-mcp](https://github.com/K11-Software-Solutions/k11techlab-performance-qa-mcp) turns API flows into load scenarios with per-endpoint SLOs and regression checks.
- **Multi-agent pipelines** where one agent plans, one runs, one analyzes and one gates the release, as in [k11techlab-agentic-ai-qa-system](https://github.com/K11-Software-Solutions/k11techlab-agentic-ai-qa-system) and [k11techlab-release-gate-mcp](https://github.com/K11-Software-Solutions/k11techlab-release-gate-mcp).

The principles don't change with the platform: deterministic gates, human approval for risky actions, hard limits and an audit trail.

## Ideas to extend the agent (exercises)

1. Add a **soak step**: after finding capacity, run 80% of it for 30 minutes and check that p95 doesn't drift upward.
2. Make step sizes **adaptive**: grow by 2× while far from the SLO, by 1.25× when close.
3. Add a **load generator check**: stop if the JMeter machine's CPU passes 80%.
4. Re-run the agent after starting the app with `--pool-size 16 --backlog 128` and compare the reports.

Next: [11. Exercises and review questions](11-exercises-and-review-questions.md)
