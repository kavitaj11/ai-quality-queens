# Test management & metrics

[00 · Module topics](../../modules/00-sdlc-test-engineering.md) · [Curriculum index](../../README.md)

**Category** · TEST ENGINEERING & PROCESS

Running real test efforts means managing cases, runs, defects, and traceability, and reporting quality with honest metrics. Tools: TestRail/Zephyr/Xray for case & run management, Jira for defects, RTM for coverage, Allure/dashboards for trends. Know the useful metrics (and the vanity ones to avoid).

## Worked reference

````text
TEST MANAGEMENT & METRICS

WHAT YOU MANAGE
  TEST CASES     versioned, organized by suite/feature/priority
  TEST RUNS      execution results per build/release
  DEFECTS        logged, triaged, linked to the failing case
  TRACEABILITY   RTM: requirement to case to result (coverage proof)

TOOLS
  case & run mgmt   TestRail · Zephyr · Xray · qTest
  defect tracking   Jira · Azure DevOps · GitHub Issues
  reporting         Allure · ExtentReports · CI dashboards
  automation results feed these tools via JUnit-XML / APIs

USEFUL METRICS (drive decisions)
  • defect DENSITY (defects / area) -- where is the risk?
  • escaped defects (found in prod) -- did we test the right things?
  • automation coverage & pass-rate trend
  • flaky rate (pass-on-retry %) -- suite trust
  • mean time to detect / to fix
  • test execution time -- feedback speed

VANITY METRICS (avoid gaming)
  x raw "number of test cases" (more is not better)
  x 100% code coverage as a goal (coverage is not good assertions)
  x "% tests automated" without value weighting

REPORT LIKE A LEAD
  tie every metric to a decision: ship / hold / where to invest.
  a red trend with a clear cause beats a green vanity number.
````

## Exercise

Design a lightweight test-management setup for a small team: where cases live, how runs are tracked, how defects link to cases, and the 3 metrics you would report.
