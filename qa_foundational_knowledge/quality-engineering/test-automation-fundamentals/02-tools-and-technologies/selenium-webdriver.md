# Selenium WebDriver

**Hands-on project** · [Selenium with Java practice project](../../../../practice-qa-projects/selenium-java/)

**QA Automation Mastery** · [06 · Selenium](../../../../QA%20Automation%20Mastery%20System/modules/06-selenium.md)

> **Learning objective** · Synchronize a browser assertion with the result a shopper actually sees.

## How WebDriver observes a browser

A WebDriver session connects test code to a particular browser. A locator describes how to find an element in the current document. A successful lookup establishes that an element exists at that moment. The application may still be fetching data, replacing the element or rendering the result of an earlier action.

Design an interaction as three separate decisions: identify the intended control, perform the user action once, then wait for a meaningful outcome. An explicit wait polls a condition up to a deadline. Choose a condition tied to the current operation, such as the expected order ID appearing in a confirmation. Waiting only for a visible panel can succeed while it still displays the previous order. Keep the expected business value in the test so a reader can tell what the interaction was meant to prove.

## ◈ Trace a browser interaction

The shipping calculator renders its status element before the quote is ready. A successful click proves the browser accepted the action. The test still needs evidence that the application finished the calculation.

```java
driver.findElement(By.id("subtotal")).sendKeys("5000");
driver.findElement(By.id("calculate")).click();
new WebDriverWait(driver, Duration.ofSeconds(5)).until(
    ExpectedConditions.textToBe(By.cssSelector("[role=status]"), "Free shipping"));
```

Choose an ID when it consistently identifies one control. A scoped CSS selector is useful for a result region. Text or XPath can express other relationships, but selectors tied to layout positions often break during an unrelated redesign. Use a fresh browser session for independent tests and call `driver.quit()` in a `finally` block to release it on failure.

## ✎ Browser lab

Follow the [Java guide setup](../../test-automation-in-java/README.md) to start the local calculator and run the [complete WebDriver example](../../test-automation-in-java/examples/selenium/ShippingBrowser.java). First run the qualifying subtotal. Then enter 4,999 while leaving the expectation unchanged. A timeout is expected because the final status describes a paid quote. Inspect the visible text before deciding whether the locator, wait or expectation needs to change.

---

[Return to the training guide](../README.md)
