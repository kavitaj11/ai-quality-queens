# Config, projects & environments

[07 · Module topics](../../modules/07-playwright.md) · [Curriculum index](../../README.md)

**Category** · TEST DESIGN

playwright.config is where suite design lives: baseURL per environment (so tests use relative paths), projects for browser/device/role matrices, dependencies between projects (setup runs first), timeout tiers, and artifact policy (trace/screenshot/video on failure). One config, many environments — switch with an env var, never by editing tests.

## Worked reference

````text
# Python: config lives in pytest.ini/pyproject + conftest.py

# ── pyproject.toml (or pytest.ini) ──
# [tool.pytest.ini_options]
# addopts = "--tracing retain-on-failure --screenshot only-on-failure"
# markers = ["smoke", "regression"]

# ── conftest.py: env-driven baseURL + context options ──
import os, pytest

ENV = os.environ.get("TEST_ENV", "staging")
BASE = {"staging": "https://staging.shop.dev",
        "prod":    "https://shop.dev"}[ENV]

@pytest.fixture(scope="session")
def base_url():                      # pytest-playwright uses this
    return BASE                      # page.goto("/cart") works

# context options (viewport, locale, storage state)
@pytest.fixture(scope="session")
def browser_context_args(browser_context_args):
    return {**browser_context_args,
            "viewport": {"width": 1440, "height": 900},
            "locale": "en-US",
            "storage_state": ".auth/user.json"}

# ── "projects" equivalent: CLI flags + markers ──
#   pytest --browser chromium --browser webkit   (matrix)
#   pytest --device "iPhone 15"                  (device profile)
#   TEST_ENV=prod pytest -m smoke -n 4           (env + tags + parallel)

# ── login-once setup: a session fixture writes .auth/user.json ──
@pytest.fixture(scope="session", autouse=True)
def auth_state(browser):
    ctx = browser.new_context(base_url=BASE)
    ctx.request.post("/api/login", data=USER)
    ctx.storage_state(path=".auth/user.json")
    ctx.close()
````

## Exercise

Write a config with staging/prod baseURLs picked by env var, a setup project that logs in, and chromium+webkit projects that depend on it.
