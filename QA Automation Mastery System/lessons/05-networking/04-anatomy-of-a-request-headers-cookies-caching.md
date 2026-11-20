# Anatomy of a request: headers, cookies & caching

[05 · Module topics](../../modules/05-networking.md) · [Curriculum index](../../README.md)

**Category** · HTTP IN DEPTH

Beyond methods and status codes (Foundations): the headers that change test outcomes. Content negotiation (Accept, Content-Type), auth transport (Authorization, cookies + their Secure/HttpOnly/SameSite flags), caching (Cache-Control, ETag, 304s that make your 'update' look ignored), and the proxy headers (X-Forwarded-For) that explain why staging sees a different client IP.

## Worked reference

````text
HTTP HEADERS — the ones that change test outcomes

REQUEST
  Content-Type    what I'M sending (json vs form —
                  wrong one = 415 or silent mis-parse)
  Accept          what I want back
  Authorization   Bearer <token> — expiry mid-suite is
                  a classic 401 cascade
  Cookie          session state, sent automatically by
                  browsers — NOT by your API client

COOKIE FLAGS (test these, they're security bugs)
  Secure     HTTPS only          HttpOnly  no JS access
  SameSite   Lax/Strict/None — CSRF posture; None
             requires Secure; breaks cross-site flows

CACHING — why your 'update' looks ignored
  Cache-Control: max-age=300   client reuses for 5 min
  ETag + If-None-Match → 304   'unchanged, use yours'
  TEST TRAP: assert fresh data → get cached response.
  Bust with cache disabled (DevTools), new context
  (Playwright), or a cache-busting query param.
  VARY decides which header values split the cache.

RESPONSE
  Set-Cookie · Location (redirects — does your client
  follow them? curl doesn't by default) ·
  X-Forwarded-For / X-Real-IP — the LB rewrites
  origin: why geo/IP logic behaves differently
  behind the proxy than locally

RULE
  when an API test disagrees with the browser, diff
  the HEADERS first — same URL, different headers is
  the usual whole story.
````

## Exercise

Capture one login + one product-page request in DevTools; annotate every request and response header with what would break if it were wrong.
