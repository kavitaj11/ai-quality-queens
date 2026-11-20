# Projects, parallelism & sharding

[07 · Module topics](../../modules/07-playwright.md) · [Curriculum index](../../README.md)

**Category** · SCALE & STRUCTURE

Fully parallel by default, per-file workers, cross-browser via projects, and --shard for splitting across CI machines. Suite architecture is config, not code.

## Worked reference

````text
# pytest-playwright: browsers & workers via CLI
#   pytest --browser chromium --browser webkit -n 4
#
# Device emulation per test:
@pytest.mark.parametrize("browser_context_args", [
    {"viewport": {"width": 393, "height": 852}},  # Pixel 7
], indirect=True)
def test_mobile_nav(page):
    ...
````

## Exercise

Configure chromium/firefox/webkit projects and shard the run 1/2 + 2/2 across two terminal sessions.
