# API commands

[07 · Module topics](../../modules/07-playwright.md) · [Curriculum index](../../README.md)

**Category** · PLAYWRIGHT CORE

Playwright ships a full HTTP client (the request fixture / APIRequestContext) — no extra library needed. Use it two ways: pure API tests (fast CRUD + status/JSON assertions), and as setup/teardown for UI tests (seed data via API, then drive the UI). It shares cookies with the browser context, so an API login can authenticate the UI.

## Worked reference

````text
# the request fixture is injected like the page fixture
def test_order_lifecycle(playwright):
    api = playwright.request.new_context(
        base_url="https://api.shop.dev",
        extra_http_headers={"Authorization": f"Bearer {token}"})

    # ── CREATE ──
    r = api.post("/orders", data={"sku": "A-100", "qty": 2})
    assert r.status == 201
    order = r.json()

    # ── READ + assert body ──
    r = api.get(f"/orders/{order['id']}")
    assert r.ok
    assert r.json()["status"] == "pending"

    # ── UPDATE / DELETE ──
    api.put(f"/orders/{order['id']}", data={"qty": 5})
    assert api.delete(f"/orders/{order['id']}").status == 204
    api.dispose()

# ── API AS SETUP FOR A UI TEST (seed fast, then drive UI) ──
def test_cart_shows_seeded_item(page, playwright):
    api = playwright.request.new_context(base_url="https://api.shop.dev")
    api.post("/cart", data={"sku": "A-100"})    # seed via API
    page.goto("/cart")                          # verify via UI
    expect(page.get_by_test_id("cart-count")).to_have_text("1")
````

## Exercise

Write an API-only order-lifecycle test (POST/GET/PUT/DELETE with status + body assertions), then use an API POST to seed a product before a UI test.
