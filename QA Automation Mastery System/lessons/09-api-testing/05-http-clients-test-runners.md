# HTTP clients + test runners

[09 · Module topics](../../modules/09-api-testing.md) · [Curriculum index](../../README.md)

**Category** · API TEST FUNDAMENTALS

The fastest way to broad coverage: session handling, auth, and clean assertion patterns in your language of choice. Most 'UI bugs' can be caught here in milliseconds.

## Worked reference

````text
import requests

BASE = "https://api.shop.dev"

def test_create_and_fetch_order(auth_session):
    created = auth_session.post(f"{BASE}/orders",
        json={"sku": "A-100", "qty": 1}).json()

    r = auth_session.get(f"{BASE}/orders/{created['id']}")
    assert r.status_code == 200
    assert r.json()["sku"] == "A-100"

def test_missing_sku_rejected(auth_session):
    r = auth_session.post(f"{BASE}/orders", json={"qty": 1})
    assert r.status_code == 422
````

## Exercise

Cover CRUD for one resource on a public API with 8 tests including negative cases — in two languages.
