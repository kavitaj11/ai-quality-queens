# Toolchain for microservices testing

[12 · Module topics](../../modules/12-microservices-testing.md) · [Curriculum index](../../README.md)

**Category** · MICROSERVICES TESTING

The hands-on tools that make each layer of the microservices honeycomb executable: Testcontainers for real disposable dependencies, Pact for consumer-driven contracts, WireMock for virtualizing services you don't control, and embedded/containerized Kafka for async event tests. This is the code behind the strategy — pick one per layer and wire it into each service's CI.

## Worked reference

````text
# ── Pact: consumer-driven contract (Python consumer side) ──
from pact import Consumer, Provider

pact = Consumer("orders").has_pact_with(Provider("payments"))

def test_charge_contract():
    (pact
     .given("a valid order")
     .upon_receiving("a charge request")
     .with_request("POST", "/charges",
                   body={"orderId": "42", "amount": 4990})
     .will_respond_with(201, body={"status": "captured"}))
    with pact:
        result = PaymentClient(pact.uri).charge("42", 4990)
        assert result["status"] == "captured"
    # pact file is published to a broker; the PROVIDER's CI
    # verifies it -> breaking API changes fail BEFORE deploy

# ── testcontainers-python: real Redis/Kafka/DB per test ──
# from testcontainers.postgres import PostgresContainer
# with PostgresContainer("postgres:16") as pg:
#     run_migrations(pg.get_connection_url())
````

## Exercise

Stand up a service test that uses Testcontainers for its DB, WireMock for one downstream dependency, and a Pact contract for its API consumer.
