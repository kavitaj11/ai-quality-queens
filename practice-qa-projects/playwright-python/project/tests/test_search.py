import pytest
from playwright.sync_api import Page, expect

from pages.products_page import ProductsPage


@pytest.mark.smoke
def test_search_returns_matching_products(logged_in_page: Page):
    products = ProductsPage(logged_in_page).open().search("lamp")

    # to_have_text with a list checks every matching element, in order.
    expect(products.names).to_have_text(["Desk Lamp", "Smart Lamp"])
    expect(products.result_count).to_have_text("2 result(s)")


@pytest.mark.regression
@pytest.mark.parametrize("term, expected_count", [("mug", 2), ("OFFICE", 3), ("bottle", 1)])
def test_search_counts(logged_in_page: Page, term, expected_count):
    products = ProductsPage(logged_in_page).open().search(term)

    expect(products.cards).to_have_count(expected_count)


@pytest.mark.regression
def test_search_with_no_match_shows_message(logged_in_page: Page):
    products = ProductsPage(logged_in_page).open().search("unicorn")

    expect(products.no_results).to_be_visible()
    expect(products.cards).to_have_count(0)
