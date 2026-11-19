# Practice QA Projects

Nine hands-on projects for learning quality engineering through runnable code, local practice applications, technical tutorials, and debugging exercises.

## Choose a project

| Project | Technical focus | Main prerequisites |
|---|---|---|
| [SQL and data pipeline testing](data-quality-testing-lab/README.md) | Joins, aggregates, duplicates, reconciliation, and ETL/ELT validation | Python 3.9+ with SQLite |
| [Selenium with Java](selenium-java/README.md) | Page objects, waits, TestNG, Cucumber BDD, parallel execution, API setup | Java 17+, Maven, a supported browser |
| [Appium with Java](appium-java/README.md) | Mobile web, native Android, gestures, sessions, device isolation | Java, Maven, Node.js, Appium; emulator for native tests |
| [Playwright with Python](playwright-python/README.md) | UI/API automation, pytest fixtures, network mocking, tracing | Python, project dependencies, Playwright browsers |
| [Playwright with Java](playwright-java/README.md) | Cucumber BDD, APIRequestContext, JSON schema, SQL/JDBC, CI | Java 17+, Maven, Playwright browsers |
| [JMeter performance testing](performance-testing-jmeter/README.md) | Workload modeling, correlation, SLO gates, bottlenecks, AI-assisted analysis | Java, JMeter, Python for the local service |
| [k6 performance testing](performance-testing-k6/README.md) | Checks, thresholds, open/closed workloads, queue measurements | k6 and Python |
| [AWS and Google Cloud testing](cloud-testing-aws-gcp/README.md) | Storage metadata contracts, offline adapters, optional cloud smoke checks | Python; provider SDKs and authorized credentials only for live checks |
| [AI Quality Engineering](ai-quality-engineering/README.md) | Conversational/agentic evaluation, judge calibration, safety, synthetic data, tools and traces | Python 3.11+; optional API key for live semantic judging |

The [shared practice shop](practice-apps/README.md) supports the Selenium, Appium mobile-web, and Playwright projects. The performance and AI QE labs include their own local services.

## Run the offline labs first

From the repository root:

```bash
cd practice-qa-projects/ai-quality-engineering/project
python -m unittest discover -s tests -v
python -m aiqe run
python -m aiqe compare
```

In a new terminal, from the repository root:

```bash
cd practice-qa-projects/cloud-testing-aws-gcp
python -m unittest discover -s tests -v
```

These commands need no paid model or cloud calls. Use the individual READMEs for browser, mobile and load-test setup. Run all load tests only against local practice services or an explicitly authorized QA environment.

## Folder organization

The Java and Playwright Python frameworks keep their executable files under each tutorial's `project/` directory. The JMeter lab uses `demo-app/`, `jmeter/` and `scripts/`; k6 uses `demo/` and `tests/`; the cloud lab runs from its own directory. The shared `practice-apps/` folder is retained so cross-project setup paths keep working.

Each project keeps its technical chapters, fixtures, dependency declarations and review exercises. Generated reports, local databases, environment files and dependency caches should remain untracked.

## CI templates

- [AI QE GitHub Actions template](ai-quality-engineering/ci/ai-qe.yml): copy to `.github/workflows/practice-ai-qe.yml` at the repository root to enable it.
- [JMeter GitHub Actions template](performance-testing-jmeter/ci/perf-smoke.yml): copy to `.github/workflows/perf-smoke.yml` at the repository root.
- [Playwright Java Jenkinsfile](playwright-java/project/Jenkinsfile) and [GitLab CI template](playwright-java/project/.gitlab-ci.yml): configure the selected CI system using the included repository-relative paths.

Templates are examples, not active workflows in this collection. They retain the intended dependency versions and technical behavior of the labs.

See [validation notes](VALIDATION.md) for the checks performed on this copy and the distinction between historical lab results and newly executed tests.
