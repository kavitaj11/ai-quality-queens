# Authentication & authorization

[01 · Module topics](../../modules/01-foundations.md) · [Curriculum index](../../README.md)

**Category** · WEB & HTTP ESSENTIALS

Authentication = who you are (login, tokens); authorization = what you may do (roles, permissions). Know the schemes — Basic, Bearer/JWT, OAuth2, API keys, session cookies — and always test both directions: no token → 401, valid token but wrong role → 403, right role → 200.

## Worked reference

````text
AUTHENTICATION vs AUTHORIZATION

  AUTHENTICATION (authN) = WHO you are   → proven by login/token
  AUTHORIZATION  (authZ) = WHAT you may do → decided by role/perms

  Log in first (authN), then every request is checked against
  your permissions (authZ). Two different failures, two codes:
    no / bad token           → 401 Unauthenticated
    valid token, wrong role  → 403 Forbidden
    correct role             → 200 OK

THE SCHEMES YOU'LL TEST
  Basic          user:pass base64 in the header (dev/internal)
  Bearer / JWT   Authorization: Bearer <token> — the common case;
                 the token encodes identity + claims, and expires
  OAuth2         delegated access; you exchange for an access token
  API key        X-Api-Key header — machine-to-machine
  Session cookie server sets a cookie; browser sends it back

WHAT TO ALWAYS TEST (both directions)
  ✓ happy path       right creds / role  → 200
  ✓ no token         → 401
  ✓ expired token    → 401
  ✓ valid token, wrong role (e.g. customer → /admin) → 403
  ✓ token tampering / privilege escalation blocked

  → The login + 401/403/200 code in every stack (and Playwright's
    log-in-once storageState pattern) lives in the API Testing phase.
````

## Exercise

For one protected endpoint, write 4 tests: no token → 401, expired token → 401, valid token wrong role → 403, admin → 200.
