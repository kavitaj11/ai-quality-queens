# Types of APIs

[09 · Module topics](../../modules/09-api-testing.md) · [Curriculum index](../../README.md)

**Category** · API TEST FUNDAMENTALS

Not all APIs are tested the same way. REST (resource + HTTP verbs, the common case), GraphQL (one endpoint, client-shaped queries), gRPC (binary Protobuf over HTTP/2, service-to-service), SOAP (legacy XML/WSDL), and async/event APIs (webhooks, WebSockets, message queues). Each has its own contract format, tooling, and failure modes.

## Worked reference

````text
TYPES OF APIs (and how testing differs)

REST  (most common)
  resources + HTTP verbs (GET/POST/PUT/PATCH/DELETE); JSON
  contract: OpenAPI/Swagger · test: status, body, schema, auth

GraphQL
  ONE endpoint; client asks for exactly the fields it needs
  contract: GraphQL schema (SDL) · test: query/mutation shape,
  partial errors (200 with an "errors" array!), over/under-fetch

gRPC
  binary Protobuf over HTTP/2; strongly typed; service-to-service
  contract: .proto files · test: grpcurl/buf, status codes,
  streaming (unary/server/client/bidi)

SOAP (legacy)
  XML envelopes described by WSDL; enterprise systems
  test: XML body/XPath, SOAP faults · tools: SoapUI

ASYNC / EVENT-DRIVEN
  webhooks (they call YOU), WebSockets (duplex), message queues
  test: consume the event, verify payload + effect, idempotency,
  ordering, at-least-once delivery (poll, don't assert instantly)

WHAT'S COMMON TO ALL
  validate status/errors, the payload SHAPE (schema/proto), auth,
  edge cases, and non-functional (latency, rate limits).
````

## Exercise

For a product you know, list which API styles it uses and, for each, name the contract format and one test you'd write.
