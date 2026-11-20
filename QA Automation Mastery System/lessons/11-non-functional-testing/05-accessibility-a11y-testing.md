# Accessibility (a11y) testing

[11 · Module topics](../../modules/11-non-functional-testing.md) · [Curriculum index](../../README.md)

**Category** · OTHER NON-FUNCTIONAL TYPES

Accessibility ensures people using screen readers, keyboards, and assistive tech can use the product; it's also a legal requirement (WCAG 2.x, ADA, EN 301 549). Automate the ~30-40% of issues tools can catch (contrast, missing labels, ARIA, landmarks) with axe-core, Lighthouse, or Pa11y, then do manual keyboard + screen-reader passes for the rest.

## Worked reference

````text
# axe-core via Playwright (Python)
from axe_playwright_python.sync_playwright import Axe

def test_home_a11y(page):
    page.goto("https://shop.dev")
    results = Axe().run(page)
    serious = [v for v in results["violations"]
               if v["impact"] in ("serious", "critical")]
    assert not serious, [v["id"] for v in serious]
````

## Exercise

Run axe on one page in CI and fail on any serious/critical violation; then keyboard-navigate the primary flow with no mouse and note gaps.
