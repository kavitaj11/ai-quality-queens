# Playwright MCP Server

[07 · Module topics](../../modules/07-playwright.md) · [Curriculum index](../../README.md)

**Category** · SCALE & STRUCTURE

Playwright MCP is an official Model Context Protocol server that gives an AI agent a real, controllable browser. Instead of guessing from pixels, the agent works off the accessibility tree — fast, deterministic, and token-light. You add it to any MCP client (Claude, Cursor, VS Code) and the agent gets tools like navigate, click, type, snapshot, and screenshot. QA use: exploratory smoke-testing, reproducing a bug from a description, and drafting a real Playwright spec you then review and commit.

## Worked reference

````text
PLAYWRIGHT MCP — an AI agent driving a real browser

   ┌──────────────┐   MCP (tools)   ┌──────────────────┐
   │  AI CLIENT   │ ◀────────────▶  │ Playwright MCP    │
   │ Claude /     │                 │ SERVER            │
   │ Cursor /     │   the agent     │  (@playwright/mcp)│
   │ VS Code      │   calls tools:  └────────┬──────────┘
   └──────────────┘   navigate               │ drives
                      click, type            ▼
                      snapshot        ┌──────────────┐
                      screenshot      │  REAL BROWSER │
                                      └──────────────┘

   KEY IDEA: the agent acts on the ACCESSIBILITY TREE
   (structured roles/names), not screenshots →
   fast, deterministic, and cheap on tokens.

TOOLS THE AGENT GETS (subset)
   browser_navigate      browser_click      browser_type
   browser_snapshot      browser_take_screenshot
   browser_select_option browser_file_upload  browser_wait_for
   browser_network_requests   browser_console_messages
````

## Exercise

Add Playwright MCP to an AI assistant and ask it to smoke-test a staging login flow, then have it emit a reviewed .spec.ts you commit.
