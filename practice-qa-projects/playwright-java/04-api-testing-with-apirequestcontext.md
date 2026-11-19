# 4. API Testing with APIRequestContext

`APIRequestContext` is Playwright's HTTP client. This framework uses it in two ways.

## Two kinds of client

| Client | How it's created | Cookies | Used for |
|---|---|---|---|
| **Browser-shared** | `context.request()` (`ShopSession.ui()`) | Shared with the page | Logging in or building data for a UI test |
| **Standalone** | `playwright.request().newContext(...)` (`ShopSession.newApiClient()`) | Its own cookie jar | Pure API tests; each client is a separate user |

From `support/ShopSession.java`:

```java
public ShopApi ui() {
    return new ShopApi(context.request());          // logging in here logs the page in too
}

public ShopApi newApiClient() {
    APIRequestContext api = BrowserManager.playwright().request()
            .newContext(new APIRequest.NewContextOptions().setBaseURL(Config.baseUrl()));
    apiClients.add(api);                             // disposed when the session closes
    return new ShopApi(api);
}
```

## Requests

From `services/ShopApi.java`:

```java
// POST with a JSON body: a Map (or any object) passed to setData is serialized to JSON
api.post("/api/login", RequestOptions.create()
        .setData(Map.of("username", username, "password", password)));

// GET with a query parameter
api.get("/api/products", RequestOptions.create().setQueryParam("q", term));

// POST with no body
api.post("/api/checkout");
```

Other options you'll use: `setHeader("Authorization", "Bearer ...")`, `setForm(FormData)` for form posts, `setMultipart(...)` for uploads, `setTimeout(...)`, `setFailOnStatusCode(true)`. Read the response with `status()`, `ok()`, `text()`, `headers()`. For a retrying assertion on a response: `PlaywrightAssertions.assertThat(response).isOK()`.

## API setup for UI tests: fast and stable

`CheckoutUiTest` builds the cart through the API and only clicks through checkout:

```java
session.ui().login("junit-shopper", Config.password())
        .addToCart(1001, 2)          // Blue Mug   2 x 12.50
        .addToCart(1006, 1);         // Eco Bottle 1 x 15.25

CartPage cart = new CartPage(session.page()).open();     // the page is already logged in
assertThat(cart.rows()).hasCount(2);
assertThat(cart.total()).isEqualByComparingTo("40.25");
```

In the verified run this whole test (login, cart, checkout and the API and database cross-checks) took **0.26 s** once the browser was warm.

## Testing security, not just happy paths

`orders_api.feature` includes authorization checks that UI tests rarely cover:

```gherkin
@regression @security
Scenario: A shopper cannot read another shopper's order
  Given I am signed in to the API as "order-owner"
  And my cart contains:
    | product   | quantity |
    | Desk Lamp | 1        |
  And I check out through the API
  When another signed-in user asks for that order
  Then the API responds with status 404 and an error containing "order not found"

@regression @security
Scenario: Orders cannot be read without signing in
  When an anonymous client asks for order "ORD-00000000"
  Then the API responds with status 401 and an error containing "not logged in"
```

Two standalone clients make "another user" trivial: each `newApiClient()` has its own cookie jar.

> While building this, the anonymous request first returned a **303 redirect to the login page** instead of a 401. That's fine for a browser but wrong for an API. The practice shop was fixed to return JSON 401 for `/api/*`: exactly the kind of defect API tests exist to find.

## What every API test should check

- Status code (including the *right* error code: 400, 401, 404, 409)
- Response schema (Chapter 5)
- Business values: totals, quantities, statuses
- Authorization: a user must never see another user's data
- Validation: out-of-range input (`cartRejectsAQuantityOutOfRange`: 0 and 6 rejected, 5 accepted)
- Side effects in the database (Chapter 7)

## Review questions

**What is APIRequestContext?**
Playwright's HTTP client. Standalone (`playwright.request().newContext()`) for pure API tests, or `context.request()` which shares cookies with the browser context.

**How would you log in once for a UI test without using the UI?**
Call the login API through `context.request()`; the cookie lands in the browser context, so the page is logged in.

**How do you test that one user can't access another user's data?**
Two API clients with separate sessions: create data as user A, request it as user B, expect 403 or 404 and no data.

**APIRequestContext or REST Assured?**
Both work. APIRequestContext keeps API and UI tests in one tool and shares cookies with the browser; REST Assured has a fluent given/when/then style and a large ecosystem.

Next: [5. JSON parsing and schema validation](05-json-parsing-and-schema-validation.md)
