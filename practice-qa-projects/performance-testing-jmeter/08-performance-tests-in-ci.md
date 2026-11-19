# 8. Performance Tests in CI/CD

Performance problems are cheapest to fix in the pull request that caused them. Full load tests are too slow for every commit, so use **layers**:

| When | Test | Length | Gate |
|---|---|---|---|
| Every pull request | Performance **smoke**: 10–20 users | 1–2 min | p95 and error rate thresholds |
| Nightly | **Load** test at expected peak | 15–30 min | SLOs + compare with last night |
| Before a release | Load + **stress** steps + short **soak** | 1–4 h | SLOs, capacity report |
| Production | Synthetic checks + real-user monitoring | continuous | Alerts on SLO burn |

## The gate: an exit code

`analyze_jtl.py` (and therefore `run_test.py`) exits with code **1** when an SLO fails. Every CI system treats a non-zero exit code as a failed step:

```bash
python scripts/run_test.py --users 20 --rampup 10 --duration 60 --name ci-smoke --p95-ms 800 --max-error-pct 1
echo $?      # 0 = SLO passed, 1 = SLO failed
```

Thresholds for a smoke test should be **loose enough not to flap** (fail randomly) and **tight enough to catch real regressions**. A practical start: set p95 at about 2× what a healthy smoke run shows, and tighten as you collect history.

## Example GitHub Actions workflow

[`ci/perf-smoke.yml`](ci/perf-smoke.yml) shows the full pattern:

1. Set up Java and Python.
2. Download JMeter and **verify its SHA-512 checksum**.
3. Start the app under test and wait for `/health`.
4. Run the smoke test with SLO thresholds, which fails the job if breached.
5. Upload the `.jtl`, summary and HTML dashboard as a build artifact, even when the test fails.

> This workflow is provided as an example and has not been run in GitHub Actions as part of this tutorial; the same commands were run locally.

## Making CI performance results trustworthy

- **Shared CI runners are noisy.** Their speed varies, so use them for smoke tests and big regressions only. Run real load tests on dedicated, consistent machines.
- **Compare with a baseline**, not only a fixed threshold: "checkout p95 is 40% slower than on `main`" is more useful than "under 800 ms".
- **Keep the artifacts.** A failed gate without the dashboard is a guessing game.
- **Same data, same environment, same plan** every run, or the comparison means nothing.

Next: [9. AI-augmented performance testing](09-ai-augmented-performance-testing.md)
