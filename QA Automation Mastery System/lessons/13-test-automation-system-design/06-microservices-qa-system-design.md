# Microservices QA system design

[13 · Module topics](../../modules/13-test-automation-system-design.md) · [Curriculum index](../../README.md)

**Category** · FRAMEWORK BLUEPRINTS

A framework for ONE app tests a thing; a microservices QA system tests an estate: contract suites at every boundary (Pact broker as the source of truth), per-service component tests with virtualized deps, a thin cross-service journey layer, event/async testing helpers, and the environment strategy (minimal real + stubs) baked into the framework itself. The design centers on the dependency graph, not the page object.

## Worked reference

````text
MICROSERVICES QA SYSTEM — testing an estate

  PER-SERVICE (lives in each service's repo)
  ├─ unit + component tests    service REAL, deps
  │                            virtualized (WireMock/
  │                            Testcontainers)
  ├─ consumer contract tests   'what I expect of you'
  ├─ provider verification     'I still honor all
  │                            published contracts'
  └─ emits: results + coverage in the shared format

  CENTRAL (the QA platform team owns)
  ├─ PACT BROKER               contract source of
  │                            truth; can-i-deploy
  │                            gates releases
  ├─ JOURNEY SUITE (thin!)     5-10 cross-service
  │                            user flows, staging —
  │                            NOT a regression suite
  ├─ data + env services       fixtures API · minimal-
  │                            env orchestration
  │                            (real + stubbed mix)
  └─ observability hooks       trace ids injected in
                               every test request →
                               failures link to traces

  ASYNC/EVENT TESTING (first-class, not bolted on)
  publish → poll-with-deadline consumers · schema
  registry checks (Avro/protobuf compat) · outbox/
  idempotency probes (duplicate delivery is normal)

  PIPELINE STAGES
  PR        unit + component + consumer contracts
  merge     provider verification vs broker →
            can-i-deploy?
  staging   journey suite + event probes
  release   smoke + synthetic monitors take over

  THE INVERTED PYRAMID TRAP
  50 E2E journeys across 8 services = permanent red.
  The system PUSHES tests down: contracts catch
  integration drift, components catch logic,
  journeys only prove the wiring.
````

## Exercise

Design the QA system for 8 services (2 teams, 1 shared Kafka bus): what does each service's repo contain, what lives centrally, and what runs at each pipeline stage?
