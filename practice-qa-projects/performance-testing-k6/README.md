# Performance Testing with k6

Verification figures below describe the reference lab runs. For checks performed on this copy and remaining environment requirements, see [validation notes](../VALIDATION.md).

A beginner lab for QA engineers. In about an hour, you'll write checks for a shopping journey, compare two workload models, and use queue measurements to explain a slowdown.

## 1. Run the smoke test

Install [Grafana k6](https://grafana.com/docs/k6/latest/set-up/install-k6/) and Python 3.9 or newer. From the repository root, enter this lab and start the local shop in one terminal:

```sh
cd practice-qa-projects/performance-testing-k6
python3 demo/shop.py
```

In another terminal, from the same `practice-qa-projects/performance-testing-k6` directory:

```sh
k6 run tests/shop.js
```

Use `python` in place of `python3` if that's how Python is installed on your machine. The lab targets `127.0.0.1:8080`. A smoke run should complete five journeys and ten HTTP requests. Each iteration searches for one of three products, reads its ID from the JSON response, and uses that ID in the details URL. This is **correlation**. If search fails, the script skips details and records a failed journey.

A **check** records whether a response met an expectation. A **threshold** sets a run-wide requirement and makes k6 return a nonzero exit code if it fails. For example, the script requires every journey to pass, the HTTP error rate to stay below 1%, and search p95 to stay below 350 ms. These are practice targets for the local demo. See [k6 thresholds](https://grafana.com/docs/k6/latest/using-k6/thresholds/).

## 2. Pick a workload model

| Profile | Command | What it models |
|---|---|---|
| Smoke | `k6 run tests/shop.js` | Five iterations to check the script |
| Closed | `k6 run -e PROFILE=load tests/shop.js` | Five virtual users repeatedly shop with a 500 ms pause |
| Open | `k6 run -e PROFILE=arrival tests/shop.js` | 24 journey starts per second when enough virtual users are available |

An iteration is one shopping journey with two HTTP requests when both steps run. At 24 journeys per second, expect about 48 HTTP requests per second if every journey succeeds and the arrival target is met. Actual request rate can differ as work queues up, or when failed searches skip the details request. A journey and an HTTP request are different units.

A closed workload slows its rate when responses get slower. An open workload schedules starts at a given rate, and k6 reports **dropped iterations** if it runs out of free virtual users. For an estimate, multiply the target journeys per second by the average journey time in seconds, then allow headroom for slow responses. The arrival executor already paces starts, so the script adds think time only to the closed workload. See [k6 scenarios](https://grafana.com/docs/k6/latest/using-k6/scenarios/) and [dropped iterations](https://grafana.com/docs/k6/latest/using-k6/scenarios/concepts/dropped-iterations/).

## 3. Find and verify a bottleneck

1. Run and record the closed workload with the default server. Check the search and details thresholds, successful journeys, and search queue time.
2. Run the open workload against the same server. A search occupies one of two slots for about 120 ms. Two slots can serve roughly 16.7 searches per second before overhead. The target of 24 starts per second can therefore build a queue.
3. Stop the demo server and restart it with `python3 demo/shop.py --slots 4`. Rerun **the same open workload**. Four slots have a nominal capacity of roughly 33 searches per second. Compare the two runs before you draw a conclusion.

| Metric | What to look for |
|---|---|
| `http_req_duration{name:search}` | p95 is the duration at or below which around 95% of search samples finish |
| `http_req_duration{name:details}` | Details bypasses the search pool. Use it as a comparison |
| `search_queue_ms` | Time the server measured waiting for a search slot, from the response header. Requests without the header are absent from this metric |
| `dropped_iterations` | Scheduled journeys k6 could not start |
| `journey_success` and `http_req_failed` | Business success and HTTP failures, which can diverge |

Record p95, queue time, dropped iterations, and journey success for both runs. A drop in both queue wait and search p95 after changing only the slot count supports the hypothesis that the search pool was limiting throughput. In a real system, compare client metrics with pool wait, dependency timing, and injector CPU and network measurements. A 12-second local run is enough for this exercise. Use a representative steady-state window and larger samples when evaluating a real target.

One local run with k6 v1.4.0 produced the results below. Timing varies by machine and run.

| Open workload | Search p95 | Queue wait p95 | Dropped iterations | Threshold result |
|---|---:|---:|---:|---|
| Two slots | 1.32 s | 1.19 s | 81 | Fail |
| Four slots | 186 ms | 16 ms | 0 | Pass |

## Practice

- Sketch a workload for 600 shopping journeys per minute. Calculate journey rate and request rate for the two-request flow.
- Change a search term to `not-in-catalog`. Run smoke and confirm the business threshold fails even though the search returned HTTP 200. Restore the term.
- Run smoke from CI after adding a wait for `/health`. Use the k6 exit code as the pass or fail signal.
- Write a short finding with the workload, the observed result, a cause supported by evidence, and the result after changing the pool.
