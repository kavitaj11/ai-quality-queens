# Test runners

[06 · Module topics](../../modules/06-selenium.md) · [Curriculum index](../../README.md)

**Category** · STRUCTURE & SCALE

Selenium only drives the browser — a test runner discovers tests, runs them, manages setup/teardown, asserts, retries, and reports. Selenium pairs with each language's standard runner: pytest (Python), TestNG or JUnit 5 (Java), Mocha/Jest + a runner for JS. Know what the runner owns vs what Selenium owns; interviewers ask exactly this.

## Worked reference

````text
# pytest — the Python standard runner
import pytest
from selenium import webdriver

@pytest.fixture
def driver():                       # setup + teardown in one place
    d = webdriver.Chrome()
    yield d                         # test runs here
    d.quit()                        # always cleans up

@pytest.mark.smoke                  # tag/group tests -> select later
def test_login(driver):
    driver.get("https://shop.dev/login")
    assert "Login" in driver.title

# What the RUNNER owns (not Selenium):
#   discovery   test_*.py / test_* functions
#   lifecycle   fixtures (setup/teardown, scopes)
#   selection   -m smoke   -k "login"   --lf
#   parallelism pytest-xdist  ->  -n auto
#   retries     pytest-rerunfailures  ->  --reruns 2
#   reporting   --junitxml / --alluredir / pytest-html
#
#   pytest -m smoke -n auto --alluredir=allure-results
````

## Exercise

Take a bare Selenium script and put it under a runner: add fixtures/annotations for driver setup+teardown, group into a suite, and produce a report.
