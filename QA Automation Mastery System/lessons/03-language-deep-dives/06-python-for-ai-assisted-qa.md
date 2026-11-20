# Python for AI-assisted QA

[03 · Module topics](../../modules/03-language-deep-dives.md) · [Curriculum index](../../README.md)

**Category** · PYTHON DEEP DIVE

Python is the native language of AI tooling — which is why it's the fastest stack for LLM-driven testing. Call a model to generate or triage tests, get structured JSON back, validate it with pydantic, and expose your suite to agents over MCP. The discipline is the same as always: the model drafts, you review; parse and schema-check every response before trusting it.

## Worked reference

````text
import json, anthropic
from pydantic import BaseModel, ValidationError

client = anthropic.Anthropic()   # reads ANTHROPIC_API_KEY

# 1. STRUCTURED OUTPUT — force JSON, then validate it
class Triage(BaseModel):
    category: str          # 'app-bug' | 'test-bug' | 'flake' | 'env'
    likely_cause: str
    suggested_fix: str
    confidence: float

def triage_failure(test_name: str, stack: str) -> Triage:
    msg = client.messages.create(
        model="claude-sonnet-4-5", max_tokens=500,
        messages=[{"role": "user", "content":
            f"Classify this test failure. Reply ONLY JSON with "
            f"keys category, likely_cause, suggested_fix, "
            f"confidence.\n{test_name}\n{stack}"}])
    # NEVER trust raw text — parse + schema-check
    return Triage.model_validate_json(msg.content[0].text)

t = triage_failure("test_checkout", tb)
if t.category == "flake" and t.confidence > 0.8:
    quarantine("test_checkout")

# 2. GENERATE tests from a spec (draft -> you review as a PR)
def gen_tests(openapi_snippet: str) -> str:
    return client.messages.create(
        model="claude-sonnet-4-5", max_tokens=2000,
        messages=[{"role": "user", "content":
            "Write pytest tests, happy path + 1 boundary each:\n"
            + openapi_snippet}]).content[0].text

# 3. EXPOSE your suite to agents over MCP (FastMCP)
from mcp.server.fastmcp import FastMCP
mcp = FastMCP("qa-tools")

@mcp.tool()
def run_suite(marker: str) -> str:
    """Run pytest filtered by marker; return the report."""
    import subprocess
    return subprocess.run(["pytest", "-m", marker],
                          capture_output=True, text=True).stdout
# mcp.run()  -> agents can now run + triage your suite
````

## Exercise

Write a helper that sends a failing test's stack trace to an LLM and returns a structured triage (category, likely cause, suggested fix) validated by a pydantic model.
