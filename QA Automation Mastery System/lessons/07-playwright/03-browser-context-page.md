# Browser, Context & Page

[07 · Module topics](../../modules/07-playwright.md) · [Curriculum index](../../README.md)

**Category** · PLAYWRIGHT CORE

Playwright's core object model, in code. Browser = a launched engine (heavy, launch once). BrowserContext = an isolated 'incognito' session (cheap — own cookies, storage, cache; create one per test for clean, parallel-safe state). Page = a tab inside a context. Supported engines: Chromium (Chrome/Edge), Firefox, and WebKit (Safari) — one API drives all three. Mastering this tree is how you get fast, isolated, reliable runs.

## Worked reference

````text
from playwright.sync_api import sync_playwright

with sync_playwright() as p:
    # ── SUPPORTED ENGINES — one API, three browsers ──
    browser = p.chromium.launch(headless=False)   # Chrome/Edge engine
    # browser = p.firefox.launch()                # Firefox
    # browser = p.webkit.launch()                 # Safari engine

    # ── CONTEXT = isolated session (cheap; one per test) ──
    ctx = browser.new_context(
        viewport={"width": 1440, "height": 900},
        locale="en-US",
        base_url="https://shop.dev")

    # ── PAGE = a tab inside the context ──
    page = ctx.new_page()
    page.goto("/login")            # base_url applied
    page2 = ctx.new_page()         # second tab, SAME session

    # ── ISOLATION: a second context can't see the first's cookies ──
    guest = browser.new_context()  # fresh incognito, no login
    # ctx (logged in)  and  guest  never leak into each other

    # ── storageState: save login once, reuse everywhere ──
    ctx.storage_state(path="auth.json")
    reused = browser.new_context(storage_state="auth.json")

    ctx.close(); guest.close(); browser.close()

# Emulate a real device (built-in descriptors):
#   iphone = p.devices["iPhone 15"]
#   ctx = browser.new_context(**iphone)
````

## Exercise

Launch each of the 3 engines, create two isolated contexts (logged-in vs guest) in one browser, and prove a cookie set in one context is invisible to the other.
