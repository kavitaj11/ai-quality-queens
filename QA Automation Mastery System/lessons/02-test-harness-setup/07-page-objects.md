# Page objects

[02 · Module topics](../../modules/02-test-harness-setup.md) · [Curriculum index](../../README.md)

**Category** · LANGUAGE ESSENTIALS

One class per screen, locators in one place, methods that speak the user's language, navigation methods return the next page. The pattern is identical across stacks — only the ceremony changes.

## Worked reference

````text
class LoginPage:
    EMAIL = "#email"
    PASSWORD = "#password"
    SUBMIT = "button[type=submit]"

    def __init__(self, page):
        self.page = page

    def login(self, email, pwd):
        self.page.fill(self.EMAIL, email)
        self.page.fill(self.PASSWORD, pwd)
        self.page.click(self.SUBMIT)
        return DashboardPage(self.page)
````

## Exercise

Build a LoginPage and DashboardPage in your primary language; login() must return the DashboardPage.
