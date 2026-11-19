"""Network interception: make the backend do exactly what the test needs.

These tests show behaviour that is hard or impossible to trigger with the real backend:
an API error, a specific set of products, and a very slow response.
"""
import json

import pytest
from playwright.sync_api import Page, Route, expect

from pages.products_page import ProductsPage


@pytest.mark.network
def test_search_shows_friendly_error_when_api_fails(logged_in_page: Page):
    logged_in_page.route("**/api/products*", lambda route: route.fulfill(status=500, body="boom"))

    products = ProductsPage(logged_in_page).open()

    expect(products.search_error).to_have_text("Could not load products. Please try again.")
    expect(products.cards).to_have_count(0)


@pytest.mark.network
def test_search_renders_mocked_products(logged_in_page: Page):
    fake = {"items": [{"id": 9001, "name": "Mocked Teapot", "price": 1.5, "category": "Kitchen"}], "count": 1}
    logged_in_page.route("**/api/products*", lambda route: route.fulfill(
        status=200, content_type="application/json", body=json.dumps(fake)))

    products = ProductsPage(logged_in_page).open()

    expect(products.names).to_have_text(["Mocked Teapot"])
    expect(products.cards.get_by_test_id("product-price")).to_have_text("$1.50")


@pytest.mark.network
def test_spinner_shows_while_api_is_slow(logged_in_page: Page):
    """Hold the API response until we have checked the loading state, then release it."""
    held: list[Route] = []
    logged_in_page.route("**/api/products*", lambda route: held.append(route))

    logged_in_page.goto("/products")
    products = ProductsPage(logged_in_page)
    expect(products.spinner).to_be_visible()          # still loading: the response is held
    expect(products.results).to_be_empty()

    for _ in range(50):                               # give the route handler time to receive it
        if held:
            break
        logged_in_page.wait_for_timeout(100)
    assert held, "the products API was never called"
    held[0].continue_()                               # now let the real request through
    products.wait_until_loaded()
    expect(products.spinner).to_be_hidden()


@pytest.mark.network
def test_page_calls_the_products_api_with_the_search_term(logged_in_page: Page):
    with logged_in_page.expect_request("**/api/products?q=lamp") as request_info:
        logged_in_page.goto("/products?q=lamp")
    assert request_info.value.method == "GET"
