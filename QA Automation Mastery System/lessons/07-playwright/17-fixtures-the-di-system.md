# Fixtures: the DI system

[07 · Module topics](../../modules/07-playwright.md) · [Curriculum index](../../README.md)

**Category** · TEST DESIGN

Fixtures are Playwright's dependency injection — the same mechanism that gives you page and request gives you your own typed dependencies: page objects, seeded data, logged-in sessions. Setup runs before use(), teardown after — they travel together, compose (fixtures can use fixtures), and only instantiate when a test asks for them. Worker-scoped fixtures share expensive setup per worker.

## Worked reference

````text
# pytest fixtures play the same role for Playwright Python
import pytest

@pytest.fixture
def user(playwright):                    # data fixture + cleanup
    api = playwright.request.new_context(base_url=BASE)
    u = api.post("/api/users", data=make_user()).json()
    yield u                              # test runs here
    api.delete(f"/api/users/{u['id']}")
    api.dispose()

@pytest.fixture(scope="session")         # login once per session
def admin_state(browser, tmp_path_factory):
    path = tmp_path_factory.mktemp("auth") / "admin.json"
    ctx = browser.new_context()
    ctx.request.post(f"{BASE}/api/login", data=ADMIN)
    ctx.storage_state(path=str(path))
    ctx.close()
    return str(path)

def test_admin_dashboard(browser, admin_state):
    ctx = browser.new_context(storage_state=admin_state)
    page = ctx.new_page()
    page.goto("/admin")
````

## Exercise

Build a test.extend with three fixtures: loginPage (page object), user (API-seeded + deleted after), and adminContext (worker-scoped storageState). Use all three in one test.
