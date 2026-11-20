# HTTP methods lifecycle

[09 · Module topics](../../modules/09-api-testing.md) · [Curriculum index](../../README.md)

**Category** · API TEST FUNDAMENTALS

The full CRUD lifecycle in code — one order walked through POST → GET → PUT → PATCH → DELETE with the right status assertion at each step (201, 200, 200, 200, 204, then 404). Shows idempotency in practice across requests, fetch, Playwright's request fixture, and REST Assured.

## Worked reference

````text
import requests
s = requests.Session()
BASE = "https://api.shop.dev"

r = s.post(f"{BASE}/orders",              # NOT idempotent
           json={"sku": "A-100", "qty": 2})
assert r.status_code == 201               # created
oid = r.json()["id"]

r = s.get(f"{BASE}/orders/{oid}")         # idempotent
assert r.status_code == 200

r = s.put(f"{BASE}/orders/{oid}",         # full replace,
    json={"sku": "A-100", "qty": 5})      # idempotent
assert r.status_code == 200

r = s.patch(f"{BASE}/orders/{oid}",       # partial update
            json={"qty": 6})
assert r.status_code == 200

r = s.delete(f"{BASE}/orders/{oid}")      # idempotent
assert r.status_code == 204               # no content
assert s.get(f"{BASE}/orders/{oid}").status_code == 404
````

## Exercise

Walk one resource through all five methods, asserting the exact status at each step; then replay the POST and assert the duplicate it creates.
