# Anatomy of an API response & validation

[09 · Module topics](../../modules/09-api-testing.md) · [Curriculum index](../../README.md)

**Category** · API TEST FUNDAMENTALS

A response has a status line (code), headers, and a body. Robust API tests validate all three layers: the status code, the key headers (content-type, cache, rate-limit), and the body — field values, types, and the whole shape via JSON Schema. Validating structure (not just one field) catches contract breaks before they reach the UI.

## Worked reference

````text
import requests, jsonschema

r = requests.get(f"{BASE}/orders/42",
                 headers={"Authorization": f"Bearer {token}"},
                 timeout=(3, 10))

# 1. STATUS
assert r.status_code == 200

# 2. HEADERS
assert r.headers["Content-Type"].startswith("application/json")
assert r.elapsed.total_seconds() < 1.0              # response time

# 3. BODY — values, types, then whole-shape schema
body = r.json()
assert body["status"] == "pending"                  # value
assert isinstance(body["total"], (int, float))      # type
assert isinstance(body["items"], list)

ORDER_SCHEMA = {
    "type": "object",
    "required": ["id", "status", "total", "items"],
    "properties": {
        "id":     {"type": "string"},
        "status": {"enum": ["pending", "paid", "shipped"]},
        "total":  {"type": "number", "minimum": 0},
        "items":  {"type": "array", "minItems": 1},
    },
}
jsonschema.validate(body, ORDER_SCHEMA)   # SHAPE / contract
````

## Exercise

For one endpoint assert: status 200, content-type JSON, response time < 1s, three field values, field types, and the full body against a JSON Schema.
