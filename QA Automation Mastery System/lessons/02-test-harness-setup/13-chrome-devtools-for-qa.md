# Chrome DevTools for QA

[02 · Module topics](../../modules/02-test-harness-setup.md) · [Curriculum index](../../README.md)

**Category** · DEVTOOLS & DATA CHECKS

DevTools (F12 / Cmd+Opt+I) is where you build and debug locators and watch traffic. Elements panel: inspect the DOM, right-click a node → Copy → Copy selector / Copy XPath / Copy JS path. Console: test any selector live before writing it into code. Network panel: watch every request, read status/headers/payload, copy as cURL, throttle, and confirm what the UI actually calls. These skills turn 'the test can't find the element' from guesswork into a 30-second check.

## Worked reference

````text
# Workflow: copy from DevTools, harden in code
#
# 1. Elements -> right-click -> Copy selector
#    e.g.  #root > div:nth-child(2) > button   (brittle!)
# 2. Simplify to something stable before committing:
from selenium.webdriver.common.by import By

# brittle (copied verbatim):
driver.find_element(By.CSS_SELECTOR,
    "#root > div:nth-child(2) > button")
# stable (hand-tuned from what you saw):
driver.find_element(By.CSS_SELECTOR, "[data-testid='submit']")

# 3. Selenium DevTools Protocol: capture network in code
#    (selenium 4, Chromium)
driver.execute_cdp_cmd("Network.enable", {})
# read performance/network logs to assert on requests

# TIP: keep DevTools "Preserve log" ON so requests
# survive page navigation while you inspect them.
````

## Exercise

On any app: (1) inspect a button, copy its selector AND xpath, verify both in Console with $$(). (2) In Network, trigger a form submit, find the XHR, read its status + payload, and copy it as cURL. Replay it in a terminal.
