# Pipelines: tests on every PR

[02 · Module topics](../../modules/02-test-harness-setup.md) · [Curriculum index](../../README.md)

**Category** · CI/CD FOR TEST SUITES

A suite that doesn't run on every pull request doesn't exist. The universal shape: checkout → setup runtime → install pinned deps → run with machine-readable output → upload artifacts on failure.

## Worked reference

````text
# .github/workflows/tests.yml (Python)
name: tests
on: [pull_request]
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-python@v5
        with: { python-version: '3.12' }
      - run: pip install -r requirements.txt
      - run: playwright install --with-deps chromium
      - run: pytest -m smoke --junitxml=results.xml
      - uses: actions/upload-artifact@v4
        if: failure()
        with: { name: artifacts, path: artifacts/ }
````

## Exercise

Set up a GitHub Actions workflow that runs your smoke suite on every PR and uploads the report as an artifact when it fails.
