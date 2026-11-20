# The Playwright CLI

[07 · Module topics](../../modules/07-playwright.md) · [Curriculum index](../../README.md)

**Category** · SCALE & STRUCTURE

Almost everything you do with Playwright runs through its CLI — knowing the flags is half the daily workflow. Install browsers, run/filter/debug tests, record with codegen, open the trace viewer and UI mode, update visual baselines, and scaffold CI. This is the JS/TS `npx playwright` surface; Python uses `playwright` + `pytest` flags and Java uses Maven, both noted below.

## Worked reference

````text
# ── SETUP ──
npm init playwright@latest        # scaffold a new project
npx playwright install            # download browser builds
npx playwright install --with-deps chromium   # +OS libs (CI)

# ── RUN TESTS ──
npx playwright test               # all tests, all projects
npx playwright test login.spec.ts # one file
npx playwright test -g "checkout" # filter by title (grep)
npx playwright test --grep @smoke # by tag
npx playwright test --project=chromium        # one browser
npx playwright test --headed      # watch it run
npx playwright test --workers=4   # parallelism
npx playwright test --repeat-each=20          # flake hunting
npx playwright test --retries=2
npx playwright test --last-failed # rerun only what failed

# ── DEBUG ──
npx playwright test --debug       # step with the Inspector
npx playwright test --ui          # UI Mode: time-travel runner
PWDEBUG=1 npx playwright test      # pause + inspector

# ── RECORD & INSPECT ──
npx playwright codegen shop.dev   # record -> generated test
npx playwright show-report        # open the HTML report
npx playwright show-trace trace.zip           # trace viewer

# ── VISUAL BASELINES ──
npx playwright test --update-snapshots

# ── OTHER ──
npx playwright --version
npx playwright test --list        # list tests without running

# ── PYTHON equivalent ──
#   playwright install
#   pytest --headed --browser webkit -n 4
#   pytest --tracing retain-on-failure
#   playwright codegen shop.dev
#
# ── JAVA equivalent ──
#   mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI \
#       -D exec.args="install"
#   mvn test -Dtest=CheckoutTest
````

## Exercise

Run only @smoke tests in Chromium headed with 4 workers and a trace, then open the trace of the last failure and record a new flow with codegen.
