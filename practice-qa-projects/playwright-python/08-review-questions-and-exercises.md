# 8. Review Questions and Exercises

## Rapid-fire questions

| Question | Short answer |
|---|---|
| Browser vs context vs page? | Process / isolated session / tab. One browser per run, one context per test. |
| How do you handle a new tab or popup? | `with context.expect_page() as info: link.click()`, then `new_page = info.value`. |
| How do you handle a JavaScript dialog? | `page.on("dialog", lambda d: d.accept())` before the action that opens it. |
| How do you work inside an iframe? | `page.frame_locator("#frame").get_by_role(...)`. |
| How do you upload a file? | `locator.set_input_files("path/to/file")`. |
| How do you download a file? | `with page.expect_download() as info: click()`, then `info.value.save_as(...)`. |
| Select from a `<select>`? | `locator.select_option("2")` (value, label or index). |
| Hover / drag? | `locator.hover()`, `source.drag_to(target)`. |
| Default timeouts? | 30 s for actions/navigation, 5 s for `expect()`; this project sets 10 s for actions per context. |
| Screenshot an element? | `locator.screenshot(path=...)`; full page: `page.screenshot(full_page=True)`. |
| Mobile emulation? | `browser.new_context(**playwright.devices["iPhone 13"])`. |
| Visual comparison? | Built into the Node.js test runner (`toHaveScreenshot`); in Python, use a plugin or compare screenshots yourself. |

## Debugging scenarios

1. **"How can a search assertion match stale page state?"** The search navigation race (Chapter 2): the old page satisfied the assertion; fixed by asserting the new URL first.
2. **"How do you debug CI-only failures?"** Traces retained on failure and uploaded as artifacts; the negative control in Chapter 6 proved they're produced.
3. **"How do you make UI tests fast?"** API setup through `context.request`: 0.31 s vs 4.62 s for the same checkout check.
4. **"How can a UI error state be tested deterministically?"** `page.route(...).fulfill(status=500)` and assert the friendly message (Chapter 5).
5. **"What can cause a pytest command-line option conflict?"** The `--base-url` clash with an installed pytest plugin (Chapter 3): pytest plugins are environment-wide, so use a virtual environment per project.

## Exercises

1. **Run it.** Start the shop and run `pytest`. Then `pytest -m smoke --browser-name webkit`.
2. **Read a trace.** Change an expected value in `test_search.py`, run it, and open the trace. Find the network call to `/api/products` and its response.
3. **Feel the race.** Remove the `to_have_url` line from `ProductsPage.search` and run `test_search_counts` many times. Explain why it *can* pass with the wrong data.
4. **Role locators.** Replace `get_by_test_id("search-button")` with a role-based locator and confirm the tests still pass.
5. **New test.** After logging out, `/cart` redirects to login. Add a `logout()` page method and a test.
6. **Mock an empty catalogue.** Route `/api/products` to return `{"items": [], "count": 0}` and assert the "no results" message.
7. **Abort the request.** Use `route.abort()` and check what the page shows. Is that the right user experience?
8. **API test.** Add a test that adding the same product twice increases the quantity and caps it at 5.
9. **Storage state.** Save a logged-in `storage_state` once per session and use it to create contexts.
10. **Parallel.** Install `pytest-xdist` and run `pytest -n 4`. Is anything flaky? Why not?

---

Back to the [Playwright tutorial index](README.md) · [All tutorials](../README.md)
