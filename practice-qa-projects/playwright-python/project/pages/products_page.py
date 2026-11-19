import re
from urllib.parse import quote_plus

from playwright.sync_api import Page, expect


class ProductsPage:
    def __init__(self, page: Page):
        self.page = page
        self.search_input = page.get_by_test_id("search-input")
        self.search_button = page.get_by_test_id("search-button")
        self.spinner = page.get_by_test_id("spinner")
        self.result_count = page.get_by_test_id("result-count")
        self.results = page.get_by_test_id("results")
        self.cards = page.get_by_test_id("product-card")
        self.names = page.get_by_test_id("product-name")
        self.no_results = page.get_by_test_id("no-results")
        self.search_error = page.get_by_test_id("search-error")
        self.welcome = page.get_by_test_id("welcome")

    def open(self) -> "ProductsPage":
        self.page.goto("/products")
        return self.wait_until_loaded()

    def wait_until_loaded(self) -> "ProductsPage":
        """Results come from a slow API call. The results container stays empty until loading
        finishes, then holds cards, a "no results" message or an error. expect() retries until
        that happens, so there is no sleep and no manual polling."""
        expect(self.spinner).to_be_hidden()
        expect(self.results).not_to_be_empty()
        return self

    def search(self, term: str) -> "ProductsPage":
        self.search_input.fill(term)
        self.search_button.click()
        # The old page already shows a result count, so first wait for the NEW page's URL.
        expect(self.page).to_have_url(re.compile(r"\?q=" + re.escape(quote_plus(term))))
        return self.wait_until_loaded()

    def open_product(self, name: str):
        from pages.product_page import ProductPage

        # Filter cards by their text, then click the link inside that card only.
        self.cards.filter(has_text=name).get_by_role("link", name="View").click()
        expect(self.page).to_have_url(re.compile(r"/products/\d+$"))
        return ProductPage(self.page)
