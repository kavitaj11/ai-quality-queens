# Organizing suites: tags, hooks & data-driven

[07 · Module topics](../../modules/07-playwright.md) · [Curriculum index](../../README.md)

**Category** · TEST DESIGN

The remaining design tools: tag tests (@smoke/@regression) and grep them in CI, group with describe + shared hooks, skip/fixme with reasons, and generate data-driven tests in a loop. Steps (test.step) structure long flows in the report. Convention: one behavior per test, no test depends on another.

## Worked reference

````text
# pytest-playwright equivalents
import pytest

# tags = markers;  pytest -m smoke
@pytest.mark.smoke
def test_checkout_completes(page): ...

# data-driven = parametrize
@pytest.mark.parametrize("code,total", [
    ("WELCOME10", "$449.10"),
    ("HALFOFF",   "$249.50"),
    ("EXPIRED",   "$499.00"),
])
def test_discount(page, code, total):
    apply_code(page, code)
    expect(page.get_by_test_id("total")).to_have_text(total)

# skip with a reason
@pytest.mark.skip(reason="flaky on webkit — BUG-123")
def test_legacy(page): ...
````

## Exercise

Tag your suite into @smoke and @regression, convert one repetitive test into a data-driven loop, and wrap a checkout flow in test.step blocks.
