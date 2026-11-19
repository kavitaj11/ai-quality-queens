# Validation notes

Checked on November 18, 2026 with Python 3.12.14 on Linux.

## Checks run on this copy

| Check | Result |
|---|---|
| AI QE: `python -m unittest discover -s tests -v` from `ai-quality-engineering/project/` | 24 tests passed, including the golden evaluation pipeline, ten deliberate regressions, backend state, dataset isolation, telemetry, and mocked judge failure handling |
| Cloud: `python -m unittest discover -s tests -v` from `cloud-testing-aws-gcp/` | 4 tests passed using offline storage adapters; no cloud credentials or SDKs required |
| File completeness | All 191 source tutorial files retained, with review exercise filenames updated; eight labs and the shared practice app included |
| Python syntax | 31 files parsed successfully |
| Structured data and configuration | 6 JSON files, 2 JSONL files, 9 XML/JMX files, and 3 YAML files parsed successfully |
| JavaScript syntax | The k6 script passed Node.js module syntax checking |
| Documentation links | Relative Markdown destinations checked after moving the collection |

The collection also includes an AI QE CI template and these validation notes. The collection index and repository README provide entry points for the copied projects. Company-specific role descriptions, locations, personal background prompts, and recruiting references were removed; technical review questions and exercises remain.

## Scope of verification

The unit tests above ran on this copy. Other result counts, timings, screenshots, and observed failures in the tutorials describe earlier reference lab runs and worked examples. They are useful technical context, but they are not new execution results for this repository.

Maven, pytest, JMeter, k6, and an Android emulator were not available in this validation environment. The Java, browser, mobile, and load-test suites therefore were not rerun. Their dependency declarations, test fixtures, sample applications, and setup instructions are included so they can be run in a suitable environment. Parsing a JMX file or checking JavaScript syntax does not validate load-test behavior.

No paid model calls or live AWS/GCP checks were made. Live judge behavior was exercised through mocked HTTP responses in the AI QE unit tests; this does not establish real-model grading accuracy. The cloud tests use offline doubles and do not establish access to a real bucket.

CI files under the project folders are optional templates. Copy and configure a template as described in the [collection README](README.md) to activate it. These checks do not imply a GitHub Actions run or a configured branch-protection gate.
