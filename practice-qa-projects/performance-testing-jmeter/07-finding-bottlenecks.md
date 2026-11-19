# 7. Finding Bottlenecks

A bottleneck is the one resource that limits the whole system. The job is to **find the knee, find the cause, prove the cause, fix it, and prove the fix.** This chapter does all five with real results from the demo app.

## Step 1: a step test to find the knee

Increase the load in steps and hold each one long enough to measure. Here, each step ran for 60 seconds against the default demo app (pool of 4 database connections, listen backlog of 5). These are the real results, collected by the agent in Chapter 10:

| Users | Throughput (req/s) | p95 overall (ms) | Errors % | SLO | Requests waiting for a DB connection (max) | Avg wait for a connection (ms) |
|---|---|---|---|---|---|---|
| 25 | 23.2 | 83 | 0.0 | pass | 1 | 0.06 |
| 50 | 46.1 | 84 | 0.0 | pass | 5 | 0.95 |
| 100 | 89.1 | 110 | 0.0 | pass | 20 | 15.1 |
| 200 | 118.6 | 790 | 0.98 | **FAIL** (checkout p95 836 ms) | 101 | 489.8 |

Read it like a performance engineer:

- **25 → 50 → 100 users:** throughput roughly doubles each time (23 → 46 → 89). The system scales.
- **100 → 200 users:** users doubled but throughput rose only **1.33×** (89 → 119), while p95 jumped from 110 ms to 790 ms. **That is the knee.** Past it, extra users just wait in a queue.
- **Capacity:** the highest step that met the SLO was 100 users. The real limit is around **120 req/s**. (Chapter 3's busy hour needs 120 req/s, so this app cannot handle its own peak: a valuable finding.)

```
throughput (req/s)                       p95 (ms)
 120 |                 ●  ← flat          800 |                 ●  ← climbs
  90 |          ●                         400 |
  45 |     ●                              110 |          ●
  23 | ●                                   83 | ●   ●
     +--------------------- users             +--------------------- users
       25  50  100    200                       25  50  100    200
```

## Step 2: form a hypothesis from server-side data

JMeter tells you **that** it's slow. Server metrics tell you **why**. The demo app's `/metrics` endpoint is our server-side view:

- At 25 users, a request waited **0.06 ms** on average for a database connection.
- At 200 users, it waited **490 ms**, and up to **101 requests** were queued for one of 4 connections.

The response time of a request is now mostly *waiting for a connection*, not doing work. **Hypothesis: the database connection pool (size 4) is the bottleneck.**

This is the **USE method** in action. For each resource, check:

- **Utilization:** how busy is it? (4 of 4 connections in use all the time)
- **Saturation:** is work queuing for it? (101 waiting)
- **Errors:** is it failing? (see step 3)

In a real system the same questions apply to CPU, memory, thread pools, connection pools, disk, network and downstream services. The data comes from APM tools, database monitoring or cloud metrics such as CloudWatch.

## Step 3: explain the errors, starting with the first one

The 200-user step also had a 0.98% error rate. Chapter 5 traced it: the **first** error was `Connection refused` at login, and the 401/404/400 errors that followed were consequences. Connection refused under a burst of new users means the server's **listen backlog** filled up: the queue of connections waiting to be accepted. Python's default backlog is 5. That's a **second bottleneck**, separate from the pool, and it only shows up when many users arrive at once (the 200 users ramped up in 20 seconds).

## Step 4: fix and prove the fix

Change one thing at a time where you can. Here both causes are clear, so the demo app was restarted with a bigger pool and backlog, and the **same 200-user test** was run again:

```bash
python demo-app/shop_api.py --pool-size 16 --backlog 128
python scripts/run_test.py --users 200 --rampup 20 --duration 60 --name fixed-200 --p95-ms 800 --max-error-pct 1
```

| 200 users | Before (pool 4, backlog 5) | After (pool 16, backlog 128) |
|---|---|---|
| Steady throughput | ~119 req/s | **~216 req/s** |
| p95 overall | 790 ms | **84 ms** |
| Errors | 0.98% | **0.0%** |
| Max requests waiting for a connection | 101 | 2 |
| SLO | FAIL | **PASS** |

Steady-state throughput now matches Little's Law (200 / (0.033 + 0.885) ≈ 218 req/s): the app is doing what the users ask, not queuing them.

**A fix is only proven when the same test, at the same load, passes.** And a fix moves the bottleneck rather than removing it: at higher load something else will saturate next (CPU, the database itself, the network). Keep stepping.

## Bottleneck checklist

| Symptom in JMeter | Likely causes | Check on the server side |
|---|---|---|
| Throughput flat, response time climbing | Pool or thread limit, CPU saturation, lock contention | Pool wait time, CPU %, thread dumps, DB locks |
| One transaction much slower than others | Slow query, missing index, slow downstream call | DB slow-query log, APM traces |
| Response time drifts up during a steady hold | Memory leak, growing tables, cache eviction | Memory and GC over time, table sizes |
| Connection refused / timeouts at ramp-up | Listen backlog, connection limits, autoscaling too slow | Server accept queue, load balancer limits |
| Errors fast, throughput high | Requests failing early (auth, validation, broken correlation) | Fix the script or the first error first |
| Results change when JMeter machine is busy | Load generator overloaded | JMeter host CPU and memory; distribute the load |

Next: [8. Performance tests in CI/CD](08-performance-tests-in-ci.md)
