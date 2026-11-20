# Quality gates & reporting

[02 · Module topics](../../modules/02-test-harness-setup.md) · [Curriculum index](../../README.md)

**Category** · CI/CD FOR TEST SUITES

Green must mean something: machine-readable results, merge-blocking required checks, PR smoke vs nightly regression, and trend history so flakes and slow tests surface before they rot the suite.

## Worked reference

````text
# Machine-readable output is the contract with CI
#   pytest --junitxml=results.xml \
#          --alluredir=allure-results

# Split by cost:
#   pytest -m smoke        on every PR  (< 5 min)
#   pytest -m regression   nightly, full matrix

# Branch protection: make the check REQUIRED —
# a red suite blocks merge, no exceptions.
# Allure history: flake + duration trends per test
````

## Exercise

Define your gate ladder: what runs on PR, what runs nightly, and the exact numbers (max duration, flake %) that block a merge.
