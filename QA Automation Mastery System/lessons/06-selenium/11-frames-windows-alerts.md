# Frames, windows & alerts

[06 · Module topics](../../modules/06-selenium.md) · [Curriculum index](../../README.md)

**Category** · BEYOND THE BASICS

Context switching is where beginners get stuck: driver.switch_to for iframes, window handles for tabs, and alert handling for native dialogs.

## Worked reference

````text
driver.switch_to.frame("editor-iframe")
driver.find_element(By.ID, "body").send_keys("hi")
driver.switch_to.default_content()

original = driver.current_window_handle
for h in driver.window_handles:
    if h != original:
        driver.switch_to.window(h)

driver.switch_to.alert.accept()
````

## Exercise

On the-internet.herokuapp.com, automate the nested frames page and the JavaScript alerts page.
