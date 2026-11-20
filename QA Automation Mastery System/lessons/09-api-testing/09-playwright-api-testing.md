# Playwright API testing

[09 · Module topics](../../modules/09-api-testing.md) · [Curriculum index](../../README.md)

**Category** · API TEST FUNDAMENTALS

APIRequestContext gives you API tests in the same repo, runner, and report as your UI suite — and lets UI tests seed data via API instead of clicking through setup.

## Worked reference

````text
def test_order_lifecycle(playwright):
    request = playwright.request.new_context(
        base_url="https://api.shop.dev")

    create = request.post("/api/orders",
        data={"sku": "A-100", "qty": 2})
    assert create.status == 201
    order_id = create.json()["id"]

    get = request.get(f"/api/orders/{order_id}")
    assert get.json()["qty"] == 2
````

## Exercise

Rewrite a slow UI setup (create account + add 3 items) as API calls, then run the UI assertion on top.
