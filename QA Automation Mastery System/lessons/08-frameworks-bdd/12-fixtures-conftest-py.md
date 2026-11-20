# Fixtures & conftest.py

[08 · Module topics](../../modules/08-frameworks-bdd.md) · [Curriculum index](../../README.md)

**Category** · PYTEST DEEP DIVE

Fixtures are dependency injection for tests: scoped setup/teardown with yield, composed and overridden per directory via conftest.py. This replaces every setUp/tearDown pattern you know.

## Worked reference

````text
import pytest
from selenium import webdriver

@pytest.fixture(scope="session")
def browser():
    driver = webdriver.Chrome()
    yield driver
    driver.quit()

@pytest.fixture
def logged_in(browser):
    LoginPage(browser).login("qa", "s3cret")
    yield browser
    browser.delete_all_cookies()
````

## Exercise

Build a session-scoped browser fixture and a function-scoped page fixture; add automatic screenshot-on-failure.
