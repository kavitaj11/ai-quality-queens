# Rate limiting & throttling

[09 · Module topics](../../modules/09-api-testing.md) · [Curriculum index](../../README.md)

**Category** · BEYOND THE BASICS

Public and internal APIs cap request rates. Test both sides: that limits are ENFORCED (burst past the quota → 429 with Retry-After and rate-limit headers), and that your CLIENT behaves (backs off, respects Retry-After, doesn't hammer). Also make sure your own test suite doesn't trip limits — throttle or use test accounts with higher quotas.

## Worked reference

````text
import requests, time

# ── ENFORCEMENT: burst past the quota -> 429 ──
codes = [requests.get(f"{BASE}/search", headers=H).status_code
         for _ in range(50)]
assert 429 in codes, "rate limit never triggered"

# ── the 429 must guide the client ──
r = requests.get(f"{BASE}/search", headers=H)
if r.status_code == 429:
    assert "Retry-After" in r.headers
    assert int(r.headers.get("X-RateLimit-Remaining", 0)) == 0

# ── CLIENT behavior: respect Retry-After, then recover ──
    time.sleep(int(r.headers["Retry-After"]))
    assert requests.get(f"{BASE}/search", headers=H).status_code == 200
````

## Exercise

Fire requests past the documented limit; assert 429 + Retry-After, then verify a request succeeds again after the window.
