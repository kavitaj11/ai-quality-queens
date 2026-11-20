# First test & the runner

[07 · Module topics](../../modules/07-playwright.md) · [Curriculum index](../../README.md)

**Category** · PLAYWRIGHT CORE

Playwright ships its own runner (JS/TS) or plugs into pytest (Python) and JUnit (Java). Browsers are managed for you; headed/headless, screenshots, and video are config flags, not plumbing.

## Worked reference

````text
from playwright.sync_api import Page, expect

def test_login(page: Page):
    page.goto("/login")
    page.get_by_label("Email").fill("qa@dev.io")
    page.get_by_label("Password").fill("s3cret")
    page.get_by_role("button", name="Log in").click()
    expect(page.get_by_role("heading", name="Dashboard"))\
        .to_be_visible()
````

## Exercise

Scaffold a project with npm init playwright@latest and get the example suite green in all 3 browsers.
