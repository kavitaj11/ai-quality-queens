# 1. Why Playwright, and Setup

## How Playwright works

```
Your Python test ──(one persistent WebSocket connection)──► Playwright driver ──► Chromium / Firefox / WebKit
                                                            (Node.js process)    (patched builds Playwright ships)
```

Unlike WebDriver's one-HTTP-request-per-command model, Playwright keeps a single connection open and talks to the browser's own automation protocol. That makes it fast and lets it listen to everything the browser does: network requests, console messages, page navigations.

## Browser, context, page

| Object | What it is | Cost | Typical scope |
|---|---|---|---|
| **Browser** | A browser process | Slow to start (~1–3 s) | Once per test run |
| **BrowserContext** | An isolated session: its own cookies, storage, cache, like a fresh incognito window | Milliseconds | **One per test** |
| **Page** | A tab inside a context | Milliseconds | One (or more) per test |

This is Playwright's big design win: **test isolation is cheap**. Each test gets a brand-new context, so tests can't leak logins or carts into each other, without paying for a new browser each time.

## Playwright vs Selenium

| | Playwright | Selenium |
|---|---|---|
| Waiting | Auto-waits before every action; assertions retry | You write explicit waits |
| Browsers | Chromium, Firefox, WebKit (Safari engine) bundled | Any browser with a W3C driver, including real Safari |
| Isolation | Contexts: fast, per test | Usually a new browser per test |
| Network | Intercept, mock, modify built in | Limited (CDP in Chromium only) |
| API testing | Built in (`request` context) | Separate library |
| Debugging | Trace viewer, codegen, inspector | Screenshots and logs |
| Languages | JS/TS, Python, Java, .NET | Java, Python, C#, JS, Ruby, Kotlin |
| Ecosystem | Newer, fast-growing | Very large, Grid, every cloud provider |

Neither is "better" in general. Playwright is usually faster to write and more stable for modern web apps; Selenium wins on real-browser and legacy coverage and an enormous ecosystem.

## Set up

```bash
pip install -r requirements.txt     # playwright + pytest
playwright install chromium         # or: playwright install  (all three browsers)
python ../practice-apps/shop-web/shop_web.py
cd project && pytest
```

Run options defined in this project's `conftest.py`:

| Option | Default | Example |
|---|---|---|
| `--browser-name` | `chromium` | `--browser-name webkit` |
| `--headed` | off (headless) | `--headed` to watch |
| `--shop-url` | `http://127.0.0.1:8091` (or `BASE_URL`) | `--shop-url https://staging.example.com` |
| `-m` | all | `-m smoke`, `-m "not network"` |

## Review questions

**What is a BrowserContext and why does it matter?**
An isolated browser session (cookies, storage, cache) inside one browser process. Creating one per test gives full isolation in milliseconds, which is what makes Playwright suites fast and independent.

**Playwright or Selenium for a new project?**
Depends on the constraints: for a modern web app with a team that can use Python/TS, Playwright's auto-waiting, tracing and network control usually win. If you need real Safari or old browsers, a big existing Selenium framework or a Grid investment, Selenium stays sensible.

**Does Playwright test real Safari?**
No. It tests WebKit, Safari's engine, built by the Playwright team. That catches most WebKit issues but isn't identical to Safari on macOS or iOS.

**Sync or async API in Python?**
The sync API (used here) is simpler for tests. The async API suits code that already uses asyncio or needs to drive many pages concurrently in one process.

Next: [2. Locators, auto-waiting and assertions](02-locators-auto-waiting-and-assertions.md)
