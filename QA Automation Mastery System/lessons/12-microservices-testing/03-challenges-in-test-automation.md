# Challenges in test automation

[12 · Module topics](../../modules/12-microservices-testing.md) · [Curriculum index](../../README.md)

**Category** · DISTRIBUTED SYSTEMS FUNDAMENTALS

Testing a distributed system is harder than a monolith: you can't spin the whole thing up easily, dependencies are unavailable or flaky, async flows have no obvious 'done', data is spread across services, and failures are intermittent. Knowing these challenges is what drives the strategy (contract tests, service virtualization, test containers) in the next topic.

## Worked reference

````text
CHALLENGES IN MICROSERVICES TEST AUTOMATION

ENVIRONMENT
  • can't run 40 services + infra on a laptop
  • full E2E envs are slow, shared, and flaky
  -> Testcontainers, ephemeral envs, service virtualization

DEPENDENCIES
  • a service you call is down / rate-limited / changing
  • third-party APIs cost money & have quotas
  -> mocks, stubs, WireMock, contract tests (Pact)

ASYNC & TIMING
  • event-driven flows have no synchronous "response"
  • eventual consistency -> read-after-write is stale
  -> poll with timeout, consume from the queue, await events

DATA
  • state spread across per-service DBs; no single source
  • seeding + cleanup across services is complex
  -> API-based setup/teardown, per-test data, DB validation

FAILURE MODES
  • partial + intermittent failures; hard to reproduce
  • network faults, retries, duplicate messages
  -> chaos/fault injection, idempotency tests, tracing

DEBUGGING
  • a failure spans many services & logs
  -> distributed tracing (OpenTelemetry, Jaeger) + correlation IDs
````

## Exercise

For your system, name the single hardest thing to test and which technique (contract test, virtualization, containers, polling) most reduces that pain.
