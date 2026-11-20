# Auto-waiting & web-first assertions

[07 · Module topics](../../modules/07-playwright.md) · [Curriculum index](../../README.md)

**Category** · PLAYWRIGHT CORE

Every action waits for the element to be actionable; every expect() polls until it passes or times out. This is why Playwright suites need almost no explicit waits — and why porting Selenium habits over is a mistake.

## Worked reference

````text
# No waits needed — these retry automatically
expect(page.get_by_test_id("cart-count")).to_have_text("3")
expect(page.get_by_role("alert")).to_contain_text("Saved")
expect(page).to_have_url(re.compile(r".*checkout"))

# Anti-pattern from Selenium habits:
# page.wait_for_timeout(2000)  ← never do this
````

## Exercise

Write a test against a page with a 2s-delayed element using zero waits or timeouts. It should just pass.
