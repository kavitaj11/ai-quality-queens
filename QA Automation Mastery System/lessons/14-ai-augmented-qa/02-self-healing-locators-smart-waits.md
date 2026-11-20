# Self-healing locators & smart waits

[14 · Module topics](../../modules/14-ai-augmented-qa.md) · [Curriculum index](../../README.md)

**Category** · SELF-HEALING & AI PLATFORMS

Tools that repair broken selectors from DOM history and ML-ranked fallbacks. Understand the mechanism and the risk: silent healing can mask real regressions.

## Worked reference

````text
# Self-healing pattern (conceptual)
# 1. Primary locator fails: #submit-btn
# 2. Engine scores candidates from DOM history:
#    [data-testid=submit]  0.94  ← heals to this
#    button.btn-primary    0.71
# 3. Heal is LOGGED for review — never silent
#    (a renamed button might be a real bug)
````

## Exercise

Evaluate one self-healing tool on a suite with 5 intentionally broken locators; document what healed correctly vs. wrongly.
