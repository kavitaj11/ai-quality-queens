import re
from decimal import Decimal

from playwright.sync_api import Page, expect


def money(text: str) -> Decimal:
    """Compare money as Decimal, never float."""
    return Decimal(text.replace("$", "").replace(",", "").strip())


class ProductPage:
    def __init__(self, page: Page):
        self.page = page
        self.title = page.get_by_test_id("product-title")
        self.price = page.get_by_test_id("product-detail-price")
        self.quantity = page.get_by_test_id("quantity")
        self.add_button = page.get_by_role("button", name="Add to cart")

    def open(self, product_id: int) -> "ProductPage":
        self.page.goto(f"/products/{product_id}")
        return self

    def unit_price(self) -> Decimal:
        return money(self.price.inner_text())

    def add_to_cart(self, quantity: int = 1):
        from pages.cart_page import CartPage

        self.quantity.select_option(str(quantity))   # native <select>
        self.add_button.click()
        expect(self.page).to_have_url(re.compile(r"/cart\?added=1$"))
        return CartPage(self.page)
