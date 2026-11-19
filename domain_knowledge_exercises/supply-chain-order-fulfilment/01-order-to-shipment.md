# Order to shipment: Test Automation Lead exercise

**90 minutes · 100 points · Pass: 75 · Strong performance: 85+**

An OMS accepts orders through a portal and REST API, reserves stock, and releases orders to a WMS through a queue. The WMS confirms shipments; a nightly load publishes orders and shipments to a reporting warehouse. Your distributed team spends three days on mostly manual regression. A small Pytest API suite exists, but there are no UI tests.

Submit a scenario matrix, UI and API tests, reconciliation SQL, a test review, an AI pilot, a release plan and a handoff note. Attempt each prompt before reading the reference. Examples assume a synthetic QA environment; fixture names describe adapters to implement for your platform.

**Example contract:** POST `/api/v1/orders`

~~~json
{"customerId":"CUST-778","shipTo":"Austin, TX","lines":[{"sku":"SKU-1001","quantity":2}]}
~~~

At $29.99 per unit with no tax or freight, expect 201 and:

~~~json
{"orderId":"ORD-50021","status":"CREATED","lines":[{"sku":"SKU-1001","quantity":2,"reserved":2}],"totalAmount":59.98}
~~~

### 1. Trace the integration boundaries (15 min · 20 points)

**Prompt:** For each boundary, identify an input, observable outcome and failure that a green HTTP status could hide.

| Boundary | Reference checks |
|---|---|
| Portal/API → OMS | Compare equivalent orders by normalized lines, address and total. Reject missing addresses, unknown SKUs and nonpositive quantities without side effects. Agree whether insufficient stock means backorder or rejection. |
| OMS → inventory | From five units, ordering two leaves three. Race two distinct orders for the last unit; only one reserves it. Cancellation restores it exactly once. |
| OMS → queue → WMS | Check schema, version, address, order and line IDs. Stop the test consumer, release an order, then resume. Redelivery is acceptable; assert one logical release and one pick request. Route malformed messages to the dead-letter queue with a reason. |
| WMS → OMS | Confirm one of two units: line shows one shipped and order is not fully shipped. Replay the same shipment ID: quantity stays one. Confirm the remainder with tracking and assert SHIPPED. |
| OMS/WMS → reporting | Compare keys, quantities and amounts for the same cutoff. A late confirmation must update the agreed business period once, even after a batch replay. |

### 2. Automate the portal journey (12 min · 15 points)

**Prompt:** Place an order, verify what the customer sees and identify the same order through the API. Reference using Playwright's synchronous Python API:

~~~python
from urllib.parse import urlsplit
from playwright.sync_api import expect

def test_place_order(authenticated_page, catalog):
    page = authenticated_page
    sku = catalog.seed(price_cents=2999, available=5)
    page.goto("/orders/new")
    page.get_by_label("SKU").fill(sku)
    page.get_by_label("Quantity").fill("2")
    page.get_by_role("button", name="Add line").click()
    with page.expect_response(lambda r: r.request.method == "POST" and
                              urlsplit(r.url).path == "/api/v1/orders") as result:
        page.get_by_role("button", name="Place order").click()
    response = result.value
    assert response.status == 201
    order_id = response.json()["orderId"]
    expect(page.get_by_test_id("order-id")).to_have_text(order_id)
    expect(page.get_by_test_id("order-status")).to_have_text("CREATED")
    expect(page.get_by_test_id("order-total")).to_have_text("$59.98")
~~~

Configure a base URL. Give each test its own browser context initialized from a test customer's storage state. Move repeated interactions into a page object as more journeys appear. Isolate stock per test even when browser contexts are separate.

### 3. Exercise the API contract (13 min · 15 points)

**Prompt:** Cover validation, reservation and duplicate submission. Assume a requests-style authenticated client, synchronous reservation and successful same-key replay returning 200 or 201. Adapt these expectations to the agreed contract.

~~~python
import pytest
from uuid import uuid4

@pytest.fixture
def order(customer, inventory):
    sku = inventory.seed(available=5, price_cents=2999)
    return {"customerId": customer.id, "shipTo": customer.address,
            "lines": [{"sku": sku, "quantity": 2}]}

@pytest.mark.parametrize("case,status", [
    ("valid", 201), ("zero", 400), ("negative", 400),
    ("unknown", 422), ("empty", 400), ("no-address", 400)])
