# 2. Locators, Auto-Waiting and Assertions

## Locator preference

| Rank | Locator | Example from the project |
|---|---|---|
| 1 | **Role + accessible name** | `page.get_by_role("button", name="Log in")` |
| 2 | **Label / placeholder / text** | `page.get_by_label("Username")`, `page.get_by_text("Your cart is empty.")` |
| 3 | **Test ID** | `page.get_by_test_id("cart-total")` (reads `data-testid` by default) |
| 4 | CSS / XPath | `page.locator("table tbody tr")`, last resort |

Role-based locators find elements the way users and screen readers do, so they also nudge the app towards accessibility. Test IDs are perfect for elements without a clear role, such as a price or a total.

## Locators are lazy and strict

A `Locator` doesn't search the page when you create it; it searches **every time you use it**. That's why page objects can create all their locators in `__init__` without staleness problems.

Locators are also **strict**: an action on a locator that matches more than one element raises an error instead of silently using the first one. Narrow it down:

```python
# from pages/products_page.py: the "View" link inside the card containing this product name
self.cards.filter(has_text=name).get_by_role("link", name="View").click()
```

## Auto-waiting

Before `click()`, `fill()` and other actions, Playwright waits until the element is attached, visible, stable (not animating), enabled and able to receive events. No `WebDriverWait`, no sleeps.

## Web-first assertions: `expect()`

```python
expect(products.names).to_have_text(["Desk Lamp", "Smart Lamp"])   # every matching element, in order
expect(products.cards).to_have_count(0)
expect(login.error).to_have_text("Invalid username or password.")
expect(page).to_have_url(re.compile(r"/login$"))
```

`expect()` **retries** until the condition is true or the timeout passes (5 s by default). Compare:

```python
assert page.get_by_test_id("cart-total").inner_text() == "$40.25"   # reads ONCE: flaky if not yet rendered
expect(page.get_by_test_id("cart-total")).to_have_text("$40.25")    # retries: stable
```

Use plain `assert` only on values that are already final, such as a parsed API response or a value read after an `expect()` has confirmed the page is ready.

## Auto-waiting doesn't know which page you mean

Auto-waiting and retrying assertions remove most timing problems, but not this one. On the products page, after clicking **Search**, the *old* page already shows a result count. An `expect()` that runs right after the click can be satisfied by the old page before the new one loads.

The fix in `ProductsPage.search` is to first assert something that is **only true on the new page**, the URL:

```python
self.search_button.click()
expect(self.page).to_have_url(re.compile(r"\?q=" + re.escape(quote_plus(term))))
return self.wait_until_loaded()
```

Every navigating action in the page objects does the same: login waits for `/products$`, add-to-cart for `/cart?added=1`, checkout for `/orders/ORD-`. (The Selenium tutorial hits the same issue and solves it with `stalenessOf`.)

## Waiting for "loading finished"

`ProductsPage.wait_until_loaded` waits for the real signal:

```python
expect(self.spinner).to_be_hidden()
expect(self.results).not_to_be_empty()   # cards, "no results" or an error: all mean "done"
```

The first draft waited on `result_count.or_(search_error)` instead. It was replaced before it ever ran, because in the error case both elements exist: the combined locator would match two elements and a single-value `to_have_text` would fail strictness. Choosing one container that's empty only while loading avoids the problem.

## Review questions

**What locator strategy do you use in Playwright?**
Role and accessible name first, then label/placeholder/text, then test IDs, and CSS/XPath only as a last resort.

**What does "strict mode" mean?**
An action or single-value assertion on a locator that matches several elements throws an error, so you never act on the wrong element by accident. Narrow it with `filter()`, chaining or `nth()`.

**What is auto-waiting?**
Before acting, Playwright waits for the element to be visible, stable, enabled and able to receive events. You don't write explicit waits for those.

**Why `expect()` instead of `assert`?**
`expect()` retries until the condition holds or times out; `assert` checks once. On a live page, a single check races the rendering.

**Do you ever need explicit waits in Playwright?**
Rarely, but you still need to wait for the *right* thing: for example the new page's URL after a navigation, or a specific API response with `page.expect_response()`. `wait_for_timeout` is for demos only.

Next: [3. pytest fixtures and framework design](03-pytest-fixtures-and-framework-design.md)
