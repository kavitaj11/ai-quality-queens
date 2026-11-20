# 06. Locators and isolation with Playwright Java

**Intermediate** · [Guide and prerequisites](../README.md) · [Worked example](../examples/playwright/ShippingBrowser.java)

## Element identity and session boundaries

A locator describes how to identify an element when an operation runs. Roles and accessible names can encode the user’s intent even when elements are recreated during rendering. Scope the locator when names repeat. A row-specific Edit button has a clearer identity than the third Edit button on a changing page.

A browser context owns client state such as cookies. Independent contexts let a test represent two signed-in users without mixing their sessions. They may still share server-side objects. In a collaborative editor, use separate contexts for author and reviewer, then give each test its own document. This separates the deliberate sharing within a scenario from accidental sharing between parallel runs.

## Apply the concepts in the lab

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
