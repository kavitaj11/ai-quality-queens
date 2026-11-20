# Limitations & Selenium vs Playwright

[06 · Module topics](../../modules/06-selenium.md) · [Curriculum index](../../README.md)

**Category** · BEYOND THE BASICS

Senior interviews probe whether you know the tool's edges. Selenium only drives real browsers over HTTP WebDriver — no built-in waits, no network mocking, no desktop/mobile-native, no built-in reporting, and it needs driver binaries. Playwright bakes in auto-waiting, network interception, tracing, and parallelism. Neither is 'better'; know when each wins.

## Worked reference

````text
WHAT SELENIUM CANNOT DO (by itself)
  ✗ auto-wait            → you write explicit waits
  ✗ mock/intercept network (CDP/BiDi help, but not core)
  ✗ built-in reporting   → bolt on Allure/Extent
  ✗ test desktop / native-mobile apps (use Appium/WinAppDriver)
  ✗ handle browser dialogs for downloads/uploads natively
  ✗ run without a matching driver binary + browser version


SELENIUM  vs  PLAYWRIGHT

                    SELENIUM              PLAYWRIGHT
  Protocol          W3C WebDriver (HTTP)  CDP / own (WebSocket)
  Auto-wait         no (manual waits)     YES (every action)
  Browsers          all + real devices    Chromium/Firefox/WebKit
  Languages         Py/Java/JS/C#/Ruby..  Py/JS/TS/Java/C#
  Network mocking    limited (CDP)          first-class
  Tracing/video      add-on                 built-in
  Parallelism        via runner/grid        built-in
  Maturity/ecosystem largest, oldest        newer, fast-growing
  Setup              driver binaries        bundled browsers

WHEN SELENIUM WINS
  • widest browser + REAL-device / legacy (IE, Safari) coverage
  • huge existing suite, team skills, enterprise Java stack
  • Grid / cloud-lab ecosystem (BrowserStack, Sauce)

WHEN PLAYWRIGHT WINS
  • greenfield web app; want speed + low flakiness out of the box
  • need network mocking, tracing, auto-wait without plumbing
  • modern JS/TS team

  Rule of thumb: NEW web project → Playwright.
  Broad/legacy/enterprise coverage → Selenium.
````

## Exercise

Write a one-paragraph recommendation: given a legacy enterprise Java shop needing IE11 + SAP coverage vs a greenfield web app team, which tool for each and why?
