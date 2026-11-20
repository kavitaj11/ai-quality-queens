# HTTP methods & status codes

[01 · Module topics](../../modules/01-foundations.md) · [Curriculum index](../../README.md)

**Category** · WEB & HTTP ESSENTIALS

GET reads, POST creates, PUT replaces, PATCH partially updates, DELETE removes. Idempotency = safe to retry: GET/PUT/DELETE give the same result every time; POST doesn't (retry = duplicate); PATCH depends. Status families: 2xx success (200 OK, 201 Created, 204 No Content), 3xx redirect, 4xx your fault (400 bad request, 401 unauthenticated, 403 forbidden, 404 not found, 409 conflict, 422 validation), 5xx their fault. The code walks one order through all five methods.

## Worked reference

````text
HTTP METHODS & STATUS CODES

THE METHODS (verbs)
  GET     read a resource                 safe · idempotent
  POST    create a new resource           NOT idempotent
  PUT     replace a resource entirely     idempotent
  PATCH   partially update a resource     may not be idempotent
  DELETE  remove a resource               idempotent

IDEMPOTENCY = "safe to retry"
  An idempotent call gives the SAME result no matter how many
  times you send it. GET/PUT/DELETE are idempotent; POST is not
  (retry a POST → a duplicate order). This matters for retries,
  network blips, and flaky-test design.
    POST /orders  ×2  →  two orders      ✗ not idempotent
    PUT  /orders/1 ×2  →  same state      ✓ idempotent
    DELETE /orders/1 ×2 → 204 then 404    ✓ idempotent

STATUS CODE FAMILIES
  2xx  SUCCESS      200 OK · 201 Created · 204 No Content
  3xx  REDIRECT     301 Moved · 304 Not Modified
  4xx  YOUR FAULT   400 Bad Request · 401 Unauthenticated
                    403 Forbidden · 404 Not Found
                    409 Conflict · 422 Validation · 429 Too Many
  5xx  THEIR FAULT  500 Internal · 502 Bad Gateway · 503 Unavail.

READ IT LIKE A TESTER
  401 vs 403 — 401 = "who are you?" (not logged in);
               403 = "I know you, but you may not" (wrong role).
  422 vs 400 — 422 = well-formed but invalid data;
               400 = malformed request.
  A 5xx is almost always a real bug worth filing.

  → The full GET/POST/PUT/PATCH/DELETE lifecycle in code lives in
    the API Testing phase (HTTP clients + test runners).
````

## Exercise

In DevTools, walk a checkout flow and log every request: method, status, idempotent yes/no. Then replay a POST and observe the duplicate it creates.
