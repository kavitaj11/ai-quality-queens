# 3. Page Objects and Service Objects

## Framework layers

```
Features (Gherkin)          JUnit tests
        │                        │
Step definitions (thin) ─────────┤
        │                        │
Page objects (UI)      Service objects (API)      ShopDb (SQL)
        │                        │                    │
ShopSession: BrowserContext + Page + APIRequestContext clients + tracing
        │
BrowserManager (ThreadLocal Playwright + Browser)  ·  Config
```

Each layer has one job. A UI change touches one page object; an endpoint change touches one service object.

## Page objects

From `pages/ProductPage.java`:

```java
public class ProductPage {
    private final Page page;
    private final Locator price;
    private final Locator quantity;
    private final Locator addToCart;

    public ProductPage(Page page) {
        this.page = page;
        this.price = page.getByTestId("product-detail-price");
        this.quantity = page.getByTestId("quantity");
        this.addToCart = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add to cart"));
    }

    public BigDecimal unitPrice() { return Money.parse(price.innerText()); }

    public CartPage addToCart(int qty) {
        quantity.selectOption(String.valueOf(qty));
        addToCart.click();
        assertThat(page).hasURL(Pattern.compile("/cart\\?added=1$"));
        return new CartPage(page);
    }
}
```

Rules used throughout `pages/`:

1. Locators are private fields created in the constructor (safe because locators are lazy).
2. Methods express user intent and **return the next page object**, so tests chain naturally.
3. Pages expose values (`unitPrice()`, `total()`) or `Locator`s for retrying assertions; **business assertions live in tests and steps**.
4. The only assertions inside pages confirm *which page* has loaded (URL), so callers never race ahead.

## Service objects

`services/ShopApi.java` is the API equivalent of a page object. Steps and tests call business methods; they never build URLs, headers or JSON:

```java
public Order checkout() {
    APIResponse response = rawCheckout();
    expectStatus(response, 201, "checkout");
    SchemaValidator.assertMatches("order", response.text());   // structure first
    return Json.read(response.text(), Order.class);            // then a typed object
}

public APIResponse rawCheckout() {
    return api.post("/api/checkout");
}
```

Two styles of method:

| Style | Example | Use |
|---|---|---|
| Business method | `checkout()`, `login(...)`, `addToCart(...)` | Happy paths and setup: returns a typed result, fails fast with the response body in the message |
| `raw*` method | `rawCheckout()`, `rawGetOrder(id)` | Negative tests: returns the `APIResponse` so the test can assert 401/404/409 |

`ShopApi` wraps **any** `APIRequestContext`, which makes it reusable in two ways (Chapter 4): over `context.request()` to share the browser's login, or over a standalone client to act as a separate user.

## Sharing state without static fields

`ShopSession` holds one test's context, page and API clients. Cucumber gets it through `TestContext` (injected by PicoContainer, Chapter 6), and JUnit tests through `PlaywrightExtension`:

```java
@ExtendWith(PlaywrightExtension.class)
class CheckoutUiTest {
    @Test
    void checkoutOfAnApiBuiltCartAgreesAcrossUiApiAndDatabase(ShopSession session) { ... }
}
```

The extension creates the session as a method parameter, and in `afterEach` closes it with `context.getExecutionException().isPresent()`, so the trace is kept only for failures. No test class stores a browser in a static field, so the framework is safe to run in parallel.

## Review questions

**What is a service object?**
A class wrapping one API's endpoints behind business methods, like `checkout()` or `getOrder(id)`. It's the API equivalent of a page object.

**Should page objects contain assertions?**
Not business assertions. They return values or locators; tests assert. Waiting for the page to load (URL) is acceptable because it keeps callers from racing.

**Why do some service methods return the raw APIResponse?**
For negative tests: the test needs to assert error statuses and messages instead of the method throwing.

**How do JUnit tests and Cucumber steps share the same framework?**
Both use `ShopSession`, pages and services; only the entry point differs (`PlaywrightExtension` for JUnit, `TestContext` + hooks for Cucumber).

Next: [4. API testing with APIRequestContext](04-api-testing-with-apirequestcontext.md)
