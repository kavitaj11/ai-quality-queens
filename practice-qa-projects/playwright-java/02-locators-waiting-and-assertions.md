# 2. Locators, Waiting and Assertions

## Locators

| Preference | Java | Example from `pages/` |
|---|---|---|
| 1. Role + accessible name | `page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Log in"))` | `LoginPage.submit` |
| 2. Label / placeholder / text | `page.getByLabel("Username")` | |
| 3. Test id (`data-testid`) | `page.getByTestId("cart-total")` | most data fields |
| 4. CSS / XPath | `page.locator("table tbody tr")` | last resort |

Locators are **lazy** (they search each time they're used, so no stale elements) and **strict** (acting on a locator that matches several elements throws). Narrow them down:

```java
cards.filter(new Locator.FilterOptions().setHasText(name))
     .getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName("View"))
     .click();
```

## Auto-waiting and retrying assertions

Actions (`click`, `fill`, `selectOption`) wait for the element to be visible, stable and enabled. Assertions from `PlaywrightAssertions` **retry** until they pass or time out:

```java
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

assertThat(products.welcome()).hasText(greeting);
assertThat(cart.rows()).hasCount(2);
assertThat(page).hasURL(Pattern.compile("/orders/ORD-[0-9A-F]{8}$"));
assertThat(results).not().isEmpty();
```

This framework uses **two** `assertThat`s: Playwright's (for `Locator`, `Page`, `APIResponse`, retrying) and AssertJ's (for plain values such as `BigDecimal`). Java picks the right one by argument type.

## Waiting for the right page

After clicking **Search**, the old page already shows results, so a "results are visible" assertion can pass on the old page. Every navigating method waits first for something only the new page has, its URL:

```java
searchButton.click();
assertThat(page).hasURL(Pattern.compile("\\?q=" + escapeForJsRegex(encoded) + "$"));
return waitUntilLoaded();     // spinner hidden, results container not empty
```

## A real bug found while building this framework: Java regex in a JavaScript engine

The first version used `Pattern.quote(term)` to build the URL pattern. Every search step failed:

```
AssertionFailedError: Page URL expected to match regex
Expected: \?q=\Qlamp\E$
Received: http://127.0.0.1:8091/products?q=lamp
```

The URL clearly matches, so why the failure? **Playwright for Java sends the `Pattern`'s source to the browser, where JavaScript's regex engine evaluates it.** `\Q...\E` is Java-only syntax; JavaScript doesn't understand it. The fix escapes special characters the portable way:

```java
private static String escapeForJsRegex(String text) {
    return text.replaceAll("[.*+?^${}()|\\[\\]\\\\]", "\\\\$0");
}
```

Lesson: in Playwright for Java, write regexes that are valid in **both** Java and JavaScript: no `\Q..\E`, possessive quantifiers or other Java-only features.

## Review questions

**What's the difference between a Locator and an ElementHandle?**
A `Locator` is lazy and re-queries the DOM every time, with auto-waiting and strictness; an `ElementHandle` points to one DOM node and can go stale. Use locators.

**What does strict mode mean?**
An action on a locator matching more than one element throws, so you never act on the wrong element by accident.

**Do you need explicit waits in Playwright?**
Rarely. Actions auto-wait and assertions retry. You still have to wait for the *right* thing, for example the new page's URL after navigation.

**Gotcha question: why might a regex URL assertion fail when the URL looks right?**
Playwright Java evaluates the pattern with the browser's JavaScript regex engine; Java-only syntax such as `Pattern.quote` won't work.

Next: [3. Page objects and service objects](03-page-objects-and-service-objects.md)
