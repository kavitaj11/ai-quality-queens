# Advanced Selenium features

[06 · Module topics](../../modules/06-selenium.md) · [Curriculum index](../../README.md)

**Category** · BEYOND THE BASICS

The Selenium 4+ capabilities that separate senior SDETs: relative (friendly) locators, full-page and element screenshots, Chrome DevTools Protocol (network stubbing, console capture, geolocation, auth), BiDi log listeners, browser context isolation, print-to-PDF, file uploads, and shadow DOM traversal. These handle the cases plain findElement can't.

## Worked reference

````text
from selenium.webdriver.support.relative_locator import locate_with
from selenium.webdriver.common.by import By

# ── RELATIVE (friendly) LOCATORS — position-based ──
pwd = driver.find_element(locate_with(By.TAG_NAME, "input")
    .below({By.ID: "email"}).above({By.ID: "submit"}))
driver.find_element(locate_with(By.TAG_NAME, "button")
    .near({By.ID: "email"}).to_right_of({By.ID: "logo"}))

# ── ELEMENT & FULL-PAGE SCREENSHOTS ──
driver.find_element(By.ID, "chart").screenshot("chart.png")
driver.save_screenshot("page.png")

# ── CDP: stub network, set gelocation, capture console ──
driver.execute_cdp_cmd("Network.enable", {})
driver.execute_cdp_cmd("Network.setBlockedURLs",
    {"urls": ["*.png", "*/analytics/*"]})      # block noise
driver.execute_cdp_cmd("Emulation.setGeolocationOverride",
    {"latitude": 40.7, "longitude": -74.0, "accuracy": 1})
driver.execute_cdp_cmd("Network.setUserAgentOverride",
    {"userAgent": "QA-Bot/1.0"})

# ── PRINT TO PDF (headless) ──
pdf = driver.print_page()      # base64 PDF of the page

# ── SHADOW DOM ──
host = driver.find_element(By.CSS_SELECTOR, "settings-panel")
root = host.shadow_root
root.find_element(By.CSS_SELECTOR, "button.save").click()

# ── FILE UPLOAD (send the path to the hidden input) ──
driver.find_element(By.CSS_SELECTOR,
    "input[type=file]").send_keys("/tmp/avatar.png")
````

## Exercise

Use CDP to stub a slow API response, capture an element screenshot on assertion failure, and pierce a shadow root to click a nested button.
