import re

from playwright.sync_api import Page, expect


class LoginPage:
    def __init__(self, page: Page):
        self.page = page
        self.username = page.get_by_test_id("username")
        self.password = page.get_by_test_id("password")
        # Role-based locator: how a user (and a screen reader) finds the button.
        self.submit = page.get_by_role("button", name="Log in")
        self.error = page.get_by_test_id("login-error")

    def open(self) -> "LoginPage":
        self.page.goto("/login")
        return self

    def login(self, username: str, password: str) -> None:
        self.username.fill(username)
        self.password.fill(password)
        self.submit.click()

    def login_as(self, username: str, password: str):
        from pages.products_page import ProductsPage

        self.login(username, password)
        expect(self.page).to_have_url(re.compile(r"/products$"))   # wait for the new page, not the old one
        return ProductsPage(self.page).wait_until_loaded()
