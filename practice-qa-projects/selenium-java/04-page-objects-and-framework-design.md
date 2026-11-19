# 4. Page Object Model and Framework Design

## Framework layers

```
Tests (TestNG classes, Cucumber steps)      "what": business scenarios and assertions
        │
Page objects (LoginPage, ProductsPage ...)   "how": locators and actions for one page
        │
BasePage (waits, click, type, testId)        shared interaction helpers
        │
DriverFactory (ThreadLocal) + Config          which browser, where, with what settings
        │
ShopApi                                       fast test setup through the API
```

Each layer has one job. A UI change touches one page object, not fifty tests.

## Page Object rules used in this project

1. **Locators are private constants inside the page object.** Tests never see a `By`.
2. **Methods express user intent:** `loginAs`, `search`, `chooseQuantity`, `addToCart`.
3. **Navigation returns the next page object.** `loginAs` returns `ProductsPage`, `addToCart` returns `CartPage`, so tests chain naturally:

```java
ProductPage product = new LoginPage(driver()).open()
        .loginAs("e2e-user", Config.password())
        .search("mug")
        .openProduct("Travel Mug");
```

4. **No assertions in page objects.** Pages return values (`total()`, `errorMessage()`) and tests assert. That keeps pages reusable for positive and negative tests.
5. **Each page knows when it is loaded** (`waitUntilLoaded()`), so callers never need their own waits.
6. **Separate methods for success and failure paths:** `loginAs` (returns `ProductsPage`) vs `loginExpectingError` (stays on `LoginPage`).

Should you use `@FindBy` and `PageFactory`? It works, but proxied `WebElement` fields hide when elements are looked up, and its lazy proxies make stale-element bugs harder to see. This project uses plain `By` constants with explicit waits, which is the more common choice in modern frameworks.

## The driver: one per thread

`DriverFactory` keeps the WebDriver in a `ThreadLocal`:

```java
private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

public static WebDriver start() { ... DRIVER.set(driver); return driver; }
public static WebDriver getDriver() { ... return DRIVER.get(); }
public static void quit() { ... driver.quit(); DRIVER.remove(); }
```

Each test thread gets its own browser, and `remove()` stops a dead driver leaking into the next test on that thread.

## A real parallel bug found while writing this tutorial

The first version of `BaseTest` looked reasonable:

```java
protected WebDriver driver;            // ← the bug

@BeforeMethod
public void startBrowser() { driver = DriverFactory.start(); }
```

Serially, all 14 tests passed. In the parallel suite, `validUserCanLogIn` failed: the URL was `/login` instead of `/products`. The failure **screenshot** showed the login page with "Username and password are required.", which is the message from a *different* test.

Why: with `parallel="methods"`, TestNG runs several methods of the **same test-class instance** at once on different threads. `driver` was an instance field, so thread B's `@BeforeMethod` overwrote thread A's driver mid-test, and A started driving B's browser. The `ThreadLocal` in `DriverFactory` was correct; copying its value into a shared field defeated it.

The fix (now in `BaseTest`):

```java
protected WebDriver driver() {
    return DriverFactory.getDriver();   // always this thread's own driver
}
```

After the fix the parallel suite passed three runs in a row. **Two lessons:** shared mutable fields break parallel tests even when you use ThreadLocal, and failure screenshots are what made this a 5-minute diagnosis instead of a day.

## Configuration

`Config` reads each setting from a `-D` system property, then an environment variable, then a default. The same code runs on a laptop (`-Dheadless=false`) and in CI (`BASE_URL=... BROWSER=edge`) with no code changes. Secrets such as passwords come from the environment in real projects, never from source code.

## Review questions

**What is the Page Object Model and why use it?**
One class per page or component holding its locators and user actions. Tests read like scenarios, duplication disappears, and a UI change is fixed in one place.

**Should page objects contain assertions?**
No. They return state; tests assert. Otherwise pages can't be reused for negative tests.

**How do you run tests in parallel safely?**
One WebDriver per thread in a `ThreadLocal`, no shared mutable state in test classes or static fields, independent test data per test, and `remove()` the ThreadLocal on quit.

**Walk me through your framework's folder structure.**
`config`, `driver`, `pages`, `api`, `tests`, `bdd`, `resources/features`, plus suite XMLs and the Maven POM. (Describe this project's layout, or your own.)

**PageFactory or By locators?**
Both work. I prefer `By` constants with explicit waits: element lookup happens when I expect it, and stale-element issues are easier to reason about.

Next: [5. TestNG: data-driven, groups and parallel](05-testng-data-driven-and-parallel.md)
