# 5. JSON Parsing and Schema Validation

## Parse responses into Java records

`models/Order.java`:

```java
public record Order(String orderId, String status, String username, String currency,
                    BigDecimal total, OffsetDateTime createdAt, List<OrderLine> lines) {

    public record OrderLine(int productId, String name, int quantity, BigDecimal unitPrice, BigDecimal lineTotal) {
    }
}
```

Records give constructors, getters, `equals`, `hashCode` and `toString` for free, and Jackson maps JSON into them directly.

## One configured ObjectMapper

`json/Json.java`:

```java
public static final ObjectMapper MAPPER = JsonMapper.builder()
        .addModule(new JavaTimeModule())                                  // OffsetDateTime, LocalDate
        .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)        // 34.99 → BigDecimal, never double
        .enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)        // a new field fails mapping
        .build();
```

| Setting | Why |
|---|---|
| `USE_BIG_DECIMAL_FOR_FLOATS` | Money must be exact; `double` can't represent 0.1 |
| `JavaTimeModule` | Dates and timestamps as `OffsetDateTime`/`LocalDate`, not strings |
| `FAIL_ON_UNKNOWN_PROPERTIES` | Contract changes are noticed. Turn it off where the API may add fields freely |

For quick checks, read a tree instead: `Json.tree(text).path("error").asText()` (used for error messages in `OrderApiSteps`).

## Compare money correctly

```java
assertThat(line.lineTotal())
        .isEqualByComparingTo(line.unitPrice().multiply(BigDecimal.valueOf(line.quantity())));
```

`isEqualByComparingTo` uses `compareTo`, so `40.25` equals `40.250`. `equals` would fail because it also compares scale.

## JSON Schema validation

Schemas live in `src/test/resources/schemas/`. Part of `order.schema.json`:

```json
{
  "type": "object",
  "required": ["orderId", "status", "username", "currency", "total", "createdAt", "lines"],
  "additionalProperties": false,
  "properties": {
    "orderId":  { "type": "string", "pattern": "^ORD-[0-9A-F]{8}$" },
    "status":   { "enum": ["CONFIRMED", "CANCELLED"] },
    "total":    { "type": "number", "exclusiveMinimum": 0 },
    "createdAt":{ "type": "string", "format": "date-time" },
    "lines":    { "type": "array", "minItems": 1, "items": { ... } }
  }
}
```

`json/SchemaValidator.java` validates with networknt and reports **every** violation at once:

```java
Set<ValidationMessage> errors = schema(schemaName).validate(Json.tree(json));
if (!errors.isEmpty()) {
    throw new AssertionError("Response does not match schema '" + schemaName + "':\n  " + ...);
}
```

`ShopApi` validates every order and product-search response before mapping it, so **every** API test checks the contract for free.

**Schema validation proves structure, not correctness.** A response can match the schema and still have the wrong total. That's why the scenario also checks values (`the order total is "40.25"`, line totals = quantity × price) and the database.

## A real dependency problem found while building this

networknt's validator declares YAML support as a dependency. Because only JSON schemas were used, the first attempt excluded the YAML libraries from the build. Every schema check then failed with:

```
NoClassDefFound Could not initialize class com.k11.pwj.json.SchemaValidator
    at com.networknt.schema.JsonSchemaFactory.getInstance(JsonSchemaFactory.java:287)
```

`JsonSchemaFactory` needs the YAML classes even for JSON schemas. The fix keeps them and pins their versions in `<dependencyManagement>`. Lesson: **exclusions are an assumption; prove them with a test run**.

## Review questions

**How do you parse JSON in Java?**
Jackson: `readValue` into records/POJOs for typed tests, `readTree` for quick checks. Configure BigDecimal for decimals and the Java time module for dates.

**How do you validate a JSON schema in Java?**
Keep a JSON Schema file per response and validate with a library (networknt, or REST Assured's `matchesJsonSchemaInClasspath`), reporting all violations.

**What does `additionalProperties: false` do?**
Rejects fields that aren't defined in the schema, so unexpected fields (possible data leaks or contract drift) fail the test.

**Is a schema check enough?**
No. It checks structure and types, not business values. Always assert values too.

Next: [6. Cucumber BDD and data-driven tests](06-cucumber-bdd.md)
