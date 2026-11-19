# 6. Debugging, Tracing, Parallel Runs and CI

## Traces: the best debugging tool in UI automation

The `context` fixture records a trace for every test and **keeps it only when the test fails**:

```python
context.tracing.start(screenshots=True, snapshots=True, sources=True)
...
if failed:
    context.tracing.stop(path=out / "trace.zip")
    page.screenshot(path=out / "page-0.png", full_page=True)
else:
    context.tracing.stop()
```

A deliberately failing test was run to check this. It produced:

```
FAILED ... AssertionError: Locator expected to have text '['Desk Lamp']'
Failure artifacts: ...\test-results\test_should_fail
    page-0.png   trace.zip
```

Open the trace:

```bash
playwright show-trace test-results/test_should_fail/trace.zip
```

The trace viewer shows every action with **before/after DOM snapshots** you can inspect, the **network** requests and responses, **console** logs, and the **source line** of each step. It answers "what did the page look like at the moment it failed?" without re-running anything, which is especially valuable for failures that only happen in CI.

## Other debugging tools

| Tool | How | Use for |
|---|---|---|
| Headed mode | `pytest --headed -k checkout` | Watch what happens |
| Inspector | `PWDEBUG=1 pytest -k checkout` (PowerShell: `$env:PWDEBUG=1`) | Step through actions, try locators live |
| `page.pause()` | Put it in a test temporarily | Stop at a point and explore |
| Codegen | `playwright codegen http://127.0.0.1:8091` | Record actions and get suggested locators |

Codegen is good for **discovering locators**, not for writing your framework: recorded scripts have no page objects, no data handling and no meaningful assertions.

## Parallel runs

Install `pytest-xdist` and run tests across worker processes:

```bash
pip install pytest-xdist
pytest -n 4            # 4 workers, each with its own browser
pytest -n auto         # one worker per CPU core
```

This project is ready for it: every test gets its own context, the practice shop gives every login its own session and cart, and no test depends on another. (Parallel runs weren't executed for this tutorial because `pytest-xdist` isn't installed on the machine it was written on.)

## Cross-browser

```bash
pytest -m smoke --browser-name firefox
pytest -m smoke --browser-name webkit
```

Verified for this tutorial: the 3 smoke tests pass in Chromium, Firefox and WebKit.

## Continuous integration

```yaml
jobs:
  playwright:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-python@v5
        with: { python-version: "3.12" }
      - name: Install dependencies and browser
        working-directory: practice-qa-projects/playwright-python/project
        run: |
          pip install -r requirements.txt
          playwright install --with-deps chromium
      - name: Start the app under test
        run: |
          python practice-qa-projects/practice-apps/shop-web/shop_web.py &
          for i in $(seq 1 20); do curl -sf http://127.0.0.1:8091/health && break; sleep 1; done
      - name: Tests
        working-directory: practice-qa-projects/playwright-python/project
        run: pytest -m "smoke or regression or api or network" --junitxml=results.xml
      - uses: actions/upload-artifact@v4
        if: always()
        with:
          name: playwright-results
          path: |
            practice-qa-projects/playwright-python/project/results.xml
            practice-qa-projects/playwright-python/project/test-results/
```

`--with-deps` installs the OS libraries the browsers need on Linux. Uploading `test-results/` means every CI failure comes with its trace. (This workflow is an example; it hasn't been run in GitHub as part of this tutorial.)

## Review questions

**How do you debug a test that only fails in CI?**
Record traces (retain on failure) and upload them as artifacts; open them with the trace viewer to see the DOM, network and console at the moment of failure.

**What is codegen good for?**
Exploring an app and finding good locators quickly. Not for production test code: it has no structure, no reuse and weak assertions.

**How do you run Playwright tests in parallel with pytest?**
pytest-xdist (`-n 4`). Each worker has its own browser; tests must be independent, which per-test contexts make easy.

**How do you handle flaky tests?**
Find the cause in the trace: usually waiting on the wrong thing (Chapter 2's navigation race), shared data or test order. Fix that. Retries (`pytest-rerunfailures`) hide flakiness; use them only for known environmental issues and track them.

Next: [7. AI-assisted Playwright testing](07-ai-assisted-playwright.md)
