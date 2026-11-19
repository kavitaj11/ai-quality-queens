# 5. Network Mocking and Interception

`page.route(pattern, handler)` intercepts every matching request the page makes. The handler decides what happens: answer it yourself (`fulfill`), let it through (`continue_`), change it, or `abort` it. This lets you test states that are hard or impossible to create with a real backend.

All four tests below are in `tests/test_network.py` and pass.

## Test an error state: the API returns 500

```python
def test_search_shows_friendly_error_when_api_fails(logged_in_page):
    logged_in_page.route("**/api/products*", lambda route: route.fulfill(status=500, body="boom"))

    products = ProductsPage(logged_in_page).open()

    expect(products.search_error).to_have_text("Could not load products. Please try again.")
    expect(products.cards).to_have_count(0)
```

Getting a real backend to fail on demand is hard. Here it takes one line, so the error message is tested on every run.

## Control the data: mocked response

```python
fake = {"items": [{"id": 9001, "name": "Mocked Teapot", "price": 1.5, "category": "Kitchen"}], "count": 1}
logged_in_page.route("**/api/products*", lambda route: route.fulfill(
    status=200, content_type="application/json", body=json.dumps(fake)))

products = ProductsPage(logged_in_page).open()
expect(products.names).to_have_text(["Mocked Teapot"])
expect(products.cards.get_by_test_id("product-price")).to_have_text("$1.50")
```

The test checks how the **front end** renders data (price formatting: `1.5` → `$1.50`) independently of what the backend currently returns.

## Test a loading state: hold the response

```python
held = []
logged_in_page.route("**/api/products*", lambda route: held.append(route))   # don't answer yet

logged_in_page.goto("/products")
expect(products.spinner).to_be_visible()     # guaranteed: the response hasn't arrived
expect(products.results).to_be_empty()

held[0].continue_()                          # release the real request
products.wait_until_loaded()
expect(products.spinner).to_be_hidden()
```

Without interception, checking the spinner is a race against a fast server. Holding the request removes the race.

## Assert that the page made the right call

```python
with logged_in_page.expect_request("**/api/products?q=lamp") as request_info:
    logged_in_page.goto("/products?q=lamp")
assert request_info.value.method == "GET"
```

`page.expect_response()` works the same way and gives you the response body too.

## When to mock and when not to

| Mock when | Don't mock when |
|---|---|
| Testing front-end handling of errors, empty data, edge cases | Proving the real system works end to end |
| A third-party service is slow, costly or unavailable in test | The integration with that service is what you're testing |
| You need a deterministic loading state | You'd be mocking the very thing under test |

Keep at least some tests against the real backend, and keep mocked data matching the real API contract; otherwise mocks quietly drift away from reality.

## Review questions

**How do you mock an API response in Playwright?**
`page.route("**/api/...", lambda route: route.fulfill(status=..., body=..., content_type=...))`. Use `route.continue_()` to pass through (optionally modified) and `route.abort()` to simulate a network failure.

**How would you test a loading spinner reliably?**
Intercept the API, hold the request until the spinner is asserted, then release it with `route.continue_()`.

**What are the risks of mocking?**
Mocks can drift from the real API and give false confidence. Keep contract checks or some real end-to-end tests alongside.

**How do you wait for a specific API call?**
`with page.expect_response("**/api/products*") as info: ...`, then inspect `info.value`.

Next: [6. Debugging, tracing, parallel runs and CI](06-debugging-tracing-parallel-and-ci.md)
