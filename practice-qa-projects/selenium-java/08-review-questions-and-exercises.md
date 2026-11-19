# 8. Review Questions and Exercises

## Rapid-fire questions

| Question | Short answer |
|---|---|
| `driver.close()` vs `driver.quit()`? | `close` closes the current window; `quit` ends the session and closes all windows. Use `quit` in teardown. |
| `get()` vs `navigate().to()`? | Both load a URL and wait for the page load; `navigate()` also has `back()`, `forward()`, `refresh()`. |
| How do you handle a JavaScript alert? | `wait.until(alertIsPresent())`, then `driver.switchTo().alert().accept()` / `dismiss()` / `getText()`. |
| How do you work inside an iframe? | `driver.switchTo().frame(element or name)`, then `switchTo().defaultContent()` when done. |
| How do you switch to a new tab? | Store `getWindowHandle()`, loop `getWindowHandles()`, `switchTo().window(handle)`. Selenium 4: `switchTo().newWindow(WindowType.TAB)`. |
| Hover or drag-and-drop? | The `Actions` class: `moveToElement`, `dragAndDrop`, `contextClick`, then `.perform()`. |
| When do you use JavascriptExecutor? | Scrolling, reading values the UI hides, or as a last resort to click. Never to bypass a real user problem. |
| Upload a file? | `sendKeys` the absolute file path to the `<input type="file">`. |
| Take a screenshot? | `((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE)`; Selenium 4 also supports element screenshots. |
| Headless mode? | Chrome/Edge: `--headless=new`; Firefox: `-headless`. |
| `getText()` vs `getAttribute("value")`? | `getText` is visible text; input values come from `getDomProperty("value")` (or `getAttribute`). |
| What is `ExpectedConditions.stalenessOf` for? | Waiting until an old element or page is gone after a navigation or re-render. |

## Debugging scenarios

Use the project to reproduce and explain these failures:

1. **"What causes the search race in this project?"** The search race in Chapter 3: a wait that matched the old page; fixed with `stalenessOf` on the old `<html>`.
2. **"Why can tests interfere during parallel execution?"** The shared `driver` field in Chapter 4: diagnosed from a screenshot showing another test's page; fixed by reading the ThreadLocal every time.
3. **"How do you keep UI tests fast?"** API setup: 0.6 s vs 3.5 s for the same checkout check (Chapter 7).
4. **"Why BigDecimal?"** Money: `0.1 + 0.2` is not `0.3` in floating point; totals are compared with `isEqualByComparingTo`.
5. **"How is this framework organized?"** Layers, ThreadLocal driver, config precedence, page objects returning page objects, no assertions in pages, screenshots on failure, suites for smoke/regression/parallel.

## Exercises

1. **Run it.** Start the shop, run `mvn test`, open `target/surefire-reports/index.html` and `target/cucumber-report.html`.
2. **Break a locator.** Change `testId("cart-total")` to `testId("cart-totals")`. Read the failure message and the screenshot. How long did the wait take before failing, and why?
3. **Feel the race.** In `ProductsPage.search`, replace `clickAndWaitForNewPage` with `click`. Run `SearchTests` ten times. Does it ever fail? Explain why it *can*.
4. **Reproduce the parallel bug.** Re-introduce a `protected WebDriver driver` field in `BaseTest`, run the parallel suite, and find the failing test from its screenshot. Then fix it again.
5. **Add a test.** Logging out returns to the login page, and `/cart` then redirects to login. Write the page-object method and the test.
6. **Data from a file.** Move the invalid-login data into a CSV file and read it in the DataProvider.
7. **Soft assertions.** Check product title, price and category on the product page in one test with `SoftAssertions`.
8. **New scenario.** Add a Gherkin scenario for an empty search ("unicorn") and implement any new steps.
9. **Cross-browser.** Run the smoke suite with `-Dbrowser=edge` and `-Dbrowser=firefox` (if installed).
10. **CI.** Put the workflow from Chapter 7 into a repository and get it green.

---

Back to the [Selenium tutorial index](README.md) · [All tutorials](../README.md)
