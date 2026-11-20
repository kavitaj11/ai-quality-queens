# Extending Selenium with wrappers

[06 · Module topics](../../modules/06-selenium.md) · [Curriculum index](../../README.md)

**Category** · BEYOND THE BASICS

Raw Selenium is verbose and repeats itself — mature suites wrap it. A driver wrapper (or a base 'actions' class) centralizes waits, logging, screenshots-on-failure, and retries so page objects call safeClick / safeType instead of raw driver calls. This is how you kill flakiness and duplication in one place; it's also the pattern behind libraries like Selenide.

## Worked reference

````text
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import logging
log = logging.getLogger("ui")

class Driver:
    """Thin wrapper: every action waits, logs, and self-heals."""
    def __init__(self, driver, timeout=10):
        self.d = driver
        self.wait = WebDriverWait(driver, timeout)

    def safe_click(self, locator):
        log.info("click %s", locator)
        el = self.wait.until(EC.element_to_be_clickable(locator))
        try:
            el.click()
        except Exception:                       # overlay/animation
            self.d.execute_script("arguments[0].click();", el)

    def safe_type(self, locator, text):
        log.info("type %r into %s", text, locator)
        el = self.wait.until(EC.visibility_of_element_located(locator))
        el.clear(); el.send_keys(text)

    def text(self, locator):
        return self.wait.until(
            EC.visibility_of_element_located(locator)).text

# Page objects now speak the wrapper, never raw Selenium:
class LoginPage:
    def __init__(self, drv): self.drv = drv
    def login(self, u, p):
        self.drv.safe_type((By.ID, "email"), u)
        self.drv.safe_type((By.ID, "password"), p)
        self.drv.safe_click((By.ID, "submit"))
````

## Exercise

Write a wrapper with safeClick, safeType, and getText that each wait for the element and log the action, then refactor a page object to use only the wrapper.
