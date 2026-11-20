# Custom waits & FluentWait

[06 · Module topics](../../modules/06-selenium.md) · [Curriculum index](../../README.md)

**Category** · WEBDRIVER CORE

When no built-in ExpectedCondition fits — waiting on app state, a JS variable, an attribute settling, or an API-backed flag — write your own. An EC is just a function (WebDriver → truthy/element). FluentWait gives you fine control: total timeout, poll interval, and which exceptions to ignore while polling. A custom wait beats a sleep every time.

## Worked reference

````text
from selenium.webdriver.support.ui import WebDriverWait
from selenium.common.exceptions import StaleElementReferenceException

wait = WebDriverWait(driver, 15, poll_frequency=0.5,
    ignored_exceptions=[StaleElementReferenceException])

# 1. Custom condition = any callable taking the driver
def row_count_at_least(n):
    def _cond(drv):
        rows = drv.find_elements(By.CSS_SELECTOR, "tbody tr")
        return rows if len(rows) >= n else False   # truthy = done
    return _cond

rows = wait.until(row_count_at_least(10))

# 2. Wait on a JavaScript / app-state flag
wait.until(lambda d: d.execute_script(
    "return window.app && window.app.ready === true"))

# 3. Wait for the network/spinner to settle
wait.until(lambda d: d.execute_script(
    "return document.readyState") == "complete")

# 4. Wait until an attribute stops changing (settled)
def attribute_stable(locator, attr):
    last = {"v": None}
    def _cond(drv):
        cur = drv.find_element(*locator).get_attribute(attr)
        if cur == last["v"]:
            return True
        last["v"] = cur
        return False
    return _cond

wait.until(attribute_stable((By.ID, "total"), "data-value"))
````

## Exercise

Write a custom wait that polls until a row count stops changing (list finished loading), and a FluentWait that ignores StaleElementReferenceException.
