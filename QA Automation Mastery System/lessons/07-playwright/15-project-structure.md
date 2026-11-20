# Project structure

[07 · Module topics](../../modules/07-playwright.md) · [Curriculum index](../../README.md)

**Category** · TEST DESIGN

The folder layout that makes the rest of Test Design click: tests grouped by feature, page objects in pages/, custom fixtures in one fixtures.ts, test data + factories in data/, config at the root, and auth state in a gitignored .auth/. Every piece has exactly one home — tests read like intent, everything else is infrastructure.

## Worked reference

````text
PLAYWRIGHT PROJECT STRUCTURE — Python (pytest-playwright)

  shop-e2e/
  ├── pytest.ini / pyproject.toml  ← markers, base-url, addopts:
  │                                  [pytest] addopts = --tracing
  │                                  retain-on-failure
  ├── requirements.txt             ← pinned deps
  ├── conftest.py                  ← FIXTURES live here (root):
  │                                  page objects, seeded data,
  │                                  storage_state, api contexts
  │
  ├── tests/                       ← test_*.py by feature
  │   ├── conftest.py              ← feature-scoped fixtures
  │   ├── checkout/
  │   │   ├── test_checkout.py
  │   │   └── test_discounts.py
  │   ├── cart/test_cart.py
  │   └── api/test_orders.py       ← pure API tests
  │
  ├── pages/                       ← page objects
  │   ├── __init__.py
  │   ├── login_page.py
  │   ├── dashboard_page.py
  │   └── components/nav_bar.py
  │
  ├── data/
  │   ├── factories.py             ← make_user() etc. (Faker)
  │   └── constants.py
  ├── utils/                       ← api_client.py, ids.py
  ├── .auth/                       ← storage_state json (GITIGNORED)
  └── artifacts/                   ← traces, screenshots (gitignored)

KEY DIFFERENCES FROM TS
  • fixtures = pytest fixtures in conftest.py (auto-discovered,
    no imports needed in tests)
  • config = pytest.ini/pyproject + CLI flags, not one config file
  • run:  pytest -m smoke -n 4 --browser webkit --headed
````

## Exercise

Restructure a flat spec folder into this layout, then trace one test's imports: spec → fixtures → page object → data factory.
