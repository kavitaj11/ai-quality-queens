# Complex response validation

[09 · Module topics](../../modules/09-api-testing.md) · [Curriculum index](../../README.md)

**Category** · BEYOND THE BASICS

Real responses are nested, paginated, and polymorphic. Go beyond one field: assert deep/nested paths, arrays (every item, ordering, counts), conditional/optional fields, polymorphic unions (oneOf), and cross-field rules (total == sum of items). JSONPath + schema (oneOf/if-then, format) handle the shapes simple asserts can't.

## Worked reference

````text
import jsonschema, jmespath

body = r.json()
# ── nested + array-wide assertions (JMESPath) ──
skus = jmespath.search("items[].sku", body)
assert all(s.startswith("A-") for s in skus)
assert body["page"]["total"] >= len(body["items"])
# ── ordering ──
dates = [i["createdAt"] for i in body["items"]]
assert dates == sorted(dates, reverse=True)
# ── cross-field rule ──
assert body["total"] == sum(i["price"] * i["qty"] for i in body["items"])
# ── polymorphic / conditional shape (JSON Schema oneOf + if/then) ──
SCHEMA = {"type":"object","properties":{
  "payment":{"oneOf":[
    {"required":["cardLast4"]},
    {"required":["invoiceId"]}]}}}
jsonschema.validate(body, SCHEMA)
````

## Exercise

For a paginated order list, assert: page metadata, every item matches a schema, items are sorted by date desc, and total == sum(line items).
