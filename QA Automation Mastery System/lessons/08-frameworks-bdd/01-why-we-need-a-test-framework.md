# Why we need a test framework

[08 · Module topics](../../modules/08-frameworks-bdd.md) · [Curriculum index](../../README.md)

**Category** · FOUNDATION

A framework exists to turn ad-hoc scripts into a maintainable SYSTEM: without one, every test re-implements setup, locators, waits, and reporting — a UI change breaks 40 tests in 40 places. A framework centralizes those concerns so a change happens once. It's infrastructure investment, justified the same way any engineering investment is: by the cost it removes downstream.

## Worked reference

````text
WHY A TEST FRAMEWORK — the problem it solves

  WITHOUT A FRAMEWORK (scripts, not a system)
  test1.py: driver = webdriver.Chrome(); login inline;
            find_element(By.ID, "submit"); no cleanup
  test2.py: COPY-PASTED login; different wait style;
            different assertion style
  test50.py: same locator, drifted after a UI change —
            fix it in 50 places or miss some

  WHAT A FRAMEWORK CENTRALIZES
  setup/teardown  ONE driver factory / fixture
  locators        ONE page object per screen
  waits           ONE wait strategy, reused
  data            ONE factory/provider layer
  reporting       ONE format, one dashboard
  config          ONE place envs/creds resolve

  THE ECONOMIC ARGUMENT
  cost of NO framework = maintenance cost × test
  count × change frequency — it's O(n); a framework
  makes locator/setup changes O(1)

  A FRAMEWORK IS INFRASTRUCTURE
  judged like any infra investment: what recurring
  cost does it remove, and does that exceed the
  cost to build and maintain it? (see ROI lesson)

  THE RULE OF THUMB
  if you're copy-pasting test setup for the 3rd
  time, you're already building a framework badly —
  do it on purpose instead.
````

## Exercise

Take 3 raw Selenium/Playwright scripts with no shared structure. List every piece of duplicated logic across them — that list IS the case for a framework.
