# 3. Workload Modeling

A load test is only as good as its workload model. "Run 500 users" means nothing until you know what those users do, how fast, and why 500.

## The questions to answer first

| Question | Where to get the answer |
|---|---|
| What are the key user journeys? | Product owner, analytics, access logs |
| How often does each journey happen at peak (the **mix**)? | Analytics, logs, business forecasts |
| What is the **peak rate**: journeys or requests per second in the busiest hour? | Logs, monitoring, last year's peak × growth |
| How long do users pause between steps (**think time**)? | Analytics session data, or a sensible range |
| How fast do users arrive (**ramp-up**), and is there a spike? | Business events: sales, month-end, 9 a.m. logins |
| How much data exists (products, accounts, history)? | Production volumes, anonymized |
| What are the **SLOs**? | Product owner and engineering, agreed in writing |

## Worked example: the demo shop

**Business input:** "At our busiest hour we expect about **430,000 page views** and **36,000 orders**."

**Step 1. Convert to per-second rates.**

```
430,000 views / 3,600 s  ≈ 120 requests per second (all journeys together)
 36,000 orders / 3,600 s =  10 checkouts per second
```

**Step 2. Define the journey and mix.** One shopper iteration in our plan:

| Step | Happens in | Requests per iteration |
|---|---|---|
| Login | once per user | (once) |
| Search products | 100% of iterations | 1.0 |
| View product | 100% of iterations | 1.0 |
| Add to cart + checkout | 30% of iterations | 0.6 (0.3 + 0.3) |
| **Total** | | **2.6** |

The 30% buy rate is modeled with a **Throughput Controller** (Chapter 4). Check the mix against the business: 10 checkouts/s out of 120 requests/s is 8% of requests; our plan gives 0.3 / 2.6 = 11.5%. That is close enough for a first model; a stricter test would tune the percentage.

**Step 3. Choose think time.** Users pause 0.5–1.5 s before each search, product view and add-to-cart (uniform random, average 1.0 s). That is 2.3 pauses per 2.6 requests, so about **0.885 s per request**. These are fast shoppers; real e-commerce think times are often 3–10 s, which means you need more users for the same throughput.

**Step 4. Use Little's Law to size the users.**

```
N = X × (R + Z)
  = 120 req/s × (0.033 s + 0.885 s)
  ≈ 110 users
```

So a load test at **~110 users** reproduces the busy hour. A useful load-test plan is then:

| Test | Users | Why |
|---|---|---|
| Smoke | 10 | Does the script work? |
| Load | 110 | Expected peak |
| Stress steps | 25 → 50 → 100 → 200 | Where is the limit? (Chapter 7) |
| Soak | 110 for 4+ hours | Leaks and slow degradation |

**Step 5. Check the model with a real run.** After the run, compute `N / (R + Z)` with the measured response time and compare it with the measured throughput. If they disagree a lot, something is off: think time not applied, errors returning instantly, or the load generator overloaded.

## Closed vs open workload models

- **Closed model (what JMeter's standard Thread Group does):** a fixed number of users; each starts a new request only after the previous one finishes. If the server slows down, the users slow down too, so the arrival rate drops. This is realistic for logged-in users working through a session.
- **Open model:** new requests arrive at a fixed rate whatever the server does (like customers walking into a shop). If the server slows down, work piles up. This is realistic for public APIs and spikes. In JMeter, use the **Open Model Thread Group** or a **Constant Throughput Timer** to drive a target rate.

A closed model can hide problems: a slow server "protects itself" by slowing the users down. When the question is "can we handle 120 requests per second?", also run an open-model test.

## Ramp-up and duration

- **Ramp-up** spreads user start times so you don't hammer the server with a burst nobody expects. A rule of thumb: ramp over at least as long as one iteration takes, and longer for big tests. (Chapter 7 shows what a burst of new connections did to the demo app.)
- **Duration**: hold the steady state long enough to collect thousands of samples per transaction. Exclude the ramp-up from your conclusions; the timeline in `analyze_jtl.py` helps with that.

## Test data

- One user account per virtual user where possible (we recycle 20 accounts across all users, which is fine for a demo).
- Realistic search terms and IDs, not the same value every time; otherwise caches make everything look fast.
- Production-like data volume in the database; an empty database is always fast.

Next: [4. Your first test plan](04-first-test-plan.md)
