# Healenium: self-healing for Selenium

[14 · Module topics](../../modules/14-ai-augmented-qa.md) · [Curriculum index](../../README.md)

**Category** · SELF-HEALING & AI PLATFORMS

An open-source proxy around WebDriver: it records successful locator/DOM history and, when a locator breaks, substitutes the closest historical match (tree-comparison scoring) — logging every heal for review. Drops into an existing Selenium suite.

## Worked reference

````text
# Healenium runs as a sidecar (docker-compose):
#   hlm-backend + postgres store locator history
#
# Python flow mirrors Java:
# 1. wrap your Selenium driver with the proxy
# 2. run the suite — broken locators get healed
#    from history and LOGGED, never silent
# 3. review the heal report; accept or fix
# github.com/healenium
````

## Exercise

Wrap an existing Selenium suite with Healenium, rename 3 locators in the app, and review the healing report.
