# 06. Locators and isolation with Playwright Java

**Intermediate** · [Guide and prerequisites](../README.md) · [Worked example](../examples/playwright/ShippingBrowser.java)

The input is located by its accessible label and the result by its status role. A locator resolves the current element when used. Clicking waits for actionability, while hasText retries the separate assertion about the resulting quote. A fresh browser context keeps cookies and local storage isolated.

## Run the example

From the guide directory, after completing the prerequisites in the guide:

```sh
jbang examples/playwright/ShippingBrowser.java
```

## Interview question

**Does auto-waiting mean the business action has completed?**

A clickable button is evidence that input can be submitted. Only the quote text provides evidence that the calculation completed. Use a web-first assertion on that result and a fresh context for independent users.

## Practice

Execute the paid-shipping scenario, then change both input and expectation to cover the first free subtotal. Rename the input label in a practice copy and inspect which locator fails.
