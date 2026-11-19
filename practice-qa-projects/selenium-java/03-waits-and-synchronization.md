# 3. Waits and Synchronization

Web pages change after they load: data arrives from APIs, spinners come and go, pages navigate. A test that doesn't wait for the right moment fails sometimes, and that is the definition of a **flaky test**.

The practice shop is built to make this real: product search results come from an API that takes **400–1200 ms**, behind a "Loading products..." spinner.

## The three kinds of wait

| Wait | How | Use it? |
|---|---|---|
| `Thread.sleep(2000)` | Always waits the full time | **Never.** Too short is flaky, too long is slow, usually both |
| **Implicit wait** | `driver.manage().timeouts().implicitlyWait(...)`: every `findElement` polls until found | Avoid. It only waits for *presence*, applies globally, and mixes badly with explicit waits |
| **Explicit wait** | `WebDriverWait` + a condition, for one specific moment | **Yes.** This framework uses only explicit waits |
| **Fluent wait** | `FluentWait` with custom polling and ignored exceptions | Explicit wait with more knobs; `WebDriverWait` is a `FluentWait` |

The framework sets **no implicit wait** (see `DriverFactory`) and every page-object action waits first (see `BasePage`):

```java
protected void click(By locator) {
    wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
}
```

## Wait for the real signal

What does "the search finished" actually look like? In `ProductsPage`:

```java
public ProductsPage waitUntilLoaded() {
    wait.until(ExpectedConditions.invisibilityOfElementLocated(SPINNER));
    wait.until(d -> !d.findElement(RESULT_COUNT).getText().isBlank());
    return this;
}
```

The spinner disappears **and** the result count has text. Either one alone can be true too early. A lambda (`d -> ...`) lets you wait for any condition you can express in code.

## A real race found while writing this tutorial

The first version of `search()` was:

```java
type(SEARCH_INPUT, term);
click(SEARCH_BUTTON);          // starts loading a NEW page
return waitUntilLoaded();      // ...but the OLD page already has a result count!
```

The wait could pass instantly on the **old** page, before the browser had navigated, and the test would then read stale results. The same trap existed after "Add to cart": both the product page and the cart page have an `<h1>`.

The fix: remember the old page's `<html>` element, click, and wait until it is **stale** (detached from the DOM). Only then wait for the new page. From `BasePage`:

```java
protected void clickAndWaitForNewPage(By locator) {
    WebElement oldPage = driver.findElement(By.tagName("html"));
    click(locator);
    wait.until(ExpectedConditions.stalenessOf(oldPage));
}
```

Lesson: **after an action that navigates, make sure you're waiting on the new page, not the old one.**

## StaleElementReferenceException

A `WebElement` is a reference to one specific DOM node. If the page re-renders or navigates, that node is gone and using the reference throws `StaleElementReferenceException`. Fixes:

- **Re-find** the element after the change instead of keeping old references (page objects store `By` locators, not `WebElement`s, for exactly this reason).
- Wait for the re-render to finish (spinner gone, new content present) before finding again.
- Don't keep a list of elements across a page change.

## Useful ExpectedConditions

| Condition | Typical use |
|---|---|
| `visibilityOfElementLocated` | Read text, check something is shown |
| `elementToBeClickable` | Before clicking |
| `invisibilityOfElementLocated` | Spinners, overlays, toasts disappearing |
| `stalenessOf` | The old page or element has gone |
| `textToBePresentInElementLocated` | A status changes to "CONFIRMED" |
| `urlContains` / `titleContains` | Navigation finished |
| `numberOfElementsToBe` | A list finished loading a known number of items |
| `alertIsPresent` | Before switching to a JavaScript alert |
| `frameToBeAvailableAndSwitchToIt` | Before working inside an iframe |

## Review questions

**Implicit vs explicit wait?**
An implicit wait applies to every `findElement` and only waits for presence. An explicit wait waits for a specific condition (visible, clickable, invisible, stale) at a specific point. I use explicit waits only. Mixing the two can produce unpredictable timeouts.

**Why is Thread.sleep bad?**
It waits a fixed time regardless of the app: flaky when too short, slow when too long. Explicit waits return as soon as the condition is met.

**How do you handle a loading spinner?**
Wait for the spinner to become invisible **and** for the content you need to appear, because the spinner may not have appeared yet when you start waiting.

**What causes StaleElementReferenceException and how do you fix it?**
The DOM node behind the reference was replaced (re-render or navigation). Re-locate the element after the change; store locators, not elements, in page objects.

**How do you make a flaky test reliable?**
Find out why it's flaky first: timing, shared data, test order, or environment. For timing, replace sleeps and implicit waits with explicit waits on the real signal. For data, give each test its own data. Never just add retries.

Next: [4. Page Object Model and framework design](04-page-objects-and-framework-design.md)
