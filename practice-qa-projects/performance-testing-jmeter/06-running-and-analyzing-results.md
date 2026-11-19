# 6. Running Tests and Analyzing Results

## Run in non-GUI mode

```bash
jmeter -n -t jmeter/shop_load_test.jmx \
       -l results/smoke/results.jtl \
       -e -o results/smoke/dashboard \
       -Jusers=10 -Jrampup=10 -Jduration=30
```

| Flag | Meaning |
|---|---|
| `-n` | non-GUI mode |
| `-t` | the test plan |
| `-l` | where to write every sample (the `.jtl` results file) |
| `-e -o <folder>` | generate the HTML dashboard into an **empty** folder |
| `-J<name>=<value>` | set a property read by `${__P(name)}` |

`scripts/run_test.py` wraps exactly this. It also checks the app's `/health` first, never overwrites an earlier run, and runs the analysis:

```bash
python scripts/run_test.py --users 10 --rampup 10 --duration 30 --name smoke --p95-ms 800 --max-error-pct 1
```

## Reading the analysis

This is a real 30-second smoke run against the demo app (10 users):

```
Transaction               Count   Err%     Avg     p50     p90     p95     p99     Max   Req/s
----------------------------------------------------------------------------------------------
01 Login                     10    0.0      22      17      20      62      62      62    0.34
02 Search products          109    0.0      43      43      50      51      51      51    3.67
03 View product             104    0.0      13      13      14      15      15      16     3.5
04 Add to cart               30    0.0      24      22      27      27      39      39    1.01
05 Checkout                  30    0.0      84      84      97      99     100     100    1.01
TOTAL                       283    0.0      33      27      68      84      97     100    9.53

Timeline
  t(s)  users   req/s   p95 ms   err%
     0     10     6.5       89    0.0
    10     10    11.0       80    0.0
    20     10    10.8       87    0.0

SLO: PASSED
```

How to read it:

1. **Errors first.** 0.0% everywhere: every assertion passed and every correlation worked.
2. **Check the mix.** 30 checkouts out of 104 product views is 29%, matching the 30% Throughput Controller.
3. **Per-transaction percentiles.** Checkout is the slowest (p95 99 ms), as expected: it does the most "database" work. Login's p95 (62 ms) comes from only 10 samples, so a single slow login moves it. **Percentiles from small counts are unreliable**: get hundreds of samples per transaction before you trust a p95.
4. **The timeline.** The first window includes the ramp-up (6.5 req/s). The steady state is about 11 req/s, which matches Little's Law (Chapter 1).
5. **The SLO.** Every transaction's p95 is far below 800 ms. This is a smoke test, so passing only means the script works and the app is healthy at low load.

## The HTML dashboard

Open `results/<run>/dashboard/index.html`. The four charts to learn first:

| Chart | Look for |
|---|---|
| **Statistics table** (Dashboard tab) | The same numbers as the analysis, per transaction |
| **Active Threads Over Time** | Did the ramp-up and hold happen as planned? |
| **Response Times Over Time** | Does latency climb as users increase, or drift upward during a steady hold (a leak)? |
| **Transactions Per Second** | Does throughput rise with users, then flatten? Flattening = a limit |
| **Response Time Percentiles** | How long is the slow tail? |

Read charts side by side. Rising threads with flat throughput and rising response times is the signature of a bottleneck, and Chapter 7 is all about it.

## Common analysis mistakes

- **Reporting averages only.** Always report p95 (or p90) and the error rate.
- **Including the ramp-up.** Judge the steady state; use the timeline to find it.
- **Trusting fast errors.** A test with 20% errors often has a *lower* average response time, because errors return instantly. Read the error rate before the response times.
- **Ignoring the load generator.** If the JMeter machine's CPU is above ~80%, the results describe JMeter, not the app. Check it, and use distributed testing for very large loads.
- **One run.** Results vary. Run important tests at least twice and compare.

Next: [7. Finding bottlenecks](07-finding-bottlenecks.md)
