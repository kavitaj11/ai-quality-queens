# Microservices architecture

[12 · Module topics](../../modules/12-microservices-testing.md) · [Curriculum index](../../README.md)

**Category** · DISTRIBUTED SYSTEMS FUNDAMENTALS

A microservices app is many small, independently deployable services owning their own data, talking over sync (REST/gRPC) and async (message queues/events) channels, behind an API gateway, often on Kubernetes. Understanding the topology — who calls whom, sync vs async, where state lives — tells you where to put test seams and what can break independently.

## Worked reference

````text
MICROSERVICES ARCHITECTURE

                       ┌── API GATEWAY ──┐   (auth, routing,
     CLIENT ──────────▶│  rate-limit,     │    aggregation)
                       └────────┬─────────┘
            ┌───────────┬───────┼────────┬────────────┐
            ▼           ▼       ▼        ▼            ▼
         [ Auth ]   [ Cart ] [Orders][Payment]  [Inventory]
            │           │       │        │            │
         own DB      own DB   own DB   own DB       own DB
                                 │
                        publishes "OrderPlaced"
                                 ▼
                    ┌──── MESSAGE BUS (Kafka/Rabbit) ────┐
                    ▼                                     ▼
              [Notification]                        [Analytics]

  SYNC   REST / gRPC — request/response (Orders -> Payment)
  ASYNC  events / queues — fire-and-forget (OrderPlaced -> …)
  DATA   database-per-service (no shared DB!) -> eventual consistency
  RUNS   containers on Kubernetes; service discovery; sidecars/mesh

TESTABILITY IMPLICATIONS
  each service testable in isolation (own DB, mock its deps) ·
  contracts govern the edges · async needs event-aware tests
````

## Exercise

Draw the service map for an e-commerce checkout (auth, cart, orders, payment, inventory, notification) and mark each edge sync or async.
