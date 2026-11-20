# Debugging test failures

[02 · Module topics](../../modules/02-test-harness-setup.md) · [Curriculum index](../../README.md)

**Category** · LANGUAGE ESSENTIALS

The fastest QA engineers debug with tools, not print statements: pdb and pytest flags, the Playwright inspector and trace viewer, VS Code breakpoints, and failure-time screenshots in Java.

## Worked reference

````text
# Drop into the debugger where a test fails
def test_checkout(page):
    page.goto("/checkout")
    breakpoint()              # pdb: inspect live state

# pytest flags you'll use daily
#   pytest -x --pdb       drop into pdb on first failure
#   pytest --lf           re-run only last failures
#   pytest -k "checkout"  run matching tests only
#   pytest -s             show print() output

# Rich failure context beats a bare assert
def test_totals(cart):
    assert cart.total == 499, f"cart was: {cart!r}"
````

## Exercise

Break a passing test on purpose, then diagnose it three ways: debugger breakpoint, trace/screenshot artifact, and log output. Time each.
