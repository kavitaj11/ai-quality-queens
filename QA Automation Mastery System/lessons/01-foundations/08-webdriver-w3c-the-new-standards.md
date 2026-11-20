# WebDriver, W3C & the new standards

[01 · Module topics](../../modules/01-foundations.md) · [Curriculum index](../../README.md)

**Category** · WEB & HTTP ESSENTIALS

Three ways to drive a browser: W3C WebDriver (HTTP, request-response — Selenium's default since v4), CDP (Chrome DevTools Protocol — fast, event-driven, Chromium-only), and WebDriver BiDi — the new W3C bidirectional standard bringing console, network, and event streams to every browser. Each tab shows the event-driven style in that stack.

## Worked reference

````text
WEBDRIVER, W3C & THE NEW STANDARDS

Three ways a tool can drive a browser — know the trade-offs.

┌─ 1. W3C WebDriver  (HTTP · request → response) ────────────┐
  The official web standard (2018). Selenium's default since
  v4. Your test sends one command, gets one reply, over HTTP.
  + universal: every browser + real devices, any language
  − one-directional: the browser can't PUSH events to you;
    to notice a console error you must poll. Chatty = slower.

┌─ 2. CDP — Chrome DevTools Protocol  (fast · event-driven) ─┐
  The low-level protocol DevTools itself uses. Fast, supports
  network stubbing, console capture, geolocation, throttling.
  + powerful, real-time events
  − Chromium-ONLY (Chrome/Edge) — not a cross-browser standard.
  Playwright uses CDP under the hood for Chromium.

┌─ 3. WebDriver BiDi — BiDirectional  (the new standard) ────┐
  The W3C's answer to CDP: a persistent, two-way channel
  (WebSocket) that IS a cross-browser standard. The browser
  streams events — console logs, network, JS errors — to your
  test as they happen.
  + CDP-class power, but standardized across all browsers
  + subscribe to events instead of polling
  Selenium 4 exposes BiDi; Playwright is built event-first.

PROTOCOL LINEAGE
  JSON Wire Protocol   →   W3C WebDriver   →   WebDriver BiDi
  (Selenium 2/3,           (Selenium 4,        (event-driven,
   reverse-engineered)      HTTP standard)      the future)

WHAT BiDi UNLOCKS THAT CLASSIC WEBDRIVER CAN'T
  ✓ fail a test instantly on any console error
  ✓ intercept / mock network at the browser edge
  ✓ react to DOM & navigation events in real time
  ✓ cross-browser — unlike Chromium-only CDP

WHY IT MATTERS FOR YOU
  Classic WebDriver is fine for click-and-assert flows. Reach
  for CDP/BiDi when you need console-error gates, network
  mocking, or throttling. See the Selenium "Beyond the Basics"
  section for the actual BiDi / CDP code in each stack.
````

## Exercise

Enable BiDi or console capture in your stack and fail a test when any console error appears during the flow.
