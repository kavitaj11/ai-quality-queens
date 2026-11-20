# Locator strategies

[06 · Module topics](../../modules/06-selenium.md) · [Curriculum index](../../README.md)

**Category** · WEBDRIVER CORE

Selenium's 8 built-in By strategies, the CSS/XPath families that cover everything else, and how to reach elements the standard By can't — hidden/non-rendered nodes, shadow DOM, dynamic attributes, and elements defined only by relative position. Priority for stability: test id > id > semantic CSS > XPath by structure; never brittle nth-child or absolute XPath. Brittle locators are the #1 cause of flaky suites.

## Worked reference

````text
from selenium.webdriver.common.by import By
from selenium.webdriver.support.relative_locator import locate_with

# ── 1. THE 8 BUILT-IN By STRATEGIES ──
driver.find_element(By.ID, "email")
driver.find_element(By.NAME, "password")
driver.find_element(By.CLASS_NAME, "primary")      # ONE class only
driver.find_element(By.TAG_NAME, "input")
driver.find_element(By.LINK_TEXT, "Sign in")
driver.find_element(By.PARTIAL_LINK_TEXT, "Sign")
driver.find_element(By.CSS_SELECTOR, "[data-testid='login']")
driver.find_element(By.XPATH, "//button[normalize-space()='Log in']")

# ── 2. CSS covers most cases (attribute operators, combinators) ──
driver.find_element(By.CSS_SELECTOR, "form > input[name^='user']")
driver.find_element(By.CSS_SELECTOR, "li:nth-of-type(2)")

# ── 3. DYNAMIC / PARTIAL attributes (ids that change per build) ──
driver.find_element(By.CSS_SELECTOR, "[id^='input-']")     # starts-with
driver.find_element(By.CSS_SELECTOR, "[class*='col-']")    # contains
driver.find_element(By.XPATH, "//*[contains(@id,'btn-')]")

# ── 4. RELATIVE (friendly) locators — by position ──
pwd = driver.find_element(locate_with(By.TAG_NAME, "input")
    .below({By.ID: "email"}).above({By.ID: "submit"}))

# ── 5. HIDDEN / non-rendered elements ──
# find_element only returns present nodes; if it's display:none
# Selenium won't click it. Read it, or force via JS:
hidden = driver.find_element(By.CSS_SELECTOR, "[data-token]")
val = hidden.get_attribute("data-token")           # attrs read fine
driver.execute_script("arguments[0].click();", hidden)  # bypass vis
# Reveal first, or send_keys to a hidden file input directly:
driver.find_element(By.CSS_SELECTOR,
    "input[type=file]").send_keys("/tmp/a.png")

# ── 6. SHADOW DOM (standard By can't cross it) ──
host = driver.find_element(By.CSS_SELECTOR, "settings-panel")
host.shadow_root.find_element(By.CSS_SELECTOR, "button.save").click()

# ── 7. CUSTOM STRATEGY: reusable locator helpers ──
def by_test_id(tid):                       # your team's convention
    return (By.CSS_SELECTOR, f"[data-testid='{tid}']")
def by_label(text):                        # field next to a label
    return (By.XPATH,
        f"//label[normalize-space()='{text}']/following::input[1]")

driver.find_element(*by_test_id("submit"))
driver.find_element(*by_label("Email"))

# ── 8. JS-based location (last resort for tricky DOM) ──
el = driver.execute_script(
    "return document.querySelector('deep > .x')")
````

## Exercise

Automate a form using each of the 8 By strategies once, then locate a display:none element (via JS), a shadow-DOM node, and a field found only by its position relative to a label.
