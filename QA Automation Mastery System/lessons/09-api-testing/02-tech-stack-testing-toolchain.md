# Tech stack & testing toolchain

[09 · Module topics](../../modules/09-api-testing.md) · [Curriculum index](../../README.md)

**Category** · API TEST FUNDAMENTALS

The tools that make API testing fast, by concern: HTTP clients & assertion libraries per language, contract/schema tooling, mocking/virtualization, exploration & collections, performance, and CI. Pick one per row for your stack and standardize on it.

## Worked reference

````text
API TESTING TECH STACK & TOOLCHAIN

HTTP CLIENT + ASSERTIONS (by language)
  Python   requests · httpx · pytest + jsonschema · schemathesis
  JS/TS    fetch · axios · supertest · Playwright request · Ajv/zod
  Java     REST Assured + Hamcrest · OkHttp
  cross    Karate (BDD-style API DSL)

CONTRACT / SCHEMA
  OpenAPI/Swagger · JSON Schema · Pact (consumer-driven) ·
  Spring Cloud Contract · Protobuf (gRPC) · GraphQL SDL

MOCKING / VIRTUALIZATION
  WireMock · MockServer · Mountebank · Prism (mock from OpenAPI) ·
  responses / nock / msw (in-language)

EXPLORE & COLLECTIONS
  Postman / Newman (CI) · Insomnia · Bruno · curl · HTTPie ·
  grpcurl (gRPC) · SoapUI (SOAP)

PERFORMANCE
  k6 · Locust · JMeter · Gatling

CI / REPORTING
  GitHub Actions / Jenkins · JUnit-XML · Allure · schema-diff
  in the pipeline to catch breaking API changes

RULE: standardize ONE stack so tests are consistent + reviewable.
````

## Exercise

Assemble your API-testing toolchain: client, schema validator, mock server, exploration tool, and CI runner. One line each on why.
