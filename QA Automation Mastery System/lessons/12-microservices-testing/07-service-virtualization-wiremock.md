# Service virtualization (WireMock)

[12 · Module topics](../../modules/12-microservices-testing.md) · [Curriculum index](../../README.md)

**Category** · MICROSERVICES TESTING

Stand-in services with programmable responses, latency, and faults. Test your service's behavior when its dependencies are slow, wrong, or down.

## Worked reference

````text
# responses — Python HTTP mocking
import responses

@responses.activate
def test_checkout_when_payments_down():
    responses.post(
        "https://payments.local/charge",
        status=500,
    )
    result = checkout(cart)
    assert result.status == "retry_scheduled"
````

## Exercise

Virtualize the payments dependency; test your checkout's behavior on timeout, 500, and malformed body.
