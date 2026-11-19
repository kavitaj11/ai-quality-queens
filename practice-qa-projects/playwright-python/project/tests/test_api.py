"""API tests with Playwright's request context: no browser page needed, same tool, same report."""
import pytest
from playwright.sync_api import APIRequestContext, Playwright, expect

from conftest import PASSWORD


@pytest.fixture
def api(playwright: Playwright, base_url: str) -> APIRequestContext:
    ctx = playwright.request.new_context(base_url=base_url)
    yield ctx
    ctx.dispose()


@pytest.mark.api
def test_products_api_filters_by_term(api: APIRequestContext):
    response = api.get("/api/products", params={"q": "lamp"})

    expect(response).to_be_ok()
    body = response.json()
    assert body["count"] == 2
    assert {p["name"] for p in body["items"]} == {"Desk Lamp", "Smart Lamp"}
    assert all({"id", "name", "price", "category"} <= p.keys() for p in body["items"])


@pytest.mark.api
@pytest.mark.parametrize("password, status", [(PASSWORD, 200), ("nope", 401)])
def test_login_api(api: APIRequestContext, password, status):
    response = api.post("/api/login", data={"username": "api-tester", "password": password})
    assert response.status == status


@pytest.mark.api
def test_cart_api_requires_login_and_validates_quantity(api: APIRequestContext):
    assert api.post("/api/cart", data={"productId": 1001, "quantity": 1}).status == 401

    expect(api.post("/api/login", data={"username": "api-tester", "password": PASSWORD})).to_be_ok()
    assert api.post("/api/cart", data={"productId": 1001, "quantity": 9}).status == 400
    assert api.post("/api/cart", data={"productId": 1001, "quantity": 2}).status == 201
