# Fixtures & authenticated state

[07 · Module topics](../../modules/07-playwright.md) · [Curriculum index](../../README.md)

**Category** · POWER FEATURES

Log in once in a setup project, save storageState, and every test starts authenticated. Custom fixtures replace base classes and setUp/tearDown entirely.

## Worked reference

````text
# conftest.py
import pytest

@pytest.fixture(scope="session")
def auth_state(browser, tmp_path_factory):
    path = tmp_path_factory.mktemp("auth") / "user.json"
    page = browser.new_page()
    page.goto("/login")
    page.get_by_label("Email").fill("qa@dev.io")
    page.get_by_role("button", name="Log in").click()
    page.context.storage_state(path=str(path))
    return str(path)

@pytest.fixture
def page(browser, auth_state):
    ctx = browser.new_context(storage_state=auth_state)
    yield ctx.new_page()
    ctx.close()
````

## Exercise

Add a global setup that signs in and saves state; make your whole suite skip the login screen.
