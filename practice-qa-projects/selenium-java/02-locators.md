# 2. Locators

A test is only as stable as its locators. Most "flaky Selenium" problems are really locator or waiting problems.

## Preference order

| Rank | Locator | Example | Why |
|---|---|---|---|
| 1 | **Dedicated test attribute** | `By.cssSelector("[data-testid='login-button']")` | Exists only for tests; survives redesigns |
| 2 | **id / name** | `By.id("username")` | Stable if unique and not auto-generated |
| 3 | **Short CSS selector** | `By.cssSelector("form.login button[type='submit']")` | Fast, readable |
| 4 | **Relative XPath** | `By.xpath("//div[@data-testid='product-card'][.//div[text()='Blue Mug']]")` | When you need text or to go up to a parent |
| ✗ | Absolute XPath | `/html/body/div[2]/main/form/button` | Breaks on any layout change |

Every element the tests need in the practice shop has a `data-testid`, so the framework uses one helper (in `BasePage`):

```java
protected static By testId(String id) {
    return By.cssSelector("[data-testid='" + id + "']");
}
```

If your application has no test IDs, ask the developers to add them. It's a small change, and the cheapest stability win available.

## Scoped locators: find inside an element

To click "View" on the *Travel Mug* card, don't search the whole page for a "View" link (there are several). Find the right card, then search inside it. From `ProductsPage.openProduct`:

```java
WebElement card = driver.findElements(CARD).stream()
        .filter(c -> c.findElement(CARD_NAME).getText().trim().equals(name))
        .findFirst()
        .orElseThrow(() -> new AssertionError("No product card named '" + name + "'"));
card.findElement(testId("view-product")).click();
```

## CSS vs XPath cheat sheet

| Goal | CSS | XPath |
|---|---|---|
| By attribute | `[data-testid='cart-total']` | `//*[@data-testid='cart-total']` |
| Attribute contains | `[class*='error']` | `//*[contains(@class,'error')]` |
| Direct child | `table > tbody > tr` | `//table/tbody/tr` |
| By visible text | not possible | `//button[text()='Checkout']` / `contains(text(),'Check')` |
| Parent of an element | not possible | `//div[@data-testid='product-name']/..` |
| Nth match | `tr:nth-of-type(2)` | `(//tr)[2]` |

CSS is usually faster to write and read. Use XPath when you need text matching or to move up the tree.

## findElement vs findElements

| | `findElement` | `findElements` |
|---|---|---|
| Nothing matches | throws `NoSuchElementException` | returns an **empty list** |
| Use for | elements that must exist | checking presence/absence, counting |

`CartPage.isEmpty()` uses `findElements(...).isEmpty()` precisely so that absence is a normal answer, not an exception.

## Review questions

**What's your locator strategy?**
Dedicated test attributes first, then stable ids/names, then short CSS, and relative XPath only when I need text or parent navigation. Never absolute XPath. If there are no stable attributes, I ask developers to add `data-testid`.

**CSS or XPath: which is better?**
Neither always. CSS is concise and fast; XPath can match text and walk up to parents. I default to CSS and use XPath for those two cases.

**How do you handle elements with dynamic ids like `btn-48213`?**
Avoid the dynamic part: use a stable attribute, `starts-with(@id,'btn-')`, a scoped locator inside a stable parent, or ask for a test ID.

**How do you select from a dropdown?**
For a native `<select>`, use `new Select(element).selectByValue/VisibleText/Index` (see `ProductPage.chooseQuantity`). For custom dropdowns built from divs, click to open, then click the option like any element.

Next: [3. Waits and synchronization](03-waits-and-synchronization.md)
