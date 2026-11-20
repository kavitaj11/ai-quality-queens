# Python–Playwright framework architecture

[13 · Module topics](../../modules/13-test-automation-system-design.md) · [Curriculum index](../../README.md)

**Category** · FRAMEWORK BLUEPRINTS

The modern lean stack: pytest as the engine (fixtures ARE the dependency injection), playwright's built-in auto-waits and contexts, conftest.py layering for scoped setup, pydantic-typed config, storage-state auth reuse, and parallelism via pytest-xdist. Same layered principles as Java, dramatically less scaffolding — fixtures replace the factory classes.

## Worked reference

````text
# conftest.py — the whole framework core in ~25 lines
import pytest
from playwright.sync_api import sync_playwright
from config import Settings

@pytest.fixture(scope="session")
def settings():
    return Settings()          # pydantic: typed + validated

@pytest.fixture(scope="session")
def browser(settings):
    with sync_playwright() as p:
        b = p.chromium.launch(headless=settings.headless)
        yield b
        b.close()

@pytest.fixture(scope="session")
def auth_state(browser, settings, tmp_path_factory):
    """Log in ONCE per run; every test reuses the state."""
    path = tmp_path_factory.mktemp("auth") / "state.json"
    ctx = browser.new_context(base_url=settings.base_url)
    page = ctx.new_page()
    page.goto("/login")
    page.get_by_label("Email").fill(settings.user)
    page.get_by_label("Password").fill(settings.password)
    page.get_by_role("button", name="Sign in").click()
    ctx.storage_state(path=str(path))
    ctx.close()
    return str(path)

@pytest.fixture
def page(browser, settings, auth_state):
    ctx = browser.new_context(base_url=settings.base_url,
                              storage_state=auth_state)
    yield ctx.new_page()
    ctx.close()                # fresh context per test
````

## Exercise

Design conftest.py layering for a suite that needs: one browser per session, fresh context per test, pre-authenticated state for 90% of tests, and a logged-out context for auth tests.
