# How frameworks reduce test complexity

[08 · Module topics](../../modules/08-frameworks-bdd.md) · [Curriculum index](../../README.md)

**Category** · FOUNDATION

Complexity reduction comes from layering and abstraction: raw driver calls hide behind page objects, page objects hide behind flows, flows hide behind readable test names. Each layer only knows about the one below it. A locator change touches one page object; a business-rule change touches one flow. This is the same discipline as good software architecture, applied to tests.

## Worked reference

````text
HOW FRAMEWORKS REDUCE COMPLEXITY — layering

  FLAT (no framework) — complexity is everywhere
  driver.findElement(By.id("email")).sendKeys(...)
  driver.findElement(By.id("pwd")).sendKeys(...)
  driver.findElement(By.cssSelector(".btn")).click()
  wait.until(ExpectedConditions.visibilityOf(...))
  assert driver.findElement(By.id("welcome"))...
  → repeated in every test that touches login

  LAYERED (framework) — complexity is LOCALIZED
  test:   loginPage.loginAs(user).shouldSeeDashboard()
  flow:   LoginFlow — orchestrates multi-step business
          logic across page objects
  page:   LoginPage — owns ONLY login-screen locators
          + waits; changes here don't leak elsewhere
  core:   driver factory, wait utils — used by every page

  WHAT EACH LAYER IS ALLOWED TO KNOW
  test    → flows/pages only (never raw driver)
  flow    → page objects only
  page    → its own locators + the driver utils
  → a locator rename touches exactly ONE file

  COMPLEXITY BUDGET
  a framework doesn't remove complexity — it
  CONTAINS it, so a change's blast radius shrinks
  from 'the whole suite' to 'one class'

  THE TEST FOR GOOD LAYERING
  can a non-expert read the TEST file and
  understand the scenario without reading the
  page objects? If not, layering leaked upward.
````

## Exercise

Take one long, flat test (20+ raw driver calls). Refactor it into layers: page object methods, then a flow, then a 5-line test. Count how many places a locator change now touches.
