# Auth & authorization tests

[09 · Module topics](../../modules/09-api-testing.md) · [Curriculum index](../../README.md)

**Category** · API TEST FUNDAMENTALS

Testing authN/authZ against a real API: log in for a token, then prove the 401 (no token) / 403 (wrong role) / 200 (right role) matrix. Includes the auth schemes (Bearer, Basic, API key) and Playwright's log-in-once storageState pattern for reusing a session across tests.

## Worked reference

````text
import requests
BASE = "https://api.shop.dev"

def login(email, pwd="s3cret"):
    r = requests.post(f"{BASE}/auth/login",
        json={"email": email, "password": pwd})
    assert r.status_code == 200
    return r.json()["access_token"]

def bearer(token):
    return {"Authorization": f"Bearer {token}"}

def test_authn_vs_authz():
    # 401 = not authenticated (who are you?)
    assert requests.get(f"{BASE}/admin/users") \
        .status_code == 401

    # 403 = authenticated but not authorized
    customer = login("customer@test.dev")
    assert requests.get(f"{BASE}/admin/users",
        headers=bearer(customer)).status_code == 403

    # 200 = right role
    admin = login("admin@test.dev")
    assert requests.get(f"{BASE}/admin/users",
        headers=bearer(admin)).status_code == 200
````

## Exercise

For one protected endpoint write 4 tests: no token → 401, expired → 401, valid token wrong role → 403, admin → 200.
