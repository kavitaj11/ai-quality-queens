# Test strategy for distributed systems

[12 · Module topics](../../modules/12-microservices-testing.md) · [Curriculum index](../../README.md)

**Category** · DISTRIBUTED SYSTEMS FUNDAMENTALS

You cannot rely on end-to-end tests through the UI for a mesh — too slow, too flaky. The honeycomb/diamond model flips the pyramid's top: heavy on integration + contract tests per service, a thin layer of E2E across critical journeys. Test each service in isolation, verify the edges with contracts, and reserve full-stack E2E for a few money paths.

## Worked reference

````text
TEST STRATEGY — THE HONEYCOMB / DIAMOND

  thin ▲ E2E across the mesh — a FEW critical journeys only
       │  (slow, flaky, expensive; money paths like checkout)
  wide ███ INTEGRATION + CONTRACT — the bulk of the value
       ███ • each service vs its real DB/deps (Testcontainers)
       ███ • CONSUMER-DRIVEN CONTRACTS (Pact) verify every edge
       ███ • async: publish/consume real events
  base ▲ UNIT / COMPONENT — per service, deps mocked (fast)

CORE PRINCIPLES
  1. test each service IN ISOLATION (own DB, virtualized deps)
  2. verify EDGES with contracts, not full E2E
     consumer defines expectations -> provider verifies them
     -> catches breaking API changes WITHOUT a shared env
  3. test ASYNC explicitly: emit event -> assert consumer effect
  4. shift-left: contract + integration in each service's CI
  5. shift-right: monitor prod (synthetics, tracing, SLOs)

WHY NOT ALL E2E?
  N services -> combinatorial paths, shared flaky env, slow
  feedback. Contracts give E2E-level confidence at unit speed.
````

## Exercise

For the checkout flow, assign each check to a layer: cart-service unit, orders↔payment contract, order-created event consumed, and one full checkout E2E.
