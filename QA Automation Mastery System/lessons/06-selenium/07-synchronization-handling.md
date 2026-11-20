# Synchronization handling

[06 · Module topics](../../modules/06-selenium.md) · [Curriculum index](../../README.md)

**Category** · WEBDRIVER CORE

The mental model before the code. A test and the app run at different speeds — the test fires commands instantly, but the browser needs time to render, fetch, and animate. Synchronization is closing that gap. This overview maps the three approaches (hard sleep, implicit wait, explicit wait) and when each fires, so the wait code in the next topics makes sense.

## Worked reference

````text
THE SYNCHRONIZATION PROBLEM

  TEST  ──click──▶ ──assert text──▶  (fires instantly)
  APP   ──────────[ AJAX fetch + render 900ms ]──────▶
                                    ▲
                          test asserts HERE, before the
                          element exists  →  FLAKY FAILURE


THREE WAYS TO CLOSE THE GAP

1) HARD SLEEP  — time.sleep(3)                    ✗ avoid
   ├────────── fixed 3s ──────────┤
   • too short → still flaky      • too long → slow suite
   • waits the SAME time whether ready in 50ms or never

2) IMPLICIT WAIT — driver.implicitly_wait(10)     ~ limited
   • a GLOBAL retry window applied to find_element
   • polls the DOM until the element is PRESENT or timeout
   • only covers "is it in the DOM?" — not visible, not
     clickable, not "text changed". Set to 0 if using explicit.

3) EXPLICIT WAIT — WebDriverWait(driver,10).until(cond)   ✓ use
   ├─ poll ─┬─ poll ─┬─ ✓ condition true → continue immediately
   0.25s    0.5s     0.75s        (returns the moment it's ready)
   • waits for a SPECIFIC condition (visible / clickable /
     text present / staleness / your own custom function)
   • fast when ready, patient when slow, precise every time


DECISION RULE
   need to wait?  →  ALWAYS explicit wait with the most
                     specific condition for THAT case.
   sleep()        →  debugging only, never in a real test.
   implicit+explicit together → NEVER (compounding timeouts).


         auto-wait note: Playwright & Cypress build the
         explicit-wait behavior into every action, so you
         rarely write waits by hand — Selenium makes it explicit.
````

## Exercise

Draw the timeline of a click that triggers an AJAX load. Mark where a sleep, an implicit wait, and an explicit wait each succeed or waste time.
