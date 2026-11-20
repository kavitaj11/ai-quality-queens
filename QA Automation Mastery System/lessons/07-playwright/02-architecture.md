# Architecture

[07 · Module topics](../../modules/07-playwright.md) · [Curriculum index](../../README.md)

**Category** · PLAYWRIGHT CORE

How Playwright talks to a browser — and why it's fast and stable. Your test calls the Playwright client, which sends commands over a single persistent WebSocket connection to a server process, which drives a bundled browser via its native protocol (CDP for Chromium; patched protocols for Firefox/WebKit). Browser → Context → Page is the object model: contexts are cheap, isolated 'incognito' profiles that make parallel tests clean.

## Worked reference

````text
PLAYWRIGHT ARCHITECTURE

   ┌─────────────────┐
   │   YOUR TEST     │   await page.getByRole('button').click()
   │  (JS/TS/Py/Java)│
   └────────┬────────┘
            │  Playwright CLIENT (language binding)
            ▼
   ┌──────────────────────────────────────────────┐
   │        ONE persistent WebSocket connection      │  ← not HTTP
   │   (bidirectional: commands ↓  +  events ↑)      │     request/reply
   └────────┬────────────────────────────────────────┘
            ▼
   ┌─────────────────┐
   │ PLAYWRIGHT SERVER│  (Node process; drives the browser)
   └────────┬────────┘
            │  native protocol
            ▼
   ┌───────────────────────────────────────────────┐
   │  BUNDLED BROWSER (version-matched by Playwright) │
   │   Chromium (CDP) · Firefox · WebKit (patched)    │
   └───────────────────────────────────────────────┘


THE OBJECT MODEL — Browser ▸ Context ▸ Page

   Browser  ──────────────────────────────────  (one launch)
     │
     ├── BrowserContext #1   ← isolated: own cookies, storage,
     │      ├── Page (tab)      cache — like a fresh incognito
     │      └── Page (tab)
     │
     ├── BrowserContext #2   ← Test B, fully isolated from Test A
     │      └── Page
     └── ...

   WHY CONTEXTS MATTER
     • cheap to create → a fresh one PER TEST = clean state
     • isolated       → parallel tests never leak into each other
     • fast           → no full browser relaunch each time
     • storageState   → save/restore login once, reuse everywhere


WHY THIS IS FAST + STABLE
   persistent socket  → no per-command HTTP handshake (vs WebDriver)
   events pushed up    → auto-wait knows the instant the DOM is ready
   bundled browsers    → no version drift between driver and browser
````

## Exercise

Diagram the Browser→Context→Page tree, then explain why creating a fresh context per test is both faster and safer than a fresh browser per test.
