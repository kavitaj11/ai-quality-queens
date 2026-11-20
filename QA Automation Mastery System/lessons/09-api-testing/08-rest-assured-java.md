# REST Assured (Java)

[09 · Module topics](../../modules/09-api-testing.md) · [Curriculum index](../../README.md)

**Category** · API TEST FUNDAMENTALS

The Java-stack standard for API testing: a given/when/then fluent DSL over Hamcrest matchers. This tour covers the whole surface — reusable RequestSpecification/ResponseSpecification, every kind of parameter, JSON-path and schema response validation, extraction + POJO deserialization, and auth. A frequent live-coding interview exercise.

## Worked reference

````text
import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.*;
import static org.hamcrest.Matchers.*;
import io.restassured.builder.*;
import io.restassured.specification.*;
import io.restassured.http.ContentType;

// ─── 1. REQUEST SPECIFICATION — build once, reuse everywhere ───
RequestSpecification apiSpec = new RequestSpecBuilder()
    .setBaseUri("https://api.shop.dev")
    .setBasePath("/v1")
    .setContentType(ContentType.JSON)
    .addHeader("Authorization", "Bearer " + token)
    .addFilter(new RequestLoggingFilter())    // log on demand
    .build();

// ─── 2. RESPONSE SPECIFICATION — reusable expectations ───
ResponseSpecification okJson = new ResponseSpecBuilder()
    .expectStatusCode(200)
    .expectContentType(ContentType.JSON)
    .expectResponseTime(lessThan(2000L))       // SLA gate
    .build();

// ─── 3. PARAMETERS — path, query, form ───
given().spec(apiSpec)
    .pathParam("id", 42)
    .queryParam("expand", "items")
    .queryParam("currency", "USD")
.when()
    .get("/orders/{id}")
.then()
    .spec(okJson);

// ─── 4. CREATE + full RESPONSE VALIDATION ───
given().spec(apiSpec)
    .body(Map.of("sku", "A-100", "qty", 2))
.when()
    .post("/orders")
.then()
    .statusCode(201)
    .header("Location", containsString("/orders/"))
    .contentType(ContentType.JSON)
    // JSON-path body assertions (Hamcrest matchers):
    .body("status", equalTo("pending"))
    .body("id", notNullValue())
    .body("total", greaterThan(0f))
    .body("items", hasSize(1))
    .body("items[0].sku", equalTo("A-100"))
    .body("items.sku", hasItems("A-100"))       // collection
    // JSON SCHEMA validation (whole shape at once):
    .body(matchesJsonSchemaInClasspath("order-schema.json"));

// ─── 5. EXTRACT — pull values / deserialize to a POJO ───
String id = given().spec(apiSpec).body(newOrder)
    .when().post("/orders")
    .then().statusCode(201)
    .extract().path("id");                       // single value

Order order = given().spec(apiSpec)
    .when().get("/orders/" + id)
    .then().statusCode(200)
    .extract().as(Order.class);                  // JSON -> POJO
assertEquals(order.status(), "pending");

// ─── 6. AUTH SCHEMES ───
given().auth().oauth2(token);                    // Bearer
given().auth().preemptive().basic("user", "pw"); // Basic
given().header("X-Api-Key", apiKey);             // API key

// ─── 7. UPDATE + DELETE (idempotent) ───
given().spec(apiSpec).body(Map.of("qty", 5))
    .when().put("/orders/" + id)
    .then().statusCode(200).body("qty", equalTo(5));

given().spec(apiSpec)
    .when().delete("/orders/" + id)
    .then().statusCode(204);

// ─── 8. Root-config default spec + logging on failure ───
RestAssured.requestSpecification = apiSpec;      // global default
given().log().ifValidationFails()
    .when().get("/orders/" + id)
    .then().log().ifError().statusCode(404);
````

## Exercise

Build a shared RequestSpecification (base URI + auth + content type), then write CRUD tests that validate status, headers, body via JSON-path, and deserialize the response into a POJO.
