# Anatomy of an API request

[09 · Module topics](../../modules/09-api-testing.md) · [Curriculum index](../../README.md)

**Category** · API TEST FUNDAMENTALS

Every request has four parts: the method + URL (with path and query params), headers (auth, content-type, accept), and an optional body (JSON for most REST APIs). Getting each right — and validating you sent it right — is half of API testing. This breaks down the full structure and shows building + asserting each part.

## Worked reference

````text
import requests
BASE = "https://api.shop.dev/v1"

# Build every part explicitly, then assert you built it right
req = requests.Request(
    method="POST",
    url=f"{BASE}/orders/42/items",
    params={"expand": "true", "currency": "USD"},   # query string
    headers={
        "Authorization": f"Bearer {token}",
        "Content-Type": "application/json",
        "Accept": "application/json",
    },
    json={"sku": "A-100", "qty": 2, "gift": True},   # body
)
prepared = requests.Session().prepare_request(req)

# ── VALIDATE THE OUTGOING REQUEST ──
assert prepared.method == "POST"
assert prepared.url.endswith("currency=USD")         # query built
assert prepared.headers["Content-Type"] == "application/json"
import json
assert json.loads(prepared.body)["qty"] == 2         # body serialized

resp = requests.Session().send(prepared, timeout=(3, 10))
````

## Exercise

Compose a POST with path param, two query params, auth + content-type headers, and a JSON body; log the exact request and confirm each part is set before sending.
