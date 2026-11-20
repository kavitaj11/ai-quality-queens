# Actions API & JavaScript executor

[06 · Module topics](../../modules/06-selenium.md) · [Curriculum index](../../README.md)

**Category** · BEYOND THE BASICS

Hover, drag-and-drop, keyboard chords via ActionChains; JS executor for scrolling and the rare element Selenium can't touch. Use the executor sparingly — it bypasses real user behavior.

## Worked reference

````text
from selenium.webdriver import ActionChains

ActionChains(driver)\
    .move_to_element(menu)\
    .click(submenu_item)\
    .perform()

driver.execute_script(
    "arguments[0].scrollIntoView({block:'center'})", el)
````

## Exercise

Automate a hover-menu navigation and a drag-and-drop, then scroll an element into view with JS.
