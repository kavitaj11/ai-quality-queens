# Timeouts, retries & resilience

[09 · Module topics](../../modules/09-api-testing.md) · [Curriculum index](../../README.md)

**Category** · BEYOND THE BASICS

Networks fail. Test that your client fails fast (aggressive per-request timeout, not a hang), retries transient errors with backoff (and only idempotent calls), and gives up cleanly. On the API side, verify slow/downstream failures return a sane error, not a 30s hang. Pair with circuit-breaker behavior where present.

## Worked reference

````text
import requests
from requests.adapters import HTTPAdapter
from urllib3.util.retry import Retry

# ── FAIL FAST: aggressive (connect, read) timeout ──
try:
    requests.get(f"{BASE}/slow", timeout=(3, 2))
    assert False, "should have timed out"
except requests.exceptions.Timeout:
    pass                                    # correct: fails fast

# ── RETRY transient errors with backoff (idempotent only) ──
s = requests.Session()
retries = Retry(total=3, backoff_factor=0.5,
                status_forcelist=[502, 503, 504],
                allowed_methods=["GET", "PUT", "DELETE"])  # NOT POST
s.mount("https://", HTTPAdapter(max_retries=retries))
assert s.get(f"{BASE}/flaky", timeout=(3, 5)).status_code == 200
````

## Exercise

Set a 2s timeout on a deliberately slow endpoint and assert it raises fast; then test a retry-with-backoff wrapper that succeeds on the 3rd try.
