# Consumer-driven contracts (Pact)

[09 · Module topics](../../modules/09-api-testing.md) · [Curriculum index](../../README.md)

**Category** · CONTRACTS & SCHEMAS

Consumers publish expectations; providers verify them in CI. Contract tests catch integration breaks without spinning up both services — core to microservices QA.

## Worked reference

````text
# pact-python — consumer side
from pact import Consumer, Provider, Like

pact = Consumer("web").has_pact_with(Provider("orders"))

(pact
 .given("order 42 exists")
 .upon_receiving("a request for order 42")
 .with_request("GET", "/orders/42")
 .will_respond_with(200, body={
     "id": "42", "status": Like("pending")}))
````

## Exercise

Write a Pact between a web consumer and the orders service; break the provider and watch verification fail.
