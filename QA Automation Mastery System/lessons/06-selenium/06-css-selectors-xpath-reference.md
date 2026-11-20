# CSS selectors & XPath reference

[06 · Module topics](../../modules/06-selenium.md) · [Curriculum index](../../README.md)

**Category** · WEBDRIVER CORE

The hands-on selector reference: every CSS family (basic, all attribute operators, combinators, pseudo-classes), XPath by text and by axis, and the JavaScript DOM query methods you use in the DevTools console to build and verify locators. This is the code behind the Foundations 'DOM, CSS selectors & XPath' overview.

## Worked reference

````text
# ── CSS SELECTOR FAMILIES (Selenium By.CSS_SELECTOR) ──
# Basic:      #id  .class  tag  *
"#email"                          # id
".btn.primary"                    # element with BOTH classes
"input"                           # by tag
# Attribute:
"[data-testid='submit']"          # exact
"[href^='https']"                 # starts-with  ^=
"[href$='.pdf']"                  # ends-with    $=
"[class*='col-']"                 # contains     *=
"[data-state~='active']"          # word in list ~=
# Combinators:
"form input"                      # descendant (any depth)
"form > input"                    # direct child
"label + input"                   # adjacent sibling
"h2 ~ p"                          # general sibling
# Pseudo-classes:
"li:first-child"  "li:last-child"
"tr:nth-child(2n)"                # even rows
"input:not([disabled])"          # negation
"a:checked" "input:enabled"

# ── XPATH (for text + axes CSS can't reach) ──
"//button[text()='Save']"                    # exact text
"//button[contains(., 'Save')]"              # partial text
"//input[@name='email']"                     # attribute
"//div[@class='card'][2]"                    # index (1-based)
"//label[text()='Email']/following-sibling::input"
"//td[.='Total']/ancestor::table"            # go UP the tree
"//*[@id='row']/preceding-sibling::*"

# Usage
from selenium.webdriver.common.by import By
driver.find_element(By.CSS_SELECTOR, "#email")
driver.find_elements(By.XPATH, "//button[text()='Save']")
````

## Exercise

Write one selector for each CSS family and two XPath expressions (one by text, one by axis); verify each matches exactly one node with $$() in the console.
