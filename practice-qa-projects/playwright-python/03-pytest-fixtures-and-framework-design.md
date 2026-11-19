# 3. pytest Fixtures and Framework Design

## Fixtures in one minute

A fixture is a function that **provides something a test needs** and **cleans up afterwards**. A test asks for it by naming it as a parameter:

```python
def test_search_returns_matching_products(logged_in_page: Page): ...
```

```python
@pytest.fixture
def page(context) -> Page:
    return context.new_page()
```

Code after `yield` in a fixture is teardown, and it runs even when the test fails.

## Scopes

| Scope | Created | This project uses it for |
|---|---|---|
| `session` | Once per run | `playwright`, `browser`, `base_url` |
| `module` / `class` | Once per file / class | (not needed here) |
| `function` (default) | For every test | `context`, `page`, `logged_in_page`, `api` |

Expensive and safe-to-share things get wide scopes (one browser for the whole run); anything holding test state gets function scope (a fresh context per test).

## This project's `conftest.py`, piece by piece

```python
@pytest.fixture(scope="session")
def browser(playwright, pytestconfig):
    browser_type = getattr(playwright, pytestconfig.getoption("--browser-name"))
    browser = browser_type.launch(headless=not pytestconfig.getoption("--headed"))
    yield browser
    browser.close()

@pytest.fixture
def context(browser, base_url, request):
    context = browser.new_context(base_url=base_url, viewport={"width": 1280, "height": 900})
    context.tracing.start(screenshots=True, snapshots=True, sources=True)
    yield context
    # save trace.zip and a screenshot only if the test failed (see Chapter 6)
    ...
    context.close()

@pytest.fixture
def logged_in_page(context, page):
    response = context.request.post("/api/login", data={"username": "pw-user", "password": PASSWORD})
    assert response.ok
    return page
```

`base_url` on the context means tests use relative paths: `page.goto("/cart")`.

To know whether a test failed inside a fixture's teardown, a small hook records each phase's report on the test item (`pytest_runtest_makereport`), and the fixture reads `request.node.rep_call.failed`.

## The pytest-playwright plugin

In most projects you'd `pip install pytest-playwright` and get these fixtures for free:

| Plugin provides | This project's equivalent |
|---|---|
| `browser`, `context`, `page` fixtures | Same names, in `conftest.py` |
| `--browser chromium/firefox/webkit` | `--browser-name` |
| `--headed` | `--headed` |
| `--tracing retain-on-failure` | Tracing in the `context` fixture |
| `--base-url` (via pytest-base-url) | `--shop-url` |
| `browser_context_args` fixture to customise contexts | Arguments to `new_context()` |

Writing the fixtures yourself once helps explain the lifecycle and isolation behavior provided by the plugin.

## A real conflict found while writing this tutorial

The first version of `conftest.py` added an option called `--base-url`. pytest refused to start:

```
ValueError: option names {'--base-url'} already added
```

The machine already had the **pytest-base-url** plugin installed (pytest-playwright depends on it, so it's common), and that plugin owns `--base-url`. The option was renamed to `--shop-url`, and the project's own `base_url` fixture overrides the plugin's fixture of the same name. Lesson: **pytest plugins are global to the environment**. Use a virtual environment per project and check `pip list | grep pytest` when options collide.

## Page objects in Python

`pages/` holds one class per page. The rules match the Selenium tutorial: locators in `__init__`, methods named after user intent, navigation returns the next page object, and no assertions about business outcomes inside pages (the only `expect()` calls in pages confirm *which page* has loaded):

```python
product = LoginPage(page).open().login_as("e2e-user", PASSWORD).search("mug").open_product("Travel Mug")
cart = product.add_to_cart(quantity=2)
order = cart.checkout()
```

## Parametrize

```python
@pytest.mark.parametrize(
    "username, password, message",
    [("alice", "wrong-password", "Invalid username or password."),
     ("locked", PASSWORD, "This account is locked. Contact support."), ...],
    ids=["wrong-password", ..., "locked-account"],
)
def test_invalid_login_shows_error(page, username, password, message): ...
```

`ids` gives readable test names in the report: `test_invalid_login_shows_error[locked-account]`.

## Review questions

**What is a fixture? How is it different from setup/teardown methods?**
A reusable provider of test dependencies with built-in teardown (after `yield`) and a scope. Tests declare what they need by name, fixtures can depend on other fixtures, and nothing runs unless a test asks for it.

**What goes in conftest.py?**
Shared fixtures and hooks for that directory and below, discovered automatically without imports.

**Which fixture scopes would you use for browser, context and page, and why?**
Browser per session (slow to start, safe to share), context and page per test (isolation).

**How does a fixture know the test failed?**
A `pytest_runtest_makereport` hookwrapper stores the report on the item; the fixture reads it in teardown. (pytest-playwright does this internally for `--tracing retain-on-failure`.)

**How do you run a test with several data sets?**
`@pytest.mark.parametrize`, with `ids` for readable names.

Next: [4. API testing and API-based setup](04-api-testing-and-api-setup.md)
