# 8. CI, Debugging and Reliability

## Traces: evidence for every failure

Every test records a Playwright trace; it's saved only on failure, with a full-page screenshot, under `target/playwright-artifacts/<test name>/`. Cucumber also attaches the screenshot to its HTML report.

Open a trace with the Playwright CLI bundled in the Java library:

```bash
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="show-trace target/playwright-artifacts/<test>/trace.zip"
```

(or drop the zip onto trace.playwright.dev). The viewer shows each action with before/after DOM snapshots, network calls and console output, so a CI-only failure can be diagnosed without re-running it.

## Negative controls: prove the suite can fail

A suite that can't fail is worse than no suite. After building this framework, it was run with a wrong password on purpose:

```bash
mvn test -DshopPassword=wrong-on-purpose -DtimeoutMillis=3000
```

Result: **exit code 1**, 8 Cucumber and 1 JUnit failure reported, and a `trace.zip` plus `screenshot.png` for each. The API tests that don't log in passed, as they should.

## A real build problem found by checking results: the suite that reported 0 tests

The first full run with **Surefire 3.5.3** finished with exit code 0, but some Cucumber scenarios had failed (their failure traces were on disk). Surefire's report said:

```
Tests run: 0, Failures: 0, Errors: 0, Skipped: 0 -- in com.k11.pwj.RunCucumberTest
```

With this combination (Surefire 3.5.3, JUnit Platform 1.10.1, Cucumber 7.15), Surefire didn't count the scenarios run by the `@Suite`, so **failing scenarios didn't fail the build**. Switching to **Surefire 3.2.5** reported all 12 scenarios, and the negative control above confirmed failures now fail the build. The `pom.xml` documents why the version is pinned.

Lessons:

- **"Green" means nothing until you've seen the same pipeline go red.** Run a negative control whenever you change the runner, plugins or framework versions.
- **Check the test count**, not just the exit code. A sudden drop to 0 is a failure.

## Jenkins

`Jenkinsfile` (declarative pipeline):

```groovy
agent { docker { image 'mcr.microsoft.com/playwright/java:v1.58.0-noble' } }   // browsers preinstalled
environment {
    SHOP_PASSWORD = credentials('shop-test-password')    // Jenkins credentials, never in code
}
stages {
    stage('Start app under test') { ... health check ... }
    stage('Tests') { steps { dir('practice-qa-projects/playwright-java/project') {
        sh 'mvn -B test -Dcucumber.filter.tags="${TAGS}"' } } }
}
post {
    always {
        junit '.../target/surefire-reports/*.xml'
        archiveArtifacts artifacts: '.../target/cucumber-report.html, .../target/playwright-artifacts/**'
    }
}
```

## GitLab CI

`.gitlab-ci.yml` runs `@smoke` on merge requests and the full suite on a schedule, publishes JUnit results to the merge request, and keeps the Cucumber report and failure traces as artifacts.

> Both pipeline files are examples; they haven't been run in Jenkins or GitLab as part of this tutorial. Use the Playwright Docker image tag that matches the Playwright version in `pom.xml`.

## Git habits for test code

- Feature branches, small commits, merge requests with the pipeline green and a review.
- Never commit secrets, traces, `target/` or database files (`.gitignore` covers them).
- Review test code like product code: does each assertion check the requirement?

## Making the suite reliable

| Practice | In this framework |
|---|---|
| Isolated state per test | New `BrowserContext` per test; per-user sessions in the app |
| Setup through APIs | `ShopSession.ui()` login and cart |
| Wait on the right signal | URL assertions after navigation; retrying assertions |
| Evidence on failure | Trace + screenshot, attached to the report |
| No shared mutable state | `ThreadLocal` Playwright/Browser; PicoContainer per scenario |
| Prove failures fail | Negative control; check the test count |

## Review questions

**How do you debug a test that only fails in CI?**
Traces retained on failure and archived as artifacts; open them in the trace viewer to see the DOM, network and console at each step.

**How do you know your suite can actually catch failures?**
Run a negative control (break something on purpose) and confirm the build goes red with the right count, especially after changing tools or versions.

**How do you keep secrets out of tests?**
CI credentials or masked variables, read through configuration (`SHOP_PASSWORD`); never in code or feature files.

**Tell me about a CI problem you solved.**
Use this chapter's Surefire story: green build, failing scenarios, test count of 0; fixed by pinning the plugin and adding a negative control.

Next: [9. Review questions and exercises](09-review-questions-and-exercises.md)
