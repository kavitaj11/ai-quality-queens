# Page Object Model

[06 · Module topics](../../modules/06-selenium.md) · [Curriculum index](../../README.md)

**Category** · STRUCTURE & SCALE

One class per page/component, locators private, methods express user intent and return page objects. The single most asked framework-design question in QA interviews.

## Worked reference

````text
class LoginPage:
    URL = "/login"
    _user = (By.ID, "username")
    _pass = (By.ID, "password")
    _submit = (By.CSS_SELECTOR, "button[type=submit]")

    def __init__(self, driver):
        self.driver = driver

    def login(self, user, pwd):
        self.driver.find_element(*self._user).send_keys(user)
        self.driver.find_element(*self._pass).send_keys(pwd)
        self.driver.find_element(*self._submit).click()
        return DashboardPage(self.driver)
````

## Exercise

Refactor a 3-test login suite into POM. Tests should read like English with zero locators in them.
