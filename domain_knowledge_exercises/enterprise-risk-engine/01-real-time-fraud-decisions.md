# Real-time fraud decisions: Senior SDET exercise

**90 minutes · 100 points · Pass: 75 · Strong performance: 85+**

Login and money-movement services call a Risk Decision API. It enriches requests with device and history signals, evaluates rules and a model, and returns ALLOW, CHALLENGE (step-up authentication), or DENY. The peak-load API objective is p99 below 150 ms. Every completed decision is audited and published to the risk-decisions topic. Case management consumes those events. Today regression depends on slow, flaky UI tests, rule changes are checked manually, and releases have no performance gate.

Work through the eight prompts before consulting the reference notes. Use fictional clients, pinned policy fixtures and an isolated QA environment. Expected outcomes below describe this exercise only.

### Contract fixture

POST `/api/v1/risk/decisions`

~~~json
{"requestId":"f3c1a9e2-6b7d-4c1e-9a55-1d2f3e4a5b6c","clientId":"C123456","channel":"WEB","eventType":"MONEY_MOVEMENT","amount":25000.00,"currency":"USD","payeeId":"P998877","payeeAddedMinutesAgo":12,"deviceId":"D-unknown-55","ipCountry":"RO","clientHomeCountry":"US"}
~~~

Example HTTP 200 response:

~~~json
{"requestId":"f3c1a9e2-6b7d-4c1e-9a55-1d2f3e4a5b6c","decision":"DENY","riskScore":92,"reasonCodes":["NEW_PAYEE","NEW_DEVICE","GEO_MISMATCH","HIGH_AMOUNT"],"ruleVersion":"2026.11.2","decisionTimeMs":41}
~~~

HTTP 200 means the engine evaluated the request; it does not authorize the transfer. One 41 ms measurement does not establish p99.

### 1. Design the API test matrix (15 min · 20 points)

**Prompt:** Identify inputs, independent expected outcomes and evidence of side effects.

| Family | Reference checks |
|---|---|
| Decision | Exercise all three decisions across login and transfer. Test just below, at and above each *fixture* boundary, plus a conflict with a documented winning rule. Verify reason codes and rule version independently. |
| Validation | Distinguish optional missing signals from missing required fields. Cover unknown currencies, negative amounts, large values and excess decimal places. Rejected input must not create a business decision or case. |
| Replay | Repeat the same request ID and payload: same decision, one logical event. Test concurrent replay and changed payload with reused ID against the agreed conflict contract. Freeze model, enrichment, clock and rules for deterministic comparisons. |
| Access and audit | Exercise synthetic authorized and unauthorized callers and tenant isolation. Assert one correlated audit record per logical decision, recording versions without logging raw identifiers or payloads unnecessarily. |
| Dependency loss | Stub missing history and slow enrichment separately. Verify the approved fallback for the event type, its reason, audit trail and deadline; do not assume a universal fallback. |

### 2. Specify behaviour in Cucumber and Java (15 min · 20 points)

**Prompt:** Write business-readable examples with independent assertions. Make device state explicit in the benign control so a change in outcome has a clear explanation.

~~~gherkin
Feature: Transfer decisions under a pinned policy
  Scenario Outline: Evaluate a transfer
    Given the risk fixture has a "<device>" device in "<country>" and a payee aged <age> minutes
    When the client requests a transfer of <amount> USD
    Then the decision is "<result>" with reasons "<reasons>"

    Examples:
      | device  | country | age   | amount   | result    | reasons                                     |
      | unknown | RO      | 12    | 25000.00 | DENY      | NEW_PAYEE,NEW_DEVICE,GEO_MISMATCH,HIGH_AMOUNT |
      | unknown | US      | 12    | 500.00   | CHALLENGE | NEW_PAYEE,NEW_DEVICE                         |
      | known   | US      | 10080 | 500.00   | ALLOW     |                                             |
~~~

Reference glue (Cucumber Java, AssertJ). Supply a scenario-scoped QA adapter through dependency injection; it seeds synthetic US-home clients, freezes the model/history/clock, and calls the actual test API. The request builder, response and adapter are integration interfaces, not bundled implementations.

~~~java
public class RiskSteps {
    private final RiskFixture fixture;
    private RiskRequestBuilder request;
    private RiskResponse response;
    private String id;

    public RiskSteps(RiskFixture fixture) { this.fixture = fixture; }

    @Given("the risk fixture has a {string} device in {string} and a payee aged {int} minutes")
    public void seed(String device, String country, int age) {
        id = UUID.randomUUID().toString();
        request = fixture.transfer("2026.11.2", "US", device, country, age).requestId(id);
    }

    @When("the client requests a transfer of {bigdecimal} USD")
    public void transfer(BigDecimal amount) {
        response = fixture.postExpecting200(request.amount(amount).currency("USD").build());
    }

