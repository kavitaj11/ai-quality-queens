# The framework as a living document

[08 · Module topics](../../modules/08-frameworks-bdd.md) · [Curriculum index](../../README.md)

**Category** · FOUNDATION

A well-built framework isn't just execution machinery — page/flow names, BDD features, and test titles describe the system's actual behavior, kept honest because they run. Unlike a wiki page, a stale living-document test FAILS, forcing an update. This is what 'tests as documentation' means in practice, and why naming discipline is a framework-design concern, not a style nitpick.

## Worked reference

````text
THE FRAMEWORK AS LIVING DOCUMENTATION

  WHY TESTS DOCUMENT BETTER THAN WIKIS
  wiki page:  goes stale silently, nobody notices
  test suite: goes stale LOUDLY — a stale assumption
              shows up as a RED test, forcing a fix
              or an explicit acknowledgment

  WHERE THE DOCUMENTATION LIVES
  Gherkin features   'Given a gold-tier customer...'
                     — read by PMs, not just engineers
  test/method names  test_checkout_fails_when_card_
                     declined() IS a spec sentence
  page/flow names    LoginFlow, CheckoutFlow — the
                     domain vocabulary, in code
  BDD Examples tables  literally the acceptance
                     criteria, executable

  WHAT MAKES IT WORK (or fail)
  • DECLARATIVE names ('an expired session') beat
    mechanical ones ('click #btn then wait 2s')
  • one vocabulary enforced (see DSL-design lesson) —
    synonyms fragment the 'document' into dialects
  • documentation value is a DESIGN GOAL from day
    one, not a side effect you hope emerges

  THE TEST
  hand your feature files to a new PM. Can they
  learn how checkout actually behaves — including
  edge cases — without reading a single line of
  implementation code? That's the bar.
````

## Exercise

Read your test suite's titles/scenario names out loud as if to a new hire. Do they explain what the product DOES? Rewrite 5 that don't.
