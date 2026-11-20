# CDP & WebDriver BiDi

[06 · Module topics](../../modules/06-selenium.md) · [Curriculum index](../../README.md)

**Category** · BEYOND THE BASICS

Selenium 4's window into the modern protocols. Use CDP (Chromium-only) or the cross-browser WebDriver BiDi to do what classic request/response WebDriver can't: stream console messages and network events, throttle the network, override geolocation, and fail a test the instant a console error fires. This is the code behind the Foundations 'WebDriver, W3C & new standards' overview.

## Worked reference

````text
# Selenium 4: W3C WebDriver by default,
# WebDriver BiDi as opt-in
from selenium import webdriver

opts = webdriver.ChromeOptions()
opts.enable_bidi = True              # BiDi channel
driver = webdriver.Chrome(options=opts)

# BiDi: subscribe to console messages (standard,
# works across browsers — not Chromium-only)
errors = []
driver.script.add_console_message_handler(
    lambda msg: errors.append(msg))

driver.get("https://shop.dev")
assert not errors, f"console errors: {errors}"
````

## Exercise

Enable BiDi (or a CDP session), subscribe to console messages, and fail the test if any console error appears during a full user flow.
