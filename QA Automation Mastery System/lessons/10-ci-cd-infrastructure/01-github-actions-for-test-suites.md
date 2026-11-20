# GitHub Actions for test suites

[10 · Module topics](../../modules/10-ci-cd-infrastructure.md) · [Curriculum index](../../README.md)

**Category** · PIPELINES

Trigger on PR, install, cache, run sharded, upload reports as artifacts. Your suite isn't real until it blocks a merge.

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
      - run: pytest -m smoke -n auto --alluredir=results
      - uses: actions/upload-artifact@v4
        if: failure()
        with: { name: allure-results, path: results }
````

## Exercise

Wire your suite into a PR workflow with 2 shards and an HTML report artifact.
