# Agentic AI QA systems

[01 · Module topics](../../modules/01-foundations.md) · [Curriculum index](../../README.md)

**Category** · AI-ASSISTED QA

An agent explores your app in a loop — observe (accessibility snapshot) → decide (LLM picks an action) → act → repeat — filing bugs as it goes. Agents explore; scripted tests assert. You need both.

## Worked reference

````text
AGENTIC AI QA SYSTEMS — theory

WHAT IT IS
  An AI agent EXPLORES your app in a loop instead of following a
  fixed script:
     observe (accessibility snapshot)
        → decide (LLM picks the next action)
           → act (click / type / navigate)
              → repeat, filing bugs as it goes.

AGENTS vs SCRIPTED TESTS
  agents EXPLORE (find the unexpected, broad coverage, non-deterministic)
  scripted tests ASSERT (precise, repeatable regression)
  → you need BOTH: agents to discover, scripts to lock in.

TOOL SCHEMA (what the agent can do)
  click · fill · navigate · snapshot(aria) · report_bug · done

GUARDRAILS (non-negotiable)
  • a step budget (max actions) to prevent runaways
  • an allowed-domain list; read-only credentials
  • NEVER against production
  • human review of every filed bug

  → The agent loop + tool-use code lives in the AI-Augmented QA phase.
````

## Exercise

Sketch the tool schema (click, fill, snapshot, report_bug) for an exploratory agent against an app you know. What guardrails does it need?
