# JSON Schema validation

[09 · Module topics](../../modules/09-api-testing.md) · [Curriculum index](../../README.md)

**Category** · CONTRACTS & SCHEMAS

Assert response shape, not just a few fields — schema checks catch breaking changes fields-you-forgot-to-assert would miss.

## Worked reference

````text
from jsonschema import validate

ORDER_SCHEMA = {
    "type": "object",
    "required": ["id", "sku", "qty", "status"],
    "properties": {
        "id":     {"type": "string"},
        "qty":    {"type": "integer", "minimum": 1},
        "status": {"enum": ["pending", "paid", "shipped"]},
    },
}

validate(instance=r.json(), schema=ORDER_SCHEMA)
````

## Exercise

Write a schema for your order resource and validate every GET response against it.
