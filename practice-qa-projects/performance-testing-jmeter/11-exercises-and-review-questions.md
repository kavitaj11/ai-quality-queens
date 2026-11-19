# 11. Exercises and Review Questions

## Exercises

Do them in order; each one builds on the last.

1. **Smoke test.** Run the 10-user smoke test. Confirm 0% errors and that checkouts are about 30% of product views.
2. **Little's Law.** Run 40 users for 2 minutes. Predict the steady throughput with Little's Law before you look, then compare.
3. **Break the timer scope.** Move one timer up to Thread Group level, re-run exercise 2 and explain the throughput change. Put it back.
4. **Break correlation.** Change the token JSON path to `$.access_token`. Run 2 users and find the first error. What does `TOKEN_NOT_FOUND` tell you?
5. **Step test.** Run the agent (Chapter 10). Where is the knee on your machine? Is it the same as in this tutorial? Why might it differ?
6. **Fix and prove.** Restart the app with `--pool-size 16 --backlog 128`, run the agent again and compare both reports.
7. **Open model.** Replace the Thread Group with an **Open Model Thread Group** at a fixed arrival rate (try `rate(2/sec) random_arrivals(5 min)`) and compare how the pool-4 app behaves when it can't slow the users down.
8. **CI gate.** Make `run_test.py` fail on purpose by setting `--p95-ms 50`. Check the exit code.
9. **AI check.** Build the AI prompt from your step test, get an analysis (from an approved assistant or `--call`), and mark every claim as *supported* or *not supported* by the numbers.

## Review questions with short answers

**What is the difference between load, stress and soak testing?**
Load: expected peak, check targets. Stress: beyond peak, find the breaking point and how it fails and recovers. Soak: normal load for hours, find leaks and slow degradation.

**Why report p95 instead of the average?**
Averages hide the slow tail. p95 tells you what the slowest 1 in 20 users experienced.

**How do you decide how many virtual users to run?**
From the workload model: peak throughput × (response time + think time), which is Little's Law. Then check with a real run that the measured throughput matches.

**What is correlation? Give an example.**
Capturing a dynamic value from a response and reusing it, like extracting a login token with a JSON Extractor and sending it as `Authorization: Bearer ${token}`.

**What's the difference between correlation and parameterization?**
Parameterization feeds varied input data (CSV, random values); correlation passes values the server generates from one request to the next.

**Why run JMeter in non-GUI mode?**
The GUI and listeners use CPU and memory and distort results; non-GUI mode with results written to a file is the reliable way to run load.

**What does it mean when throughput stops increasing but response time keeps climbing?**
The system has hit a bottleneck: requests are queuing for a limited resource. That point is the knee, and it marks the practical capacity.

**How would you find the cause of a bottleneck?**
Correlate JMeter results with server-side metrics during the run, and use the USE method (utilization, saturation, errors) per resource: CPU, memory, pools, database, downstream services. Form a hypothesis, change one thing, re-run the same test.

**Your test shows many 401 errors. What do you do?**
Find the first error in each failing chain. Many 401s are often the consequence of one failed login or a broken token extraction, not the real problem.

**How do you include performance testing in CI/CD?**
A short smoke test with p95 and error-rate thresholds on each pull request (the script exits non-zero to fail the build), fuller load tests nightly and before release, with baselines and artifacts kept.

**How can AI help in performance testing, and what are the risks?**
It speeds up workload analysis, script and data creation, and results summaries. The risks are invented causes or numbers and leaking sensitive data, so send aggregates only, use approved tools and verify every claim.

**What guardrails would you put on an AI agent that runs load tests?**
An allowlist of targets, hard limits on users and duration, human approval for each step, deterministic stop conditions, and a full audit log of what it did and why.

---

Back to the [tutorial index](README.md).
