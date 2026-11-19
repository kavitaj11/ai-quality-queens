import re
from decimal import Decimal

import pytest
from playwright.sync_api import BrowserContext, Page, expect

from conftest import PASSWORD
from pages.cart_page import CartPage
from pages.login_page import LoginPage


@pytest.mark.smoke
def test_customer_buys_a_product_end_to_end(page: Page):
    """The whole journey through the UI. Keep only a few tests like this one."""
    product = LoginPage(page).open().login_as("e2e-user", PASSWORD).search("mug").open_product("Travel Mug")
    unit_price = product.unit_price()

    cart = product.add_to_cart(quantity=2)
    expect(cart.toast).to_have_text("Added to cart")
    expect(cart.row("Travel Mug").get_by_test_id("cart-item-qty")).to_have_text("2")
    assert cart.total_amount() == unit_price * 2

    order = cart.checkout()
    expect(order.status).to_have_text("CONFIRMED")
    expect(order.order_id).to_have_text(re.compile(r"^ORD-[0-9A-F]{8}$"))
    expect(order.total).to_have_text("$36.00")


@pytest.mark.regression
def test_checkout_of_a_cart_built_through_the_api(context: BrowserContext, logged_in_page: Page):
    """Setup through the API (fast and stable), then test only checkout in the UI."""
    for product_id, qty in [(1001, 2), (1006, 1)]:          # Blue Mug 2 x 12.50, Eco Bottle 1 x 15.25
        response = context.request.post("/api/cart", data={"productId": product_id, "quantity": qty})
        expect(response).to_be_ok()

    cart = CartPage(logged_in_page).open()
    expect(cart.rows).to_have_count(2)
    assert cart.total_amount() == Decimal("40.25")

    order = cart.checkout()
    expect(order.total).to_have_text("$40.25")
    expect(CartPage(logged_in_page).open().empty).to_be_visible()
