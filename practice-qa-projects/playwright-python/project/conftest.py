"""Fixtures for the Playwright tests.

This file builds the browser, context and page fixtures by hand so you can see how they
work. The pytest-playwright plugin provides the same fixtures (browser, context, page,
--browser, --headed, --tracing); Chapter 3 compares the two.

Command-line options:
    --browser-name chromium|firefox|webkit   (default chromium)
    --headed                                  show the browser
    --shop-url http://127.0.0.1:8091          app under test (or BASE_URL env var)
"""
import os
import re
from pathlib import Path

import pytest
from playwright.sync_api import Browser, BrowserContext, Page, Playwright, sync_playwright

ARTIFACTS = Path(__file__).parent / "test-results"
PASSWORD = os.environ.get("SHOP_PASSWORD", "Passw0rd!")


def pytest_addoption(parser):
    parser.addoption("--browser-name", default="chromium", choices=["chromium", "firefox", "webkit"])
    parser.addoption("--headed", action="store_true", help="show the browser window")
    parser.addoption("--shop-url", default=os.environ.get("BASE_URL", "http://127.0.0.1:8091"))


@pytest.hookimpl(hookwrapper=True)
def pytest_runtest_makereport(item, call):
    """Record each phase's result on the test item so fixtures can see whether the test failed."""
    outcome = yield
    report = outcome.get_result()
    setattr(item, f"rep_{report.when}", report)


@pytest.fixture(scope="session")
def base_url(pytestconfig) -> str:
    return pytestconfig.getoption("--shop-url").rstrip("/")


@pytest.fixture(scope="session")
def playwright() -> Playwright:
    with sync_playwright() as p:
        yield p


@pytest.fixture(scope="session")
def browser(playwright, pytestconfig) -> Browser:
    """One browser process for the whole run: starting a browser is the slow part."""
    browser_type = getattr(playwright, pytestconfig.getoption("--browser-name"))
    browser = browser_type.launch(headless=not pytestconfig.getoption("--headed"))
    yield browser
    browser.close()


@pytest.fixture
def context(browser, base_url, request) -> BrowserContext:
    """A fresh, isolated context (cookies, storage, cache) per test: cheap, like an incognito window.

    Tracing records every action, DOM snapshot and network call. The trace is saved only
    when the test fails; open it with:  playwright show-trace test-results/<test>/trace.zip
    """
    context = browser.new_context(base_url=base_url, viewport={"width": 1280, "height": 900})
    context.set_default_timeout(10_000)
    context.tracing.start(screenshots=True, snapshots=True, sources=True)
    yield context
    failed = getattr(request.node, "rep_call", None) is not None and request.node.rep_call.failed
    if failed:
        out = ARTIFACTS / re.sub(r"[^\w.-]+", "_", request.node.name)
        out.mkdir(parents=True, exist_ok=True)
        context.tracing.stop(path=out / "trace.zip")
        for i, pg in enumerate(context.pages):
            pg.screenshot(path=out / f"page-{i}.png", full_page=True)
        print(f"\nFailure artifacts: {out}")
    else:
        context.tracing.stop()
    context.close()


@pytest.fixture
def page(context) -> Page:
    return context.new_page()


@pytest.fixture
def logged_in_page(context, page) -> Page:
    """Log in through the API. context.request shares cookies with the browser context,
    so the page is already logged in: no UI login, no waiting, no flakiness."""
    response = context.request.post("/api/login", data={"username": "pw-user", "password": PASSWORD})
    assert response.ok, f"API login failed: {response.status} {response.text()}"
    return page
