# Listeners for reporting

[06 · Module topics](../../modules/06-selenium.md) · [Curriculum index](../../README.md)

**Category** · BEYOND THE BASICS

Selenium's event listeners let you hook into every command without touching test code — perfect for cross-cutting concerns like logging every action and grabbing a screenshot the instant anything fails. WebDriver has EventFiringDecorator/WebDriverListener; your test runner (TestNG ITestListener, pytest hooks) covers pass/fail reporting. Together they feed Allure/ExtentReports.

## Worked reference

````text
# ── WebDriver event listener (wraps the driver) ──
from selenium.webdriver.support.events import (
    EventFiringWebDriver, AbstractEventListener)

class LoggingListener(AbstractEventListener):
    def before_click(self, element, driver):
        print("about to click", element)
    def after_navigate_to(self, url, driver):
        print("navigated to", url)
    def on_exception(self, exception, driver):
        driver.save_screenshot("error.png")     # capture on any error

driver = EventFiringWebDriver(webdriver.Chrome(), LoggingListener())

# ── pytest hook: screenshot + attach to report on failure ──
import pytest, allure
@pytest.hookimpl(hookwrapper=True)
def pytest_runtest_makereport(item, call):
    outcome = yield
    report = outcome.get_result()
    if report.when == "call" and report.failed:
        drv = item.funcargs.get("driver")
        if drv:
            allure.attach(drv.get_screenshot_as_png(),
                name="failure",
                attachment_type=allure.attachment_type.PNG)
````

## Exercise

Add a WebDriver listener that logs before every click and a TestNG/pytest listener that attaches a screenshot to the report on failure.
