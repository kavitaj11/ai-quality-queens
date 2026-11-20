# Selenium vs Playwright

[07 · Module topics](../../modules/07-playwright.md) · [Curriculum index](../../README.md)

**Category** · PLAYWRIGHT CORE

The comparison every team (and interviewer) asks for, now from the Playwright side: protocol and architecture differences, what Playwright bakes in that Selenium bolts on, where Selenium still wins, and the same login test side-by-side so the ergonomics are obvious.

## Worked reference

````text
SELENIUM vs PLAYWRIGHT

ARCHITECTURE
                   SELENIUM                PLAYWRIGHT
  protocol         W3C WebDriver (HTTP,    CDP / own protocol
                   request→response)       (WebSocket, events)
  browsers         anything with a         bundled Chromium /
                   driver + real devices   Firefox / WebKit
  setup            driver binaries         npx playwright install
                   (Selenium Manager)      (version-matched)

BUILT-IN vs BOLT-ON
  auto-wait        ✗ explicit waits        ✓ every action
  retrying asserts ✗ write your own        ✓ web-first expect()
  network mocking  ~ CDP/BiDi only         ✓ page.route()
  auth reuse       DIY cookies             ✓ storageState
  test runner      pick one (pytest…)      ✓ built in (JS/TS)
  parallelism      runner/Grid             ✓ workers built in
  trace/debugging  screenshots + logs      ✓ trace viewer, UI mode
  fixtures/DI      pytest only             ✓ test.extend

WHERE SELENIUM STILL WINS
  • real-device / legacy browser coverage (incl. old Safari, IE
    modes) and the Grid / cloud-lab ecosystem
  • more languages (Ruby, PHP, C#…), deepest Stack Overflow
    history, most enterprise suites already written
  • W3C standard = vendor-neutral, many wrappers (Selenide…)

WHERE PLAYWRIGHT WINS
  • speed + stability out of the box (auto-wait, isolation)
  • network control, tracing, parallelism with zero plumbing
  • one API across 3 engines; mobile emulation built in

CHOOSING (rule of thumb)
  greenfield web app            → Playwright
  legacy/enterprise + device lab → Selenium
  huge existing Selenium suite  → don't rewrite; add Playwright
                                   for new critical journeys
````

## Exercise

Port one of your Selenium tests to Playwright. Count the lines that disappeared and name exactly what absorbed each one (auto-wait, fixture, storageState, locator).
