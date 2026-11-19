import re

import pytest
from playwright.sync_api import Page, expect

from conftest import PASSWORD
from pages.login_page import LoginPage


@pytest.mark.smoke
def test_valid_user_lands_on_products(page: Page):
    products = LoginPage(page).open().login_as("alice", PASSWORD)

    expect(products.welcome).to_have_text("Hi, alice")
    expect(page).to_have_title(re.compile("Products"))


@pytest.mark.regression
@pytest.mark.parametrize(
    "username, password, message",
    [
        ("alice", "wrong-password", "Invalid username or password."),
        ("", PASSWORD, "Username and password are required."),
        ("alice", "", "Username and password are required."),
        ("locked", PASSWORD, "This account is locked. Contact support."),
    ],
    ids=["wrong-password", "no-username", "no-password", "locked-account"],
)
def test_invalid_login_shows_error(page: Page, username, password, message):
    login = LoginPage(page).open()
    login.login(username, password)

    # expect() retries until the text matches or the timeout passes: no explicit wait needed.
    expect(login.error).to_have_text(message)
    expect(page).to_have_url(re.compile(r"/login$"))


@pytest.mark.regression
def test_pages_require_login(page: Page):
    page.goto("/cart")
    expect(page).to_have_url(re.compile(r"/login$"))
