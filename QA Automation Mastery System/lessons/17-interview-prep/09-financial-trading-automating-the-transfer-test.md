# Financial trading: automating the transfer test

[17 · Module topics](../../modules/17-interview-prep.md) · [Curriculum index](../../README.md)

**Category** · DOMAIN SPECIFIC INTERVIEW PREP

Question 2 of the assessment: a REST Assured test that proves the transfer completed, the schema matched, both balances moved exactly once, the transaction persisted, and a resubmit with the same idempotency key did not process twice — plus the follow-up questions and the answers that satisfy them.

## Worked reference

````text
json
Q2  AUTOMATE THE TRANSFER API

SCENARIO
  A customer transfers $1,000 from a valid source account containing
  $5,000 to a valid destination account containing $2,000.

THE TEST MUST VERIFY
  1. The response status code is correct.
  2. The transfer status is COMPLETED.
  3. The response matches the expected JSON schema.
  4. The source account balance becomes $4,000.
  5. The destination account balance becomes $3,000.
  6. A transaction record is created.
  7. The transfer is not processed twice if the request is
     resubmitted.

  Java + REST Assured, Python + pytest, Postman, ReadyAPI, Bruno, or
  another appropriate API automation tool are all acceptable.

FOLLOW-UP QUESTIONS THE INTERVIEWER SHOULD ASK
  • Why did you use BigDecimal instead of double?
  • Why did you capture balances before and after the transfer?
  • What should happen if the client times out but the server
    completes the transfer?
  • How would you ensure the verification does not pass because of
    previously existing data?
  • Would you validate the database directly in every API test?

STRONG ANSWER POINTS
  • BigDecimal avoids floating-point precision issues in financial
    calculations.
  • Capturing balances establishes the actual test baseline.
  • The same idempotency key should prevent duplicate financial
    processing.
  • Each test should create or identify unique data.
  • Database validation should be used carefully. Tests should prefer
    public service interfaces when possible, but targeted database
    validation can be appropriate for integration testing and
    critical financial reconciliation.

Q3  JSON SCHEMA VALIDATION — see the "json" tab

  Is schema validation enough to prove that the API is working
  correctly?
  "No. Schema validation confirms that the response structure and
   data types meet the contract, but it does not validate the
   business outcome. I would also validate response values, business
   rules, account balances, transaction persistence, authorization,
   downstream events, and audit records."
````

## Exercise

Re-implement this test in Python with pytest + requests, keeping Decimal for every monetary value.
