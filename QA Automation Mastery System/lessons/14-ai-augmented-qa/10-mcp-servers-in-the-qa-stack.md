# MCP servers in the QA stack

[14 · Module topics](../../modules/14-ai-augmented-qa.md) · [Curriculum index](../../README.md)

**Category** · AGENTIC QA, FLAKE AI & MCP

MCP is the plug between AI agents and your tools. Playwright MCP gives any agent a real browser; a custom MCP server exposes YOUR suite — run tests, seed data, fetch reports — so agents work inside your stack, not around it.

## Worked reference

````text
# Custom MCP server: expose YOUR stack to agents
import subprocess
from mcp.server.fastmcp import FastMCP

mcp = FastMCP("qa-tools")

@mcp.tool()
def run_suite(marker: str) -> str:
    """Run the pytest suite filtered by marker."""
    return subprocess.run(
        ["pytest", "-m", marker, "--json-report"],
        capture_output=True, text=True).stdout

@mcp.tool()
def seed_test_data(kind: str) -> dict:
    """Create a fresh user/order from the factory."""
    return factory.create(kind)

mcp.run()   # agents can now seed data + run suites
````

## Exercise

Connect Playwright MCP to an AI assistant and ask it to smoke-test a staging flow. Then list 3 tools a custom qa-tools MCP server should expose for your team.