def test_validation(api, inventory, order, case, status):
    line = order["lines"][0]
    sku = line["sku"]
    if case in ("zero", "negative"): line["quantity"] = 0 if case == "zero" else -1
    if case == "unknown": line["sku"] = "ABSENT-SKU"
    if case == "empty": order["lines"] = []
    if case == "no-address": del order["shipTo"]
    r = api.post("/api/v1/orders", json=order)
    assert r.status_code == status
    assert inventory.available(sku) == (3 if status == 201 else 5)

def test_retry(api, inventory, order):
    headers = {"Idempotency-Key": str(uuid4())}
    first, retry = [api.post("/api/v1/orders", json=order, headers=headers)
                    for _ in range(2)]
    assert first.status_code == 201 and retry.status_code in (200, 201)
    assert first.json()["orderId"] == retry.json()["orderId"]
    assert inventory.available(order["lines"][0]["sku"]) == 3
~~~

For asynchronous reservation, poll the correlated reservation to a deadline. On timeout report the last state; do not resend the order.

### 4. Reconcile operational and reporting data (12 min · 15 points)

**Prompt:** Show how to catch a missing order offset by a duplicate and a partial shipment counted twice. Assume source and target snapshots share a cutoff, amounts use integer cents and keys include tenant. Run this query on order-line snapshots; an empty result passes.

~~~sql
WITH delta AS (
  SELECT tenant_id, order_id, line_id, 1 AS n, qty, paid_cents
  FROM oms_line_snapshot WHERE batch_id = :batch
  UNION ALL
  SELECT tenant_id, order_id, line_id, -1, -qty, -paid_cents
  FROM wh_line_snapshot WHERE batch_id = :batch
)
SELECT tenant_id, order_id, line_id, SUM(n) AS count_delta,
       SUM(qty) AS qty_delta, SUM(paid_cents) AS amount_delta
FROM delta
GROUP BY tenant_id, order_id, line_id
HAVING SUM(n) <> 0 OR SUM(qty) <> 0 OR SUM(paid_cents) <> 0;
~~~

Assert non-null measures and unique business keys separately on each side. Repeat the comparison at shipment-line grain (tenant, shipment ID, order line), using shipped quantity. Aggregate shipments to order line **before** joining to order amounts. Two one-unit shipments against one $59.98 line must still report $59.98, not $119.96. Track event time and load time separately for late arrivals.

### 5. Review a flawed test (8 min · 10 points)

~~~python
def test_order(page):
    page.goto("/orders/new")
    page.get_by_text("Place order").click()
    page.wait_for_timeout(5000)
    print(page.get_by_test_id("order-total").inner_text())
~~~

**Prompt:** Could this pass after checkout breaks? **Reference:** Yes: a stale total can print even if submission failed. Seed a priced SKU, establish a customer session, fill the cart, bind the click to the creation response, and assert the returned ID, rendered status and independent expected total. Capture the trace on failure and clean up only the test's records.

### 6. Pilot AI-assisted testing (8 min · 8 points)

**Prompt:** Choose one use case and define a stop condition. **Reference:** Feed a redacted WMS schema change to a generator and ask for changed-field and replay test cases. A reviewer maps each proposed oracle to the contract. Compare against human-authored cases on held-out changes: measure known defects caught, unsupported assertions and review time. Stop if sensitive fields appear or reviewed suggestions miss a critical contract defect. Keep generated cases out of release gates until reviewed and reproducible.

### 7. Build a framework and release flow (12 min · 10 points)

**Prompt:** Reduce the three-day regression cycle without hiding integration failures. **Reference:** Separate portal page objects, service clients, event probes, warehouse queries and fixture factories. Tag tests by boundary and cost. On PRs run contract, validation and isolated component tests. After QA deployment run one portal-to-reservation smoke. Nightly run queue replay, last-unit concurrency and post-load SQL. Before release, exercise peak order volume and consumer recovery. Block on overselling, duplicate picks or unexplained data deltas. Attach build ID, batch watermark, order correlation ID and last observed event; retries must not erase original failures.

### 8. Plan the distributed handoff (10 min · 7 points)

**Prompt:** A partial shipment is doubled in reporting but correct in OMS. Who owns the next step? **Reference:** The onsite lead confirms the expected grain with reporting and operations and owns release risk. The offshore automation owner reproduces with a two-shipment fixture and compares event, staging and fact keys. Record build, synthetic IDs, cutoff, query output, impact, named owner and next checkpoint in a shared defect. Use overlap time to agree the first divergent boundary. Close after a replay and a fresh nightly load both reconcile.

**Scoring:** Award each part up to its stated points for concrete inputs, observable expected results and a way to distinguish failure from delay or test-data drift. Give partial credit for correct scenarios without a usable oracle.
