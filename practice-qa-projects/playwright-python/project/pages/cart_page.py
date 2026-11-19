import re
from decimal import Decimal

from playwright.sync_api import Page, expect

from pages.product_page import money


class CartPage:
    def __init__(self, page: Page):
        self.page = page
        self.toast = page.get_by_role("status")
        self.rows = page.get_by_test_id("cart-row")
        self.total = page.get_by_test_id("cart-total")
        self.empty = page.get_by_test_id("empty-cart")
        self.checkout_button = page.get_by_role("button", name="Checkout")

    def open(self) -> "CartPage":
        self.page.goto("/cart")
        return self

    def row(self, product_name: str):
        return self.rows.filter(has_text=product_name)

    def total_amount(self) -> Decimal:
        return money(self.total.inner_text())

    def checkout(self):
        from pages.order_page import OrderPage

        self.checkout_button.click()
        expect(self.page).to_have_url(re.compile(r"/orders/ORD-"))
        return OrderPage(self.page)
