# AI test generation

[14 · Module topics](../../modules/14-ai-augmented-qa.md) · [Curriculum index](../../README.md)

**Category** · AGENTIC QA, FLAKE AI & MCP

LLMs draft tests from specs, recordings, and prose — you review them like any PR. Generation is cheap; the discipline is in review, quarantine, and keeping locators semantic.

## Worked reference

````text
# Generate API tests from your OpenAPI spec
import anthropic, yaml

spec = yaml.safe_load(open("openapi.yaml"))
client = anthropic.Anthropic()

msg = client.messages.create(
    model="claude-sonnet-4-5",
    max_tokens=2000,
    messages=[{"role": "user", "content":
        "Write pytest tests for every endpoint. "
        "Happy path + one boundary case each:\n"
        + yaml.dump(spec["paths"])}])

open("tests/test_generated.py", "w") \
    .write(msg.content[0].text)
# Rule: generated tests are a DRAFT — review like a PR
````

## Exercise

Generate tests for one API endpoint from its OpenAPI spec using an LLM. Review, fix, and count what you had to change.
