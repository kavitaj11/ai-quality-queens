# Self-healing locators

[14 · Module topics](../../modules/14-ai-augmented-qa.md) · [Curriculum index](../../README.md)

**Category** · AGENTIC QA, FLAKE AI & MCP

When the DOM changes, healing finds the closest surviving element instead of failing — Healenium in Java, ranked fallback chains anywhere else. Healing is a bridge, not a fix: review heal reports and repair the real locator.

## Worked reference

````text
def find_with_fallbacks(page, candidates):
    for sel in candidates:
        loc = page.locator(sel)
        if loc.count() == 1:
            if sel != candidates[0]:
                log.warning("HEAL: fell back to %s", sel)
            return loc
    # Last resort: ask an LLM to repair from live DOM
    html = page.content()[:8000]
    fixed = llm("Return ONE CSS selector for the "
                f"submit button in this HTML:\n{html}")
    return page.locator(fixed.strip())

submit = find_with_fallbacks(page, [
    "[data-testid='submit']",
    "button[type=submit]",
])
````

## Exercise

Build a fallback-chain finder with 3 ranked selectors. Rename the element's id and confirm the test still passes — and logs which fallback fired.
