# MCP servers in the QA stack

[01 · Module topics](../../modules/01-foundations.md) · [Curriculum index](../../README.md)

**Category** · AI-ASSISTED QA

MCP is the plug between AI agents and your tools. Playwright MCP gives any agent a real browser; a custom MCP server exposes YOUR suite — run tests, seed data, fetch reports — so agents work inside your stack, not around it.

## Worked reference

````text
MCP SERVERS IN THE QA STACK — theory

WHAT MCP IS
  Model Context Protocol — the standard "plug" between an AI agent
  and your tools. It lets an agent call real capabilities instead
  of guessing.

TWO USES IN QA
  1. Playwright MCP — gives any agent a REAL browser, driven off
     the accessibility tree: navigate, click, type, snapshot.
     Use it to smoke-test a flow and draft a reviewed spec.
  2. A CUSTOM MCP server — exposes YOUR stack to agents:
     run_suite(marker) · seed_test_data(kind) · fetch_report()
     so agents work INSIDE your system, not around it.

WHY IT MATTERS
  agents become useful teammates: they can seed data, run the
  suite, and read results — all through governed, reviewable tools.

DISCIPLINE
  agent output (explorations, drafted specs) is still reviewed and
  committed by a human; expose only safe, scoped tools.

  → Playwright MCP setup + a FastMCP server example live in the
    AI-Augmented QA phase.
````

## Exercise

Connect Playwright MCP to an AI assistant and ask it to smoke-test a staging flow. Then list 3 tools a custom qa-tools MCP server should expose for your team.
