# Agentic AI QA systems

[14 · Module topics](../../modules/14-ai-augmented-qa.md) · [Curriculum index](../../README.md)

**Category** · AGENTIC QA, FLAKE AI & MCP

An agent explores your app in a loop — observe (accessibility snapshot) → decide (LLM picks an action) → act → repeat — filing bugs as it goes. Agents explore; scripted tests assert. You need both.

## Worked reference

````text
GOAL = "Add any product to cart, reach checkout"

def run_agent(page, goal, max_steps=25):
    history = []
    for _ in range(max_steps):
        state = page.locator("body").aria_snapshot()
        action = llm_decide(goal, state, history)
        if action["type"] == "done":
            return history
        if action["type"] == "bug":
            file_issue(action["details"],
                       page.screenshot())
        execute(page, action)   # click / fill / goto
        history.append(action)
    raise TimeoutError("Agent exceeded step budget")

# Guardrails: step budget, allowed-domain list,
# read-only creds, never on production
````

## Exercise

Sketch the tool schema (click, fill, snapshot, report_bug) for an exploratory agent against an app you know. What guardrails does it need?
