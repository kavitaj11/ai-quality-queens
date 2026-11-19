# 9. Review Questions and Exercises

## Rapid-fire questions

| Question | Short answer |
|---|---|
| Playwright Java: how do you choose the browser? | `playwright.chromium()/firefox()/webkit().launch(...)`; here `-Dbrowser=firefox` |
| How do you run headed and slowed down? | `LaunchOptions.setHeadless(false).setSlowMo(300)`; here `-Dheadless=false -DslowMo=300` |
| Locator vs ElementHandle? | Locators are lazy, auto-wait and are strict; handles can go stale |
| How do you assert text that appears later? | `assertThat(locator).hasText(...)` retries until the timeout |
| How do you change the assertion timeout? | `PlaywrightAssertions.setDefaultAssertionTimeout(ms)` |
| How do you handle a new tab? | `Page popup = context.waitForPage(() -> link.click());` |
| How do you handle a dialog? | `page.onDialog(dialog -> dialog.accept());` before the action |
| How do you mock an API call in the UI? | `page.route("**/api/products*", route -> route.fulfill(...))` |
| JSON body in APIRequestContext? | `RequestOptions.create().setData(map or object)` |
| Form body? | `RequestOptions.create().setForm(FormData.create().set("k", "v"))` |
| How do you parse JSON to objects? | Jackson `readValue` into records/POJOs |
| How do you compare money? | `BigDecimal` with `compareTo` / `isEqualByComparingTo` |
| How do Cucumber steps share state? | PicoContainer-injected context object per scenario |
| How do you run only smoke scenarios? | `-Dcucumber.filter.tags="@smoke"` |
| How do you avoid SQL injection in tests? | `PreparedStatement` with bound parameters |

## Debugging scenarios from this framework

1. **"Why can a Java regex fail in a browser locator?"** The `Pattern.quote()` regex that couldn't match because Playwright evaluates patterns in JavaScript (Chapter 2).
2. **"How can failing scenarios be hidden by a green build?"** Surefire 3.5.3 reporting 0 Cucumber tests and a green build with failing scenarios; pinned 3.2.5 and added a negative control (Chapter 8).
3. **"Why can excluding transitive dependencies break schema validation?"** Excluding networknt's YAML dependencies broke schema validation at runtime; kept and pinned them (Chapter 5).
4. **"Which response violates the authentication contract?"** Unauthenticated API calls returned a 303 redirect instead of 401 (Chapter 4).
5. **"How do you keep UI tests fast?"** API setup through `context.request()`: the UI checkout test runs in 0.26 s once the browser is warm.
6. **"How do you test data integrity?"** Reconciliation (header total minus lines = 0) and exactly-one audit row (Chapter 7).

## Exercises

1. **Run it.** Start the shop, `mvn test`, then open `target/cucumber-report.html`.
2. **Read a trace.** Change `"40.25"` to `"40.26"` in `orders_api.feature`, run it, open the trace, then put it back.
3. **Firefox and WebKit.** Run with `-Dbrowser=firefox` and `-Dbrowser=webkit`.
4. **New API scenario.** Add a scenario: adding the same product twice caps the quantity at 5. Use a DataTable.
5. **Network mock.** Write a JUnit test that routes `/api/products*` to return HTTP 500 and asserts the "Could not load products" message.
6. **New SQL check.** Add a `ShopDb` method and step that checks no order has duplicate lines for the same product.
7. **Schema drift.** Add an extra field to `order_json` in `shop_web.py` and watch the schema check and `FAIL_ON_UNKNOWN_PROPERTIES` catch it. Revert.
8. **Parallel scenarios.** Enable `cucumber.execution.parallel.enabled=true` in `junit-platform.properties`. What makes this framework safe to run in parallel?
9. **Negative control.** Run with `-DshopPassword=wrong-on-purpose` and confirm the build fails with traces.
10. **CI.** Put the `Jenkinsfile` or `.gitlab-ci.yml` into a repository and get it green.

---

Back to the [Playwright Java tutorial index](README.md) · [All tutorials](../README.md)