    @Then("the decision is {string} with reasons {string}")
    public void verify(String decision, String csv) {
        String[] reasons = csv.isBlank() ? new String[0] : csv.split(",");
        assertThat(response.requestId()).isEqualTo(id);
        assertThat(response.ruleVersion()).isEqualTo("2026.11.2");
        assertThat(response.decision()).isEqualTo(decision);
        assertThat(response.reasonCodes()).containsExactlyInAnyOrder(reasons);
    }
}
~~~

An empty expected list must actually verify no reasons. Keep business rules at the API layer; retain one thin UI journey for step-up presentation.

### 3. Follow the event to case management (10 min · 12 points)

**Prompt:** How would you distinguish an unpublished decision, a delayed consumer and a duplicate case?

**Reference:** For this fixture DENY opens a case; ALLOW is audited and published without opening one. Confirm the CHALLENGE routing policy separately. Start an isolated subscriber at a recorded offset before calling the API. Match request ID, stable decision/event identity, schema, decision and version. Poll case management by decision ID up to the agreed processing deadline. For a negative assertion, wait for the consumer checkpoint to pass the event. Repeat the API call, then separately redeliver the broker event: distinguish one logical publication from transport retries and assert exactly one case. Pause and resume the consumer; compare publisher acknowledgement, topic offset, consumer lag, dead-letter record and case ID to locate a failure.

### 4. Review a flawed test (8 min · 8 points)

~~~java
var result = api.decide(sharedRequest);
assertThat(result.status()).isEqualTo(200);
Thread.sleep(1000);
assertThat(cases.list().size()).isGreaterThan(0);
~~~

**Prompt:** How could this pass after the DENY rule or case consumer breaks? **Reference:** Any decision returns 200, and an old case satisfies the global count. A shared request may replay cached results. Use a fresh ID, frozen fixture and exact decision/reason assertions; poll for its case and inspect its source event. Test idempotency separately with an intentionally reused ID.

### 5. Build a fraud-pattern dataset (10 min · 10 points)

**Prompt:** Design reusable scenarios that exercise patterns over time without using customer records. **Reference:** Generate seeded histories (stable login, device change, new payee, repeated activity), label expected outcomes from a reviewed policy table and pair each pattern with a benign control. Include missing, stale and contradictory signals. Record generator seed, event sequence, feature snapshot, model and rule versions. Keep identities distinct across parallel runs and hold out entire scenario families for evaluation. Review false challenges on benign controls alongside missed high-risk fixtures; synthetic rates are not production fraud estimates.

### 6. Gate performance and resilience (12 min · 12 points)

**Prompt:** Can a release pass if p99 is 120 ms but 3% of calls time out? **Reference:** No. Successful calls alone hide failures. Agree peak arrival rate, traffic mix, measurement window, error budget and fallback limits before running. Drive a sustained open arrival rate with warm-up, record attempted/completed requests, timeouts and dropped iterations, and verify the generator sustained the target. Measure client-observed round-trip latency separately from decisionTimeMs. Gate on p99 below 150 ms at peak *and* the agreed error and correctness budgets. Measure event-to-case lag separately. In QA inject dependency delay, model unavailability and publisher interruption; verify bounded fallback, durable eventual publication and clean recovery without duplicate cases.

### 7. Design the automation architecture and pipeline (12 min · 12 points)

**Prompt:** Replace most UI regression while keeping coverage of the customer experience. **Reference:** Separate deterministic rule tests, HTTP and event contracts, enrichment stubs, broker/consumer integration, and thin UI smoke. On PRs run rule boundaries, priority and contract tests; on QA deploy run the three pinned decisions through API, audit and consumer. Nightly run replay, dependency failures and broader model regression. Before release run production-shaped peak and soak tests on isolated infrastructure. Attach versioned configuration, redacted correlation evidence, latency distributions and case-lag results. A wrong decision, missing audit/event, duplicate case or failed performance gate blocks promotion; verify rollback with the previous rule/model pair.

### 8. Use GenAI responsibly (8 min · 6 points)

**Prompt:** Propose a pilot with a measurable benefit and a stop condition. **Reference:** Give an approved assistant a fictional rule-change specification and ask for candidate boundary tests. A reviewer derives the expected decisions independently, rejects invented rules and runs the accepted tests against seeded defects. Compare defects caught, false assertions and review time with manual design. Use synthetic inputs only. Stop the pilot if sensitive data appears or an unsupported oracle reaches the release gate; the assistant must not make live fraud or case decisions.

**Scoring:** Use the points shown for each part. Award full credit for observable outcomes and evidence tied to the same request; award partial credit for a relevant scenario without a workable oracle.
