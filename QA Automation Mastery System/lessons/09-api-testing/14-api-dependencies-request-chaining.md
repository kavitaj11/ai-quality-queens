# API dependencies & request chaining

[09 · Module topics](../../modules/09-api-testing.md) · [Curriculum index](../../README.md)

**Category** · BEYOND THE BASICS

Most real flows are multi-step: create a resource, extract its id, use it in the next call, and clean up after. Chain requests by capturing values from one response into the next, share auth across the chain, and manage the created state's lifecycle. Keep tests independent — each chain sets up and tears down its own data.

## Worked reference

````text
# Chain: capture values from one response, feed the next
token = requests.post(f"{BASE}/login",
    json={"email": u, "password": p}).json()["access_token"]
H = {"Authorization": f"Bearer {token}"}

# create -> capture id
oid = requests.post(f"{BASE}/orders", json=order, headers=H).json()["id"]
# use the id downstream
assert requests.get(f"{BASE}/orders/{oid}", headers=H).status_code == 200
# ... and always clean up (independent test)
requests.delete(f"{BASE}/orders/{oid}", headers=H)
````

## Exercise

Chain: register user → login (capture token) → create order (capture id) → get order → delete order, asserting at each step.
