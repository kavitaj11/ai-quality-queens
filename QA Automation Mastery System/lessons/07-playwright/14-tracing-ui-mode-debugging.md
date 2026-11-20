# Tracing, UI mode & debugging

[07 · Module topics](../../modules/07-playwright.md) · [Curriculum index](../../README.md)

**Category** · POWER FEATURES

npx playwright test --ui for time-travel debugging; trace.zip on CI failures gives you DOM snapshots, network, and console for every step. Nobody should debug from a screenshot anymore.

## Worked reference

````text
# Trace via pytest-playwright flags:
#   pytest --tracing retain-on-failure
#   pytest --screenshot only-on-failure --video retain-on-failure

# Or manually:
context.tracing.start(screenshots=True, snapshots=True)
# ... run steps ...
context.tracing.stop(path="trace.zip")
# View:  playwright show-trace trace.zip
````

## Exercise

Force a test to fail on CI settings (trace: 'on-first-retry') and walk the trace in trace.playwright.dev.
