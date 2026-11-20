# Load testing tools: JMeter, k6, Gatling & friends

[11 · Module topics](../../modules/11-non-functional-testing.md) · [Curriculum index](../../README.md)

**Category** · PERFORMANCE TESTING

The load-tool landscape: JMeter (the GUI-configured veteran — hugely capable, protocol-rich, cluster-able), k6 (JS scripts, CLI-first, thresholds as code — the modern CI default), Gatling (Scala/Java DSL, excellent reports), Locust (Python, code-first swarms), and LoadRunner (enterprise incumbent). Choose by: who writes the tests, what protocol, where it runs (CI vs perf rig), and whether thresholds can fail a build.

## Worked reference

````text
# Locust — load tests as plain Python
from locust import HttpUser, task, between

class Shopper(HttpUser):
    wait_time = between(1, 3)

    @task(3)
    def browse(self):
        self.client.get("/products")

    @task(1)
    def add_to_cart(self):
        self.client.post("/cart",
                         json={"sku": "A-100", "qty": 1})

# headless CI run with a gate:
# locust --headless -u 50 -r 10 -t 3m \
#   --host $BASE_URL \
#   --check-avg-response-time 300 --check-fail-ratio 0.01
# (nonzero exit on breach — same idea as k6 thresholds)
````

## Exercise

Script the same 2-step journey (browse → add to cart) in JMeter and k6. Compare: authoring speed, diff-ability in code review, and wiring a p95 threshold into CI.
