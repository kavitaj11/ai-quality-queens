# Dev & testing tech stack

[12 · Module topics](../../modules/12-microservices-testing.md) · [Curriculum index](../../README.md)

**Category** · DISTRIBUTED SYSTEMS FUNDAMENTALS

The toolbox that makes distributed testing tractable, by concern: containers & orchestration (Docker, Kubernetes, Testcontainers), contracts (Pact, Spring Cloud Contract), virtualization/mocking (WireMock, Mountebank, MockServer), messaging (Kafka/RabbitMQ + their test kits), API (REST Assured, Postman/Newman, gRPC), and observability (OpenTelemetry, Jaeger, Prometheus, Grafana).

## Worked reference

````text
MICROSERVICES DEV & TEST TECH STACK

CONTAINERS & ORCHESTRATION
  Docker · docker-compose · Kubernetes · Helm
  Testcontainers — real DB/queue/service per test, disposable

CONTRACT TESTING
  Pact (consumer-driven) · Spring Cloud Contract
  a broker shares + verifies contracts across teams

SERVICE VIRTUALIZATION / MOCKING
  WireMock · MockServer · Mountebank · Hoverfly
  stand in for slow/paid/unavailable dependencies

MESSAGING / EVENTS
  Kafka · RabbitMQ · NATS
  test kits: embedded brokers, Testcontainers Kafka module

API TESTING
  REST Assured (Java) · requests/httpx (Py) · Playwright request
  Postman/Newman · gRPC (grpcurl, buf) · schema: OpenAPI, Protobuf

OBSERVABILITY (test IN prod / debug failures)
  OpenTelemetry (traces) · Jaeger/Zipkin · Prometheus (metrics)
  Grafana (dashboards) · correlation IDs across services

CHAOS / RESILIENCE
  Chaos Monkey · Gremlin · Litmus · toxiproxy (network faults)

CI/CD
  Jenkins · GitHub Actions · Argo CD · ephemeral preview envs
````

## Exercise

Pick one tool from each category for your stack and write one sentence on what it lets you test that you couldn't before.
