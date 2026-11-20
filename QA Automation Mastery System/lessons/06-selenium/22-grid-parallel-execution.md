# Grid & parallel execution

[06 · Module topics](../../modules/06-selenium.md) · [Curriculum index](../../README.md)

**Category** · STRUCTURE & SCALE

Selenium Grid 4 (hub/node or fully distributed) lets one suite fan out across browsers and machines. Locally, pytest-xdist or TestNG parallel gets you most of the value.

## Worked reference

````text
# Remote driver against a Grid
driver = webdriver.Remote(
    command_executor="http://grid.local:4444",
    options=webdriver.ChromeOptions())

# Parallel locally:
#   pip install pytest-xdist
#   pytest -n 4
````

## Exercise

Run your suite on 4 workers with pytest -n 4. Fix any test that fails only in parallel — that's shared state.
