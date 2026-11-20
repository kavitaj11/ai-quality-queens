# Synchronization & waits

[07 · Module topics](../../modules/07-playwright.md) · [Curriculum index](../../README.md)

**Category** · POWER FEATURES

The #1 cause of flaky tests is racing the app. Selenium waits explicitly (WebDriverWait + expected conditions); Playwright auto-waits on every action and retries web-first assertions until timeout. Know the timeout tiers too: Playwright's global/test/action/navigation/expect timeouts, and API request timeouts (requests, httpx, fetch AbortController, Playwright request, REST Assured) — a hung network call should fail fast, not hang the whole suite. sleep() is never the answer.

## Worked reference

````text
# ── UI: Selenium explicit wait ──
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC

wait = WebDriverWait(driver, 10)
btn = wait.until(EC.element_to_be_clickable(
    (By.CSS_SELECTOR, "[data-testid='submit']")))
btn.click()

# ── UI: Playwright (Python) auto-waits every action ──
page.click("[data-testid='submit']")     # waits itself
page.set_default_timeout(15_000)          # actions
page.set_default_navigation_timeout(30_000)
expect(page.locator(".toast")).to_have_text(
    "Saved", timeout=5_000)               # per-assertion retry

# ── API TIMEOUTS: a hung call must fail fast ──
import requests, httpx
# requests: (connect, read) seconds
requests.get(url, timeout=(3.05, 10))
# httpx: fine-grained
httpx.get(url, timeout=httpx.Timeout(10.0, connect=3.0))

# time.sleep(3)  <- never: too short = flake, too long = slow
````

## Exercise

Replace a sleep() with the right wait, then set an aggressive per-request API timeout and prove a slow endpoint fails fast instead of hanging.
