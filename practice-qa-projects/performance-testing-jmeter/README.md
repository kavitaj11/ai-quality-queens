# Performance Testing with JMeter: A Beginner's Hands-On Tutorial

Verification figures below describe the reference lab runs. For checks performed on this copy and remaining environment requirements, see [validation notes](../VALIDATION.md).

**Level:** beginner QA engineer · **Time:** about 4–6 hours · **Last reviewed:** November 2026 (JMeter 5.6.3)

You will load-test a small shop API that runs on your own machine, find its breaking point, prove the cause, fix it and prove the fix. The tutorial develops four core performance-engineering skills: **workload modeling, correlation, results analysis and finding bottlenecks**. The last two chapters add **AI-augmented analysis** and a small **agentic QA** loop that runs the step tests for you.

Every number in these chapters comes from real runs of the files in this folder.

## Beginner overview

New to performance testing? Start with the [standalone beginner guide](beginner-guide.md) for workload modeling, correlation, results analysis, and bottleneck investigation. Its illustrative API examples and hypothetical results are separate from the runnable demo documented in the chapters below.

## What you will build

```
                ┌─────────────────────────────────────────────────────────┐
  JMeter        │  Shop API (demo-app/shop_api.py)                        │
  test plan ───►│  login → search → view product → add to cart → checkout │
  (N users)     │  every query borrows 1 of 4 "database connections"      │
                └─────────────────────────────────────────────────────────┘
        │
        ▼
  results.jtl ──► analyze_jtl.py ──► table, timeline, SLO pass/fail (CI gate)
        │                │
        ▼                ▼
  HTML dashboard    ai_summary.py (AI-augmented)   perf_agent.py (agentic step test)
```

## Chapters

| # | Chapter | You will be able to |
|---|---|---|
| 1 | [Performance testing basics](01-performance-testing-basics.md) | Explain test types, percentiles, throughput, SLOs and Little's Law |
| 2 | [Setup: JMeter and the demo app](02-setup.md) | Install and verify JMeter, start the app, open the plan |
| 3 | [Workload modeling](03-workload-modeling.md) | Turn business numbers into users, ramp-up, think time and mix |
| 4 | [Your first test plan](04-first-test-plan.md) | Read and build a plan: samplers, config, timers, assertions, scope |
| 5 | [Correlation and parameterization](05-correlation-and-parameterization.md) | Pass tokens and IDs between requests, and debug broken chains |
| 6 | [Running tests and analyzing results](06-running-and-analyzing-results.md) | Run in non-GUI mode, read percentiles and the HTML dashboard |
| 7 | [Finding bottlenecks](07-finding-bottlenecks.md) | Find the knee, trace the first error, fix and prove the fix |
| 8 | [Performance tests in CI/CD](08-performance-tests-in-ci.md) | Gate a pipeline on p95 and error rate |
| 9 | [AI-augmented performance testing](09-ai-augmented-performance-testing.md) | Use AI to speed up analysis safely, and check its claims |
| 10 | [Agentic QA for performance](10-agentic-qa-for-performance.md) | Run a guarded plan → act → observe → decide loop |
| 11 | [Exercises and review questions](11-exercises-and-review-questions.md) | Practise and prepare |

## Quick start (if you already have Java and JMeter)

```bash
# 1. Start the app under test (leave this terminal open)
python demo-app/shop_api.py

# 2. In a second terminal: point to JMeter and run a 30-second smoke test
export JMETER_HOME=/path/to/apache-jmeter-5.6.3        # Windows PowerShell: $env:JMETER_HOME="C:\path\to\apache-jmeter-5.6.3"
python scripts/run_test.py --users 10 --rampup 10 --duration 30 --name smoke --p95-ms 800 --max-error-pct 1
```

## Folder layout

```
performance-testing-jmeter/
├── demo-app/shop_api.py        app under test (standard library only, port 8099)
├── jmeter/
│   ├── shop_load_test.jmx      the JMeter test plan
│   └── data/users.csv          test users and search terms
├── scripts/
│   ├── run_test.py             run JMeter non-GUI + dashboard + analysis
│   ├── analyze_jtl.py          percentiles, timeline, SLO check (CI gate)
│   ├── ai_summary.py           AI-augmented analysis (prompt builder, optional Claude call)
│   └── perf_agent.py           agentic step-load test with guardrails
├── ci/perf-smoke.yml           example GitHub Actions workflow
├── requirements-ai.txt         only for the optional AI steps
└── results/                    created by your runs (git-ignored)
```

## Safety rule

Only load-test systems you own or have written permission to test. A load test against someone else's site can look exactly like an attack. Everything here targets `127.0.0.1`.
