# Selenium WebDriver

**Hands-on project** · [Selenium with Java practice project](../../../../practice-qa-projects/selenium-java/)

> **Learning objective** · Synchronize a browser assertion with the result a shopper actually sees.

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
