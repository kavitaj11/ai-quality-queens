# CORS & the browser security model

[05 · Module topics](../../modules/05-networking.md) · [Curriculum index](../../README.md)

**Category** · HTTP IN DEPTH

The bug class where 'the API works in Postman but fails in the browser'. Same-Origin Policy blocks cross-origin reads; CORS is the server opting specific origins back in via Access-Control-* headers; non-simple requests trigger an OPTIONS preflight first. API clients skip all of this — which is exactly why browser tests catch what API tests can't.

## Worked reference

````text
CORS — why Postman works and the browser doesn't

SAME-ORIGIN POLICY
  origin = scheme + host + port
  https://shop.dev ≠ https://api.shop.dev  (host)
  https://shop.dev ≠ http://shop.dev       (scheme)
  JS on one origin can't READ responses from another
  — unless the server opts in via CORS headers

THE PREFLIGHT DANCE (non-simple requests)
  non-simple = methods beyond GET/POST-form, custom
  headers (Authorization!), or json content-type

  browser ──OPTIONS /api/orders──────────▶ server
            Origin: https://shop.dev
            Access-Control-Request-Method: PUT
  browser ◀─204 + Access-Control-Allow-* ─ server
  browser ──the real PUT──────────────────▶ server

  preflight fails → real request NEVER SENT →
  console error, network tab shows only OPTIONS

THE HEADERS (server-side, on BOTH preflight + response)
  Access-Control-Allow-Origin   exact origin (or *,
    but * is INVALID with credentials/cookies)
  Allow-Methods · Allow-Headers · Allow-Credentials
  Max-Age — how long the browser caches the preflight

QA TAKEAWAYS
  • API suites (requests/RestAssured/Postman) don't
    enforce SOP — only BROWSER tests catch CORS regressions
  • test the matrix: each app origin × env; staging
    origins often missing from the allow-list
  • 'CORS error' in console can MASK a 500 on the
    preflight — check the OPTIONS response itself
  • wildcard + credentials = misconfiguration; flag it
````

## Exercise

Reproduce a CORS failure deliberately (call staging API from a local page), read the exact console error, then fix it with the right Access-Control headers — not a wildcard.
