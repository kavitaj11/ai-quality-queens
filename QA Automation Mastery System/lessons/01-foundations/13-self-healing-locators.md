# Self-healing locators

[01 · Module topics](../../modules/01-foundations.md) · [Curriculum index](../../README.md)

**Category** · AI-ASSISTED QA

When the DOM changes, healing finds the closest surviving element instead of failing — Healenium in Java, ranked fallback chains anywhere else. Healing is a bridge, not a fix: review heal reports and repair the real locator.

## Worked reference

````text
SELF-HEALING LOCATORS — theory

THE IDEA
  When the DOM changes and a locator breaks, "healing" finds the
  closest surviving element from a stored history instead of
  failing — keeping the test green through minor UI churn.

HOW IT WORKS
  • the tool records successful locator + DOM snapshots over time
  • on a break, it scores candidate nodes by similarity and
    substitutes the best match, logging the heal

THE TOOLS
  Healenium  open-source proxy around Selenium WebDriver (Java-first)
  Testim / mabl / Functionize  ML "smart locators" in commercial
  DIY        a ranked fallback-locator chain (testid → css → text)
  Note: Selenium & Playwright have NO built-in healing.

THE RISK (why it's a bridge, not a fix)
  silent healing can MASK a real, user-facing regression. Always:
  • log every heal and REVIEW it
  • repair the real locator in code
  • alert if heal frequency spikes

  → Healenium + fallback-chain code lives in the AI-Augmented QA phase.
````

## Exercise

Build a fallback-chain finder with 3 ranked selectors. Rename the element's id and confirm the test still passes — and logs which fallback fired.
