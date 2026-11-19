# Playwright with Python and pytest: Hands-On Tutorial

Verification figures below describe the reference lab runs. For checks performed on this copy and remaining environment requirements, see [validation notes](../VALIDATION.md).

**Level:** beginner to intermediate · **Time:** about 4–5 hours · **Last reviewed:** November 2026 (Playwright 1.57, pytest 8, Python 3.10+)

Build a Playwright + pytest framework against the **K11 Practice Shop**: UI tests, API tests, network mocking, tracing and CI. Each chapter teaches one skill with working code and ends with the **review questions** for that skill. The project has been run end to end: **21 tests pass**, and the smoke tests pass in **Chromium, Firefox and WebKit**.

## Chapters

| # | Chapter | Technical topics |
|---|---|---|
| 1 | [Why Playwright, and setup](01-why-playwright-and-setup.md) | Playwright vs Selenium, browsers, contexts |
| 2 | [Locators, auto-waiting and assertions](02-locators-auto-waiting-and-assertions.md) | get_by_role, test IDs, strictness, web-first assertions |
| 3 | [pytest fixtures and framework design](03-pytest-fixtures-and-framework-design.md) | conftest, fixture scopes, page objects, the pytest-playwright plugin |
| 4 | [API testing and API-based setup](04-api-testing-and-api-setup.md) | APIRequestContext, shared cookies, fast setup |
| 5 | [Network mocking and interception](05-network-mocking.md) | page.route, fulfill/continue, testing error states |
| 6 | [Debugging, tracing, parallel runs and CI](06-debugging-tracing-parallel-and-ci.md) | Trace viewer, codegen, xdist, GitHub Actions |
| 7 | [AI-assisted Playwright testing](07-ai-assisted-playwright.md) | MCP, test generation, self-healing, guardrails |
| 8 | [Review questions and exercises](08-review-questions-and-exercises.md) | Rapid-fire Q&A, debugging scenarios, coding tasks |

## Quick start

```bash
# Terminal 1: the app under test
python ../practice-apps/shop-web/shop_web.py            # http://127.0.0.1:8091

# Terminal 2: the tests
cd project
pip install -r requirements.txt
playwright install chromium                  # downloads the browser (once)
pytest                                       # all 21 tests
pytest -m smoke --browser-name firefox       # smoke tests in Firefox
pytest --headed -k checkout                  # watch the checkout tests
```

## Project layout

```
project/
├── conftest.py            browser / context / page fixtures, tracing on failure, API login
├── pytest.ini             markers: smoke, regression, network, api
├── requirements.txt
├── pages/                 page objects (login, products, product, cart, order)
└── tests/
    ├── test_login.py      parametrized negative logins
    ├── test_search.py     async results, auto-waiting
    ├── test_checkout.py   UI journey vs API-based setup
    ├── test_network.py    mocked errors, mocked data, held responses
    └── test_api.py        pure API tests with Playwright's request context
```

Next: [1. Why Playwright, and setup](01-why-playwright-and-setup.md)
