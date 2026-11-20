# Performance testing basics

[11 · Module topics](../../modules/11-non-functional-testing.md) · [Curriculum index](../../README.md)

**Category** · PERFORMANCE TESTING

Load (expected traffic), stress (find the ceiling), spike, and soak. The metrics that matter: p95/p99 latency — never averages — throughput, and error rate under load. Locust, k6, and Gatling make perf tests code you can gate in CI; Playwright adds frontend vitals.

## Worked reference

````text
from locust import HttpUser, task, between

class Shopper(HttpUser):
    wait_time = between(1, 3)

    @task(3)                      # 3x more browsing
    def browse(self):
        self.client.get("/api/products")

    @task(1)
    def order(self):
        self.client.post("/api/orders",
            json={"sku": "A-100", "qty": 1})

# Run headless with a fixed shape:
#   locust -f perf.py --headless \
#          -u 50 -r 5 --run-time 2m
# Read p95/p99 from the stats — never averages
````

## Exercise

Script your busiest endpoint: 50 VUs for 2 minutes, gate on p95 < 500ms and error rate < 1%. Run it twice and compare.
