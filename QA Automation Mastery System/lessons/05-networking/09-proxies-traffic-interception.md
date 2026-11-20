# Proxies & traffic interception

[05 · Module topics](../../modules/05-networking.md) · [Curriculum index](../../README.md)

**Category** · TOOLING & DEBUGGING

An intercepting proxy (mitmproxy, Charles, Fiddler, Burp) sits between client and server: see every request from ANY app — including mobile devices — modify traffic on the fly, throttle, and replay. It's how you test the app you can't instrument: point the device at the proxy, install the proxy's CA, and the encrypted conversation becomes visible.

## Worked reference

````text
INTERCEPTING PROXIES — see and shape any app's traffic

  device/app ───▶ PROXY (you) ───▶ real server
              ◀───         ◀───
  setup: point device's proxy at your machine:port,
  install the proxy's CA cert (see TLS lesson);
  cert-PINNED apps resist by design — expected

WHAT YOU CAN DO
  OBSERVE   every request incl. mobile/desktop apps
            you can't open DevTools in
  MODIFY    rewrite rules: turn a 200 into a 500,
            null a field, delay a response — chaos
            testing without touching the backend
  THROTTLE  3G/flaky profiles — 'works on office
            wifi' is not tested
  REPLAY    re-send a captured request with edits —
            fast API poking with real auth context
  MAP LOCAL serve a local file for a prod URL —
            test a fix against live backend

THE TOOLS
  mitmproxy  free, CLI + python addons — scriptable
             (auto-rewrite, assert on traffic in CI)
  Charles    GUI, the mobile-QA classic
  Fiddler    Windows staple
  Burp       same idea, security-focused (see
             Non-Functional Testing)

WHEN PROXY vs page.route()
  Playwright route(): browser tests, per-test stubs
  proxy: real devices · native apps · cross-app
  flows · anything that isn't your browser context
````

## Exercise

Route a mobile device (or emulator) through mitmproxy, capture your app's API traffic, and use a rewrite rule to turn one 200 into a 500 — how does the app cope?
