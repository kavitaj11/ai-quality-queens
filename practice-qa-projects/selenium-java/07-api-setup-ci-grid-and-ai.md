# 7. API Setup, CI/CD, Grid and AI-Assisted Testing

## Set up through the API, test through the UI

UI tests are slow and fragile mostly because of **setup**: logging in and filling a cart through the UI before getting to the behaviour you actually want to test. `api/ShopApi.java` logs in and adds to the cart over HTTP, then gives the session cookie to the browser:

```java
new ShopApi().login("api-user", Config.password())
        .addToCart(1001, 2)
        .addToCart(1006, 1)
        .applySessionTo(driver());          // add the "sid" cookie to the browser

CartPage cart = new CartPage(driver()).open();   // start the UI where the test begins
```

Measured in this project's test report:

| Test | Setup | Time |
|---|---|---|
| `customerCanBuyAProductEndToEnd` | Everything through the UI | **3.5 s** |
| `checkoutTotalsAMixedCart` | Login and cart through the API | **0.6 s** |

Both test checkout. Keep a few full UI journeys, and use API setup for everything else. (A cookie can only be added for the domain currently open in the browser, which is why `applySessionTo` loads `/health` first.)

## Running in CI/CD

A minimal GitHub Actions job for this project:

```yaml
jobs:
  selenium:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with: { distribution: temurin, java-version: "21", cache: maven }
      - uses: actions/setup-python@v5
        with: { python-version: "3.12" }
      - name: Start the app under test
        run: |
          python practice-qa-projects/practice-apps/shop-web/shop_web.py &
          for i in $(seq 1 20); do curl -sf http://127.0.0.1:8091/health && break; sleep 1; done
      - name: Smoke tests
        working-directory: practice-qa-projects/selenium-java/project
        run: mvn -B test -Dsuite=testng-smoke.xml -Dheadless=true
      - uses: actions/upload-artifact@v4
        if: always()
        with:
          name: selenium-reports
          path: |
            practice-qa-projects/selenium-java/project/target/surefire-reports
            practice-qa-projects/selenium-java/project/target/screenshots
```

GitHub's Ubuntu runners include Chrome, and Selenium Manager fetches the driver. A common layout: smoke suite on every pull request, full regression nightly, cross-browser before release. (This workflow is an example; it hasn't been run in GitHub as part of this tutorial.)

## Selenium Grid and cross-browser testing

Run a local Grid with Docker:

```bash
docker run -d -p 4444:4444 --shm-size=2g selenium/standalone-chrome
mvn test -DgridUrl=http://localhost:4444
```

`DriverFactory` switches to `RemoteWebDriver` whenever `gridUrl` is set, so tests don't change. For many browsers or OS versions, use a Grid with several nodes, or a cloud provider (Sauce Labs, BrowserStack, LambdaTest) at the same URL-based switch. (If the app runs on your machine and the browser in Docker, the browser must reach the app via `host.docker.internal`, not `127.0.0.1`.)

## AI-assisted Selenium testing

Where AI genuinely helps today:

- **Writing code faster:** GitHub Copilot or Claude Code drafting page objects, data providers and step definitions from your existing patterns.
- **Test design:** turning a user story into scenarios and boundary cases, which you then review.
- **Self-healing locators:** when a locator breaks, a tool proposes a replacement based on the element's other attributes, with a confidence score. For example, [k11techlab-selenium-qa-mcp](https://github.com/K11-Software-Solutions/k11techlab-selenium-qa-mcp) offers verified locators, user story to test, and self-healing with an audit trail through an MCP server for Claude Code.
- **Failure triage:** summarizing a failed run (stack trace, screenshot, logs) into a likely cause.

Recommended engineering guardrails:

- Review AI-written tests like a junior engineer's pull request; check that the assertion tests the requirement, not just what the code does.
- A healed locator must be reviewed and committed, not silently applied on every run, or real bugs (a missing button) get "healed" away.
- Never put credentials or customer data into prompts.

## Review questions

**How do you make UI tests faster?**
API-based setup, parallel execution, headless browsers in CI, pushing most coverage to API and unit tests, and keeping only critical journeys end to end in the UI.

**How do you run Selenium tests in CI?**
Headless browser, app started and health-checked first, smoke suite on pull requests and full regression nightly, reports and screenshots uploaded as artifacts.

**What is Selenium Grid?**
A hub that routes WebDriver sessions to browser nodes on other machines or containers, used for parallel and cross-browser runs. In code you switch from `ChromeDriver` to `RemoteWebDriver` with the Grid URL.

**What is a self-healing locator, and what's the risk?**
A tool that finds a replacement when a locator breaks. The risk is hiding a real regression, so healed locators should be proposed with evidence and reviewed before they're accepted.

Next: [8. Review questions and exercises](08-review-questions-and-exercises.md)
