# Page Object Model in Playwright

[07 · Module topics](../../modules/07-playwright.md) · [Curriculum index](../../README.md)

**Category** · TEST DESIGN

POM in Playwright is leaner than Selenium's: locators are lazy (defined once in the constructor, resolved at action time, auto-waiting built in), so you don't need waits inside page objects. Keep methods user-intent-level, return the next page from navigation, and pass the page object in via a fixture so tests never construct it.

## Worked reference

````text
# pages/login_page.py — same shape in Python
from playwright.sync_api import Page, expect

class LoginPage:
    def __init__(self, page: Page):
        self.page = page
        self.email = page.get_by_label("Email")
        self.password = page.get_by_label("Password")
        self.submit = page.get_by_role("button", name="Log in")

    def goto(self):
        self.page.goto("/login")

    def login(self, email: str, pwd: str) -> "DashboardPage":
        self.email.fill(email)
        self.password.fill(pwd)
        self.submit.click()
        return DashboardPage(self.page)     # next page back

    def expect_error(self, msg: str):
        expect(self.page.get_by_role("alert")).to_contain_text(msg)

# conftest.py — inject it, tests never construct pages
import pytest
@pytest.fixture
def login_page(page):
    lp = LoginPage(page)
    lp.goto()
    return lp
````

## Exercise

Build LoginPage + DashboardPage where login() returns DashboardPage, then inject LoginPage through a fixture so tests receive it ready-made.
