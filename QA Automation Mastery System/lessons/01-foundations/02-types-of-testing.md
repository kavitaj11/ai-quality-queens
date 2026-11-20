# Types of testing

[01 · Module topics](../../modules/01-foundations.md) · [Curriculum index](../../README.md)

**Category** · TESTING FUNDAMENTALS

The map of the discipline. Two big axes: FUNCTIONAL (does it do the right thing?) vs NON-FUNCTIONAL (how well?), plus the LEVEL it runs at (unit→integration→system→acceptance) and the APPROACH (manual/automated, black/white/grey-box). Automation engineers mostly write functional tests across levels, but you'll be asked to place any test type on this map — and to know smoke vs sanity vs regression cold.

## Worked reference

````text
TYPES OF TESTING — the map

┌─ BY LEVEL (where in the stack) ────────────────────────────┐
  Unit          one function/class in isolation (mocks deps)
  Integration   modules/services talking to each other, real DB
  System / E2E  the whole app end-to-end, like a real user
  Acceptance    meets business requirements (often UAT / BDD)

┌─ FUNCTIONAL (does it do the RIGHT thing?) ─────────────────┐
  Smoke         "is the build even alive?" — a few critical paths,
                run first; if it fails, stop and reject the build
  Sanity        narrow, deep check of ONE fixed area after a change
  Regression    re-run existing suite to prove nothing broke
  Retesting     re-run the exact tests that failed, after a fix
  UAT           real users/business verify it solves their problem

┌─ NON-FUNCTIONAL (how WELL does it do it?) ─────────────────┐
  Performance   speed & responsiveness (p95/p99 latency)
    ├ Load       behavior under expected traffic
    ├ Stress     push past the ceiling to find the break point
    ├ Spike      sudden burst of load
    └ Soak       sustained load over hours (memory leaks)
  Security      auth/z, injection, XSS, OWASP Top 10
  Usability     is it intuitive? (often manual/qualitative)
  Accessibility WCAG / a11y — screen readers, contrast, keyboard
  Compatibility cross-browser, cross-device, cross-OS
  Reliability   uptime, failover, recovery

┌─ BY APPROACH / VISIBILITY ─────────────────────────────────┐
  Manual        a human executes; exploratory & ad-hoc live here
  Automated     code executes (this course) — fast, repeatable
  Black-box     test behavior only, no source knowledge
  White-box     test with code knowledge (branches, paths)
  Grey-box      some internal knowledge (APIs, DB) — most QA autом.

WHERE AUTOMATION FOCUSES:
  Mostly functional, across unit → integration → e2e (the pyramid),
  plus automated performance, security smoke, and a11y checks.
````

## Exercise

For a checkout feature, name one concrete test you'd write in each category: unit, integration, e2e, smoke, regression, performance, security, accessibility.
