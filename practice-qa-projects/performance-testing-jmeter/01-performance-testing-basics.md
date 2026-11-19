# 1. Performance Testing Basics

Functional testing asks "does it work?". Performance testing asks "does it still work, fast enough, when many people use it at once, and for how long?"

## Types of performance test

| Test | Question it answers | Typical shape |
|---|---|---|
| **Load** | Do we meet our targets at expected peak traffic? | Ramp to peak, hold 30–60 min |
| **Stress** | Where does it break, and how does it fail and recover? | Keep increasing past peak |
| **Spike** | What happens when traffic jumps suddenly? | Jump from low to very high in seconds |
| **Soak (endurance)** | Does it degrade over hours (memory leaks, full disks)? | Normal load for 4–24 hours |
| **Scalability** | Does adding resources add capacity? | Same steps, different server sizes |
| **Capacity (step)** | What is the most load we can handle within targets? | Steps of increasing users (Chapter 7) |

## The metrics that matter

**Response time** is how long one request takes. Report **percentiles**, not just the average:

- **p50 (median):** half of requests were faster than this
- **p90 / p95:** 90% / 95% of requests were faster than this, so 1 in 10 or 1 in 20 users waited longer
- **p99:** the slow tail; often where real problems first show

Why not the average? Nine requests at 100 ms and one at 5,000 ms average 590 ms. That number describes nobody: most users saw 100 ms and one user waited 5 seconds. The p95 (5,000 ms here) tells you about the unhappy user.

**Throughput** is how much work gets done per second (requests/s or transactions/s). As you add users, throughput should rise. When it stops rising, you have hit a limit.

**Error rate** is the share of requests that failed (HTTP errors, timeouts, failed assertions). A fast error is still an error.

**Concurrency** is how many users (JMeter threads) are active at once.

**Think time** is the pause a real user takes between actions (reading a page, typing). Without it, 10 virtual users behave like hundreds of real ones.

## Service level objectives (SLOs)

An SLO is a measurable target, agreed before testing. In this tutorial:

> **p95 of every transaction ≤ 800 ms, and overall error rate ≤ 1%.**

Without an SLO, a test result is just numbers; with one, it is a pass or a fail.

## Little's Law: the one formula to remember

For a system in a steady state:

```
users in the system  =  throughput  ×  time each user spends per request
        N            =      X       ×        (R + Z)
```

- `X` = throughput (requests/second)
- `R` = response time (seconds)
- `Z` = think time (seconds)

Example from the demo app (Chapter 6): each iteration makes 2.6 requests on average and pauses 2.3 times for 1.0 s on average, so think time per request is `Z = 2.3 / 2.6 ≈ 0.885 s`. With 10 users and an average response time of 0.033 s:

```
X = N / (R + Z) = 10 / (0.033 + 0.885) ≈ 10.9 req/s
```

JMeter measured **11.0 and 10.8 req/s** once all 10 users were running. (The whole-run average was lower, 9.5 req/s, because it includes the ramp-up.) Little's Law is how you check that a test generates the load you intended, and how you work out how many users you need (Chapter 3).

## Where performance problems come from

Almost every bottleneck is one of these, shown here as a checklist you'll reuse in Chapter 7:

- **A limited pool:** database connections, threads, worker processes, sockets
- **A slow dependency:** a database query, another service, a third-party API
- **CPU or memory** saturation on the server (or on the load generator itself)
- **Locks and contention:** many requests waiting for the same row, file or lock
- **Network:** bandwidth, connection limits, TLS handshakes

Next: [2. Setup](02-setup.md)
