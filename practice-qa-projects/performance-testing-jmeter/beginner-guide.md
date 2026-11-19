# Performance Testing with JMeter: A Beginner’s Guide

[Back to the hands-on tutorial](README.md)

**Audience:** QA engineers familiar with basic HTTP requests and functional testing  
**Practice time:** Approximately 3–4 hours  
**Goal:** Model a realistic workload, handle dynamic data, analyze results, and investigate bottlenecks.

This standalone guide uses an illustrative shopping application:

**Log in → Search products → View a product → Add it to the cart**

The endpoints, response fields, performance targets, and results below are teaching examples. They are not measurements from the demo app in this folder. For its runnable API, test plan, and exact field names, use [Setup](02-setup.md), [Your first test plan](04-first-test-plan.md), and [Correlation and parameterization](05-correlation-and-parameterization.md). In particular, that demo uses `token`, `items`, and `/api/cart`; this guide’s examples use `accessToken`, `products`, and `/api/cart/items`.

Only run load against an environment you own or have permission to test.

## Contents

1. [Understand the measurements](#1-understand-the-measurements)
2. [Model the workload](#2-model-the-workload)
3. [Set up JMeter](#3-set-up-jmeter)
4. [Build your first test plan](#4-build-your-first-test-plan)
5. [Correlate dynamic values](#5-correlate-dynamic-values)
6. [Add assertions and realistic pauses](#6-add-assertions-and-realistic-pauses)
7. [Run progressively](#7-run-progressively)
8. [Analyze the results](#8-analyze-the-results)
9. [Investigate bottlenecks](#9-investigate-bottlenecks)
10. [Report the findings](#10-report-the-findings)
11. [Practice assignment](#11-practice-assignment)
12. [Official references](#official-references)

## 1. Understand the measurements

Functional testing asks, “Does adding a product to the cart work?” Performance testing asks, “Does it still work quickly and reliably when many people do it?”

| Term | Meaning | Example |
|---|---|---|
| Response time | Time taken to complete a request | Search returns in 600 ms |
| Throughput | Work completed per unit of time | 100 HTTP requests/second |
| Concurrent users | Virtual users active during the test | 50 users browsing |
| Error rate | Percentage of samples that fail | 12 failures out of 1,000 = 1.2% |
| Percentile | Response time below which a percentage of samples fall | p95 = 1.5 seconds |
| Think time | Pause representing a user reading or deciding | 3 seconds before opening a product |
| Ramp-up | Time used to start the configured users | Start 50 users over 100 seconds |
| Bottleneck | Resource or dependency limiting performance | Database connection pool |

**p95 = 1.5 seconds** means approximately 95% of measured requests completed within 1.5 seconds; the remaining 5% took longer. Always specify the request label and measurement window.

JMeter sends protocol requests. It does not render pages or execute browser JavaScript like a browser. HTTP timings therefore do not measure the complete visual user experience.

## 2. Model the workload

A workload model describes **what users do, how often they do it, and how traffic changes over time**. “Run 100 users” is incomplete without their behavior.

### Gather evidence

Ask product and engineering teams for peak-hour request or session volumes, common journeys, pauses between actions, session duration, login frequency, expected growth, and performance targets. Use analytics, access logs, monitoring, and business forecasts. Record assumptions when evidence is unavailable.

### Define the journey mix

Suppose the target is **600 journey starts per minute**:

| Journey | Share of journey starts | Starts/minute | HTTP requests per journey |
|---|---:|---:|---:|
| Search only | 60% | 360 | 1 |
| Search → View product | 30% | 180 | 2 |
| Search → View → Add to cart | 10% | 60 | 3 |

For this calculation, login, static assets, retries, and redirects are excluded.

```text
Search requests:        600/minute
Product requests:       240/minute
Add-to-cart requests:    60/minute
Total:                  900/minute = 15 HTTP requests/second
```

**Journey throughput and HTTP request throughput differ.** One journey can contain several requests.

### Estimate concurrent users

For a stable population repeatedly completing journeys:

```text
Concurrent users ≈ journeys/second × average journey cycle time
```

If 10 journeys start each second and each complete cycle takes 20 seconds, including pauses:

```text
Concurrent users ≈ 10 × 20 = 200
```

Keep units consistent: a journey rate needs a journey cycle time. This is an initial estimate; verify achieved traffic during the test.

### Understand closed and open workloads

| Model | Behavior | Useful for |
|---|---|---|
| Closed | A fixed number of users repeat actions; slower responses reduce request rate | A known population of active users |
| Open | New work arrives at a specified rate independently of previous completion | Orders, events, or incoming traffic |

A standard Thread Group creates a closed workload. When the application slows down, users wait longer and send fewer requests. This can hide the effect of continued incoming demand.

Start with a closed workload for learning. For arrival-rate requirements, select suitable arrival scheduling and verify the achieved rate. A throughput timer can delay existing threads, but cannot guarantee target arrivals when insufficient threads are available.

### Define success before running

| Measurement | Illustrative target |
|---|---|
| Search p95 | ≤ 1,000 ms |
| Product-details p95 | ≤ 800 ms |
| Add-to-cart p95 | ≤ 1,500 ms |
| Failed HTTP samples | < 1% |
| Target workload | Sustained during the measurement window |

These are learning targets, not universal standards. Agree on real targets with stakeholders.

## 3. Set up JMeter

Download JMeter from [Apache](https://jmeter.apache.org/download_jmeter.cgi), and install a compatible Java runtime using the [Getting Started guide](https://jmeter.apache.org/usermanual/get-started.html).

Verify Java:

```bash
java -version
```

Launch JMeter from its `bin` directory:

```bash
# Windows
jmeter.bat

# macOS / Linux
./jmeter
```

Use the GUI to build and debug the plan. Use command-line mode for actual load execution.

## 4. Build your first test plan

Start with **one user and one iteration**. Add these components through JMeter’s **Add** menu:

| Component | Purpose |
|---|---|
| Thread Group | Defines virtual users and execution |
| HTTP Request Defaults | Stores common protocol and host |
| HTTP Cookie Manager | Handles application cookies |
| HTTP Header Manager | Supplies request headers |
| CSV Data Set Config | Reads credentials and inputs |
| HTTP Request samplers | Send requests |
| Extractors | Capture dynamic response values |
| Assertions | Validate responses |
| View Results Tree | Debugs a small test |

For HTTP Request Defaults:

```text
Protocol: https
Server Name: your-test-application.example
```

This host is a placeholder. For JSON request bodies, set `Content-Type: application/json`. Set explicit connection and response timeouts appropriate to the environment—for example, 5,000 ms and 10,000 ms for initial debugging.

Use stable sampler names:

```text
01_Login
02_Search
03_ProductDetails
04_AddToCart
```

Avoid changing IDs in sampler labels; stable labels make aggregation and comparison easier.

### Parameterize inputs

Parameterization supplies different prepared input data.

Example `users.csv` with dummy credentials:

```csv
username,password,searchTerm
qa_user_01,test_password_01,laptop
qa_user_02,test_password_02,headphones
```

CSV Data Set Config:

```text
Filename: users.csv
Variable Names: username,password,searchTerm
Ignore first line: True
Recycle on EOF: False
Stop thread on EOF: True
Sharing mode: All threads
```

Variable names are explicitly entered, so skip the CSV header. Supply enough rows: when CSV loading occurs in a repeating loop, it can consume another row each iteration. It does not automatically bind one account permanently to each thread.

For a session-based plan, put credential loading and login inside a **Once Only Controller**, with repeated shopping actions after it. Decide whether search terms should stay fixed for a session or come from a separately scoped data source.

## 5. Correlate dynamic values

**Correlation captures a value returned by the application and reuses it later.** Examples include access tokens, CSRF tokens, cart IDs, order IDs, and product IDs.

| Parameterization | Correlation |
|---|---|
| Reads prepared input data | Captures runtime response data |
| Username from CSV | Token from login |

### A. Capture an access token

Send `POST /api/login` with this **Body Data**:

```json
{
  "username": "${username}",
  "password": "${password}"
}
```

Illustrative response:

```json
{
  "accessToken": "generated-token-value",
  "userId": 1042
}
```

Add a **JSON Extractor** directly beneath the login sampler:

```text
Names of created variables: accessToken
JSON Path expressions: $.accessToken
Match No.: 1
Default Values: TOKEN_NOT_FOUND
```

For subsequent authenticated requests, set:

```text
Authorization: Bearer ${accessToken}
```

Scope this header to the authenticated requests after login.

Add a **JSR223 Assertion**, select **Groovy**, and enter:

```groovy
def token = vars.get('accessToken')

if (!token || token == 'TOKEN_NOT_FOUND') {
    AssertionResult.setFailure(true)
    AssertionResult.setFailureMessage(
        'Login did not return an access token'
    )
}
```

During debugging, set the Thread Group’s sampler error action to **Stop Thread**. A failed login should not generate misleading downstream requests. For longer tests, define explicit token refresh or session recovery behavior; do not silently ignore failures.

### B. Capture a product ID

Search:

```http
GET /api/products?query=${searchTerm}
```

Illustrative response:

```json
{
  "products": [
    {"id": "P1001", "name": "Laptop"},
    {"id": "P1002", "name": "Laptop Stand"}
  ]
}
```

Add a JSON Extractor:

```text
Names of created variables: productId
JSON Path expressions: $.products[0].id
Match No.: 1
Default Values: PRODUCT_NOT_FOUND
```

Validate extraction as you did for the token. Then request:

```http
GET /api/products/${productId}
```

For `POST /api/cart/items`, send:

```json
{
  "productId": "${productId}",
  "quantity": 1
}
```

Selecting the first result is sufficient for learning. Vary products in realistic tests to avoid an unrealistically cache-friendly workload.

### C. Cookies and CSRF

Use **HTTP Cookie Manager** for normal cookies. For HTML such as:

```html
<input name="_csrf" value="generated-csrf-token">
```

A **CSS Selector Extractor** can use:

```text
Reference Name: csrfToken
CSS Selector: input[name="_csrf"]
Attribute: value
Default Value: CSRF_NOT_FOUND
```

Send `${csrfToken}` in the form field or header expected by your application.

Inspect the actual outgoing request using View Results Tree. The extractor must run after the response containing the value and before the request consuming it.

## 6. Add assertions and realistic pauses

HTTP 200 does not guarantee business success. Validate the expected status code and business outcome.

| Request | Useful validation |
|---|---|
| Login | Token exists |
| Search | Expected `products` field exists |
| Product details | Returned ID matches the selected product |
| Add to cart | Requested item and quantity are present |

Prefer structured JSON checks to searching for a generic word like `success`.

### Scope think time carefully

For a learning example, add a **Uniform Random Timer** directly beneath the sampler that should be preceded by a pause:

```text
Constant Delay Offset: 1000 ms
Random Delay Maximum: 2000 ms
```

This produces a pause of approximately 1–3 seconds **before that sampler**. A timer affects every sampler in its scope. At Thread Group level, it may pause before login and all other requests, changing the intended workload.

Separate Thread Groups can make different journeys easier to understand. However, allocating threads 60/30/10 does not necessarily produce that journey-completion mix because the journeys have different durations. Measure and adjust.

The **Throughput Controller** controls execution selection; it does not directly configure requests per second.

## 7. Run progressively

| Test | Example | Purpose |
|---|---|---|
| Script validation | 1 user, 1 iteration | Check inputs, assertions, correlation |
| Small baseline | 5 users, 5 minutes | Establish low-load behavior |
| Load | Agreed workload, 20–30 minutes | Check expected demand |
| Stress | Increase load in steps | Find capacity limits |
| Spike | Sudden increase | Check recovery |
| Soak | Sustained load for hours | Find gradual degradation |

For a manually built plan named `shopping.jmx`, use:

```text
Number of Threads: ${__P(users,5)}
Ramp-up Period: ${__P(ramp,30)}
Loop Count: Forever
Specify Thread lifetime: Enabled
Duration: ${__P(duration,300)}
```

Ramp-up is part of the configured lifetime. Leave enough time for a useful steady-state window.

Disable View Results Tree and other heavy debugging listeners. From the directory containing `shopping.jmx` and `users.csv`:

```bash
jmeter -n -t shopping.jmx \
  -Jusers=50 \
  -Jramp=120 \
  -Jduration=1320 \
  -l results-50.jtl \
  -e -o report-50
```

On Windows, use `jmeter.bat` and enter the command on one line. This allows approximately two minutes of ramp-up and twenty minutes afterward. Use fresh result filenames and an empty or nonexistent report directory each run.

This command describes the plan built in this guide. To run the repository’s supplied plan, follow its [running and analysis chapter](06-running-and-analyzing-results.md), which uses its own filenames and options.

## 8. Analyze the results

Open the generated report’s `index.html`.

### First validate the run

Confirm that:

1. Intended journeys executed successfully.
2. Tokens and data remained valid.
3. Achieved throughput and journey mix matched the workload model.
4. The injector had CPU, memory, and network headroom.
5. Environment and dataset were representative.
6. Warm-up and steady-state periods were identified.

A test configured for 50 users can still generate the wrong workload.

### Then inspect performance

| Measurement | Meaning | Common mistake |
|---|---|---|
| p50 | Typical response experience | Assuming it describes slow users |
| p95/p99 | Tail response behavior | Looking only at averages |
| Throughput | Work actually completed | Treating failures as useful work |
| Error rate | Reliability under load | Ignoring assertion failures |
| Active threads | Running virtual users | Equating threads with requests/second |
| Response time over time | Whether performance deteriorates | Mixing warm-up with steady state |

Review request labels separately. Search and add-to-cart have different targets. Do not add HTTP sample counts and Transaction Controller sample counts together as request volume: they represent different levels of work. Check the relevant dashboard graph’s inclusion rules.

### Worked analysis: hypothetical results

| Users | HTTP requests/sec | Search p95 | Error rate |
|---:|---:|---:|---:|
| 10 | 8 | 400 ms | 0% |
| 25 | 18 | 700 ms | 0.1% |
| 50 | 24 | 1,800 ms | 2% |
| 100 | 25 | 5,000 ms | 12% |

Throughput grows initially, then barely increases while latency and failures rise sharply. This suggests a capacity limit around the higher load levels. It does **not** identify the cause; it identifies a load range and time window to investigate.

Fast failures can reduce the overall average. Inspect errors and successful-response timings together, and record whether reported percentiles include failed samples. Small samples also make p99 unstable.

## 9. Investigate bottlenecks

JMeter shows client-observed symptoms. Application metrics, database metrics, dependency traces, and logs help explain them. Synchronize timestamps and record timezone.

| Symptom | Possible cause | Evidence |
|---|---|---|
| All requests slow; app CPU high | CPU-intensive processing | Profiles, hot methods |
| Slow responses; CPU moderate | Dependency, lock, or pool waits | Traces, wait metrics, queues |
| Search alone becomes slow | Query or index problem | Query plans, slow-query logs |
| Periodic spikes | Garbage collection or scheduled jobs | GC pauses, job timestamps |
| Errors beyond a concurrency threshold | Connection or worker pool exhaustion | Pool usage, queues, timeouts |
| High connect time | Connection establishment or network issue | Connection logs, network metrics |
| Degradation over hours | Resource leak or backlog | Heap, connections, queue depth |
| App healthy; injector CPU high | Load generator limit | JMeter host metrics |

These are hypotheses, not diagnoses. High memory use alone does not prove a leak, and moderate CPU does not prove spare capacity.

### Example: Search slows under load

Observation: Search p95 reaches 2.8 seconds while product details remain fast.

1. Match the slowdown timestamp to application traces.
2. Identify time spent in search’s database calls.
3. Inspect waits, query duration, and query plans.
4. Check whether queries scan more data than expected.
5. Have engineering implement the evidence-supported fix.
6. Repeat the same workload with the same dataset and environment.
7. Compare throughput, p95, errors, and database behavior.

“Database CPU was high” is an observation. A confirmed cause needs stronger evidence, such as an expensive query plan and repeatable improvement after a targeted change. Change one major factor at a time.

## 10. Report the findings

```text
Objective:
Validate shopping journeys at the agreed peak workload.

Environment:
Application version, infrastructure, database size,
dependency configuration, and JMeter host.

Workload:
Journey mix, users or arrival rate, think time,
ramp-up, duration, and test data.

Measurement window:
Exact steady-state start and end timestamps, with timezone.

Expected:
Response-time, reliability, and workload targets.

Actual:
Achieved workload, per-request or per-journey percentiles,
successful throughput, and failures.

Findings:
Observed symptoms and supporting monitoring evidence.

Conclusion:
Which targets passed or failed within the tested conditions.

Next action:
Specific investigation or change, owner, and retest plan.
```

Avoid reporting only “supports 100 users.” Explain what they did, at what rate, with what latency and failures.

## 11. Practice assignment

1. Build the four-request flow and prove correlation with one user.
2. Break the token extractor deliberately and verify a clear failure.
3. Restore it and run 5, 10, and 25 users.
4. Record achieved throughput, p95, and error rate.
5. Explain how think time changes request rate.
6. Investigate one slow sampler using server evidence.
7. Write a one-page report separating observations from suspected causes.

You have completed the exercise when you can justify the workload, demonstrate valid requests, interpret the results, and support a bottleneck hypothesis with evidence.

Continue with the [full hands-on tutorial](README.md) for the local demo, supplied JMeter plan, CI gate, and exercises.

## Official references

- [Apache JMeter: Getting Started](https://jmeter.apache.org/usermanual/get-started.html)
- [Apache JMeter: Elements of a Test Plan](https://jmeter.apache.org/usermanual/test_plan.html)
- [Apache JMeter: Component Reference](https://jmeter.apache.org/usermanual/component_reference.html)
- [Apache JMeter: Best Practices](https://jmeter.apache.org/usermanual/best-practices.html)
- [Apache JMeter: Generating Dashboard Reports](https://jmeter.apache.org/usermanual/generating-dashboard.html)
