# The test automation system

[06 · Module topics](../../modules/06-selenium.md) · [Curriculum index](../../README.md)

**Category** · STRUCTURE & SCALE

Zoom out: a real automation 'system' is all the layers working together — not just test scripts. Config (env/secrets), a driver/client factory, page objects + API clients, the tests themselves, test data, the runner, reporting, and CI wiring. This is the mental model behind the 'design a framework' interview question, and how a suite scales from 10 tests to 10,000.

## Worked reference

````text
THE TEST AUTOMATION SYSTEM — layers, top to bottom

  ┌─ CI / CD ────────────────────────────────────────────┐
  │  Jenkins / GitHub Actions — trigger on PR, matrix,    │
  │  publish report artifact, gate the merge              │
  └───────────────┬───────────────────────────────────────┘
                  ▼  invokes the runner
  ┌─ TEST RUNNER ─────────────────────────────────────────┐
  │  pytest / TestNG — discover, lifecycle, parallel,     │
  │  retries, produce results                             │
  └───────────────┬───────────────────────────────────────┘
                  ▼
  ┌─ TESTS ───────────────────────────────────────────────┐
  │  test_checkout.py — arrange/act/assert, one behavior  │
  └───────────────┬───────────────────────────────────────┘
                  ▼  call intent, not raw Selenium
  ┌─ PAGE OBJECTS  +  API CLIENTS ───────────────────────┐
  │  LoginPage.login() · OrderApi.create() — reusable    │
  └───────────────┬───────────────────────────────────────┘
                  ▼
  ┌─ CORE / DRIVER FACTORY  +  WAITS  +  WRAPPERS ────────┐
  │  build the driver, safeClick/safeType, config-driven  │
  └───────────────┬───────────────────────────────────────┘
                  ▼
  ┌─ SUPPORT LAYERS ──────────────────────────────────────┐
  │  config/  env, browsers, secrets (never in code)      │
  │  data/    factories, fixtures, test users             │
  │  utils/   unique ids, files, JSON, logging            │
  │  reporting/ Allure / ExtentReports                    │
  └───────────────────────────────────────────────────────┘

  FOLDER LAYOUT
    config/   core/   pages/   clients/   tests/
    data/     utils/   reports/   requirements.txt|pom.xml
    .github/workflows/tests.yml    conftest.py|testng.xml

  ONE TEST'S JOURNEY
    PR opened → CI runs pytest -m smoke -n 4 → runner builds
    drivers via factory → test calls LoginPage.login() →
    assertion → results → Allure report published as artifact
    → red gate blocks merge, green lets it through.
````

## Exercise

Sketch the folder structure for a Selenium framework with every layer named, then trace one test from CI trigger to published report.
