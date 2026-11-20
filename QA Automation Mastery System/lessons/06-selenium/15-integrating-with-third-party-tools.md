# Integrating with third-party tools

[06 · Module topics](../../modules/06-selenium.md) · [Curriculum index](../../README.md)

**Category** · BEYOND THE BASICS

Selenium is only the browser layer — real suites plug it into a stack: a runner (pytest/TestNG), reporting (Allure, ExtentReports), a Grid or cloud lab (Selenium Grid, BrowserStack, Sauce Labs, LambdaTest) for cross-browser at scale, CI (Jenkins/GitHub Actions), and BDD (Cucumber). This overview maps the ecosystem and the seams that join it.

## Worked reference

````text
THE SELENIUM ECOSYSTEM — what plugs into what

                    ┌──────────────────────────┐
   TEST RUNNER  ───▶│   Your Selenium tests    │◀─── BDD LAYER
   pytest /         │  (page objects + waits)  │     Cucumber /
   TestNG / JUnit   └────────────┬─────────────┘     behave
                                 │ RemoteWebDriver + capabilities
                                 ▼
                  ┌──────────────────────────────┐
   EXECUTION      │  Grid / Cloud device lab      │
                  │  Selenium Grid (self-host)    │
                  │  BrowserStack / Sauce Labs /  │
                  │  LambdaTest (cross-browser,   │
                  │  parallel, real devices)      │
                  └──────────────┬────────────────┘
                                 │ results + screenshots + video
                                 ▼
   REPORTING      Allure  •  ExtentReports  •  JUnit XML
                                 │
                                 ▼
   CI / CD        Jenkins • GitHub Actions • GitLab CI
                  (trigger on PR, publish report artifact)

REMOTE EXECUTION IN ONE LINE
   # local:   webdriver.Chrome()
   # remote:  webdriver.Remote(command_executor=GRID_URL,
   #              options=ChromeOptions())
   Java:  new RemoteWebDriver(new URL(gridUrl), options);

WHY: run the SAME tests across 20 browser/OS combos in
parallel without owning the machines.
````

## Exercise

Point your existing tests at a remote grid via RemoteWebDriver + capabilities, then wire Allure results into your CI job as an artifact.
