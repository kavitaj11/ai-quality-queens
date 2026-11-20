# WebDriver BiDi

[06 · Module topics](../../modules/06-selenium.md) · [Curriculum index](../../README.md)

**Category** · WEBDRIVER CORE

Classic WebDriver is one-directional: you send a command, you get one response — it can't push events to you. WebDriver BiDi (BiDirectional) is the new W3C standard that adds a persistent, two-way channel (over WebSockets), so the browser can stream events — console logs, network requests, JS errors — to your test as they happen. It brings the power people used vendor-only CDP for into a cross-browser standard.

## Worked reference

````text
CLASSIC WEBDRIVER  vs  WEBDRIVER BiDi

CLASSIC (W3C WebDriver — HTTP, request/response)
   TEST ──POST /element──▶ DRIVER ──▶ BROWSER
   TEST ◀──── response ─── DRIVER ◀── BROWSER
   • one command → one reply, then the channel is idle
   • the browser CANNOT tell you "a console error just fired"
   • to notice something, you must POLL for it


BiDi (WebDriver BiDi — WebSocket, bidirectional + events)
                 ┌──────── persistent WebSocket ────────┐
   TEST ◀════════╡         DRIVER  ⇄  BROWSER            ╞
                 └──────────────────────────────────────┘
   • TEST sends commands  AND  subscribes to EVENTS
   • BROWSER PUSHES events the moment they happen:
        log.entryAdded        → console.log / errors
        network.beforeRequest → intercept / mock a call
        network.responseCompleted
        browsingContext.load  → navigation finished
        script.message        → messages from injected JS


WHAT BiDi UNLOCKS (that classic WebDriver can't)
   ✓ fail a test instantly on any console error
   ✓ capture & assert on network traffic — no proxy
   ✓ intercept / stub responses at the browser edge
   ✓ react to DOM & navigation events in real time
   ✓ cross-browser — a STANDARD, unlike Chromium-only CDP


THE PROTOCOL LINEAGE
   JSON Wire Protocol   → W3C WebDriver      → WebDriver BiDi
   (Selenium 2/3,         (Selenium 4,          (event-driven,
    reverse-engineered)    HTTP standard)        the future)

   CDP (Chrome DevTools Protocol) = powerful but Chromium-ONLY;
   BiDi is the standards-based successor that all browsers adopt.
````

## Exercise

Compare the two models on a whiteboard, then name three test scenarios BiDi enables that classic WebDriver can't (live console errors, network interception, DOM mutation events).
