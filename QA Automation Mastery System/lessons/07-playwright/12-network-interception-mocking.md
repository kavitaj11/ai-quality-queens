# Network interception & mocking

[07 · Module topics](../../modules/07-playwright.md) · [Curriculum index](../../README.md)

**Category** · POWER FEATURES

page.route() lets you stub APIs, simulate errors, and cut flaky backends out of UI tests entirely. This is Playwright's superpower over vanilla Selenium.

## Worked reference

````text
def handle(route):
    route.fulfill(status=500)

page.route("**/api/checkout", handle)
page.goto("/cart")
page.get_by_role("button", name="Pay").click()
expect(page.get_by_role("alert"))\
    .to_contain_text("Something went wrong")
````

## Exercise

Write a test that mocks the products API to return an empty list and asserts the empty state UI renders.
