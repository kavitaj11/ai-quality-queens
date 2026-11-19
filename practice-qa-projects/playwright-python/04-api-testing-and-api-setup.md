# 4. API Testing and API-Based Setup

Playwright includes an HTTP client, `APIRequestContext`, so API tests and UI tests share one tool, one runner and one report.

## Pure API tests

From `tests/test_api.py`:

```python
@pytest.fixture
def api(playwright, base_url):
    ctx = playwright.request.new_context(base_url=base_url)
    yield ctx
    ctx.dispose()

def test_products_api_filters_by_term(api):
    response = api.get("/api/products", params={"q": "lamp"})

    expect(response).to_be_ok()
    body = response.json()
    assert body["count"] == 2
    assert {p["name"] for p in body["items"]} == {"Desk Lamp", "Smart Lamp"}
    assert all({"id", "name", "price", "category"} <= p.keys() for p in body["items"])
```

```python
def test_cart_api_requires_login_and_validates_quantity(api):
    assert api.post("/api/cart", data={"productId": 1001, "quantity": 1}).status == 401   # not logged in
    expect(api.post("/api/login", data={"username": "api-tester", "password": PASSWORD})).to_be_ok()
    assert api.post("/api/cart", data={"productId": 1001, "quantity": 9}).status == 400   # out of range
    assert api.post("/api/cart", data={"productId": 1001, "quantity": 2}).status == 201
```

Passing a dict as `data=` sends JSON. The request context keeps cookies between calls, so the login cookie is sent on the later requests, just like a browser.

What to check in an API test: status code, response schema (required fields and types), business values, authentication and authorization, error handling, and side effects.

## API-based setup for UI tests

The most valuable trick in UI automation: **set up state through the API, and use the UI only for the behaviour under test.**

`context.request` belongs to the browser context and **shares its cookies**. Log in through the API and the page is logged in too:

```python
@pytest.fixture
def logged_in_page(context, page):
    response = context.request.post("/api/login", data={"username": "pw-user", "password": PASSWORD})
    assert response.ok
    return page
```

Then build a cart through the API and test checkout in the UI (`tests/test_checkout.py`):

```python
def test_checkout_of_a_cart_built_through_the_api(context, logged_in_page):
    for product_id, qty in [(1001, 2), (1006, 1)]:
        expect(context.request.post("/api/cart", data={"productId": product_id, "quantity": qty})).to_be_ok()

    cart = CartPage(logged_in_page).open()
    expect(cart.rows).to_have_count(2)
    assert cart.total_amount() == Decimal("40.25")
    ...
```

Measured with `pytest --durations=0`:

| Test | Setup | Test time |
|---|---|---|
| `test_customer_buys_a_product_end_to_end` | Login, search, product page, add to cart, all in the UI | **4.62 s** |
| `test_checkout_of_a_cart_built_through_the_api` | Login and cart through the API | **0.31 s** |

Both test checkout. Keep a handful of full UI journeys; use API setup everywhere else.

## Saving a login: storage state

For apps where login is slow (SSO, MFA in test environments), log in once and reuse the session:

```python
context.storage_state(path="auth/state.json")            # after logging in once
browser.new_context(storage_state="auth/state.json")      # every later test starts logged in
```

Keep state files out of git; they contain session cookies.

## Review questions

**Can Playwright do API testing?**
Yes, with `APIRequestContext` (`playwright.request.new_context()` or `context.request`): GET/POST/PUT/DELETE, JSON bodies, headers, cookies, and `expect(response).to_be_ok()`.

**How do you log in once and reuse it?**
Log in through the API (or the UI once), then use `context.request` cookies or save `storage_state` and create later contexts from it.

**Why set up tests through the API?**
Speed (0.31 s vs 4.62 s here) and stability: fewer UI steps means fewer things that can break for reasons unrelated to what you're testing.

**What do you validate in an API response?**
Status, schema, values, auth, errors, and side effects (for example the cart now contains the item).

Next: [5. Network mocking and interception](05-network-mocking.md)
