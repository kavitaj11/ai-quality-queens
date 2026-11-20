# Financial trading: mock assessment & test design

[17 · Module topics](../../modules/17-interview-prep.md) · [Curriculum index](../../README.md)

**Category** · DOMAIN SPECIFIC INTERVIEW PREP

The mock technical assessment brief (90 minutes, 100 points, pass 75) and Question 1: a complete scenario matrix for a money-transfer API — positive, negative, boundary, idempotency, and data-integrity coverage.

## Worked reference

````text
json
MOCK TECHNICAL ASSESSMENT
Senior Quality Engineer, API and Backend Automation
Recommended time: 90 minutes · Total points: 100
Suggested passing score: 75 · Strong candidate score: 85+

CANDIDATE INSTRUCTIONS
  You are joining a Quality Engineering team supporting financial
  account and transaction services. The team currently performs most
  API regression testing manually through Postman. Releases occur
  every two weeks, and regression testing takes approximately three
  days.
  Your assignment is to:
    • Design a repeatable API automation framework
    • Automate critical backend regression scenarios
    • Validate business logic and data integrity
    • Recommend sustainable testing practices
    • Explain how the framework will integrate with CI/CD
    • Ensure the team can maintain the solution after your engagement
  The focus is on your technical approach, test design, reusable
  automation, and ability to explain your decisions.

SYSTEM UNDER TEST
  POST /api/v1/accounts   → accountId, availableBalance, status
  POST /api/v1/transfers  → transferId, status, createdTimestamp
  (payloads on the "json" tab)

Q1  TEST SCENARIO DESIGN — the money transfer API
Cover: positive · negative · boundary · business rules ·
data integrity · security and authorization · duplicate transaction
prevention · downstream system behavior.

POSITIVE
  Transfer between two valid active accounts
  Transfer using the full available balance
  Transfer of a valid decimal amount
  Transfer between supported account types
  Correct balances after a completed transfer
  Correct transaction history for both accounts
  Successful response contains the expected transfer information

NEGATIVE
  Missing / invalid source account
  Missing / invalid destination account
  Inactive, closed, frozen, or restricted account
  Insufficient funds · zero-dollar transfer · negative amount
  Unsupported currency · currency mismatch between accounts
  Source and destination accounts are the same
  Missing or invalid authentication token
  User attempts to transfer from an unauthorized account
  Malformed JSON · missing required fields · invalid data types
  Excessive field lengths
  SQL injection or invalid special-character input

BOUNDARY
  Minimum allowable transfer · one cent below the minimum
  Maximum allowable transfer · one cent above the maximum
  Transfer equal to the available balance
  Transfer one cent above the available balance
  High-precision decimal values
  Daily transfer limit reached · transfer that would exceed it
  Monthly transaction threshold reached

IDEMPOTENCY
  Same request twice with the same idempotency key
  Different request details with the same idempotency key
  Retry after a client timeout
  Retry after the original transaction completes
  Concurrent requests using the same idempotency key

DATA INTEGRITY
  Source balance decreases exactly once
  Destination balance increases exactly once
  Total funds remain consistent
  Transaction record matches the API response
  Audit record is created
  No partial update occurs if the request fails
  Rollback occurs when a downstream service is unavailable
````

## Exercise

Write the scenario list from scratch in 15 minutes, then diff it against this one and note what you missed.
