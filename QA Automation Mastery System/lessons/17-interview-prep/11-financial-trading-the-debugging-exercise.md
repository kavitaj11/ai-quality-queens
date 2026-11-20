# Financial trading: the debugging exercise

[17 · Module topics](../../modules/17-interview-prep.md) · [Curriculum index](../../README.md)

**Category** · DOMAIN SPECIFIC INTERVIEW PREP

Question 5 of the assessment: a deliberately bad REST Assured test. The interviewer asks for at least eight problems; twenty are findable. Know the list and the polling fix for the fixed sleep.

## Worked reference

````text
Q5  IDENTIFY THE PROBLEMS  (test on the "java" tab)

Ask the candidate to identify at least eight problems.

EXPECTED FINDINGS
   1. Authentication token is hard-coded.
   2. Base URL is hard-coded.
   3. Test data is hard-coded.
   4. Content type is not specified.
   5. Request is constructed as a raw string.
   6. Test does not verify the response schema.
   7. Test does not verify the transfer status.
   8. Test does not verify the destination balance.
   9. Test assumes the source balance began at $5,000.
  10. Uses a fixed Thread.sleep.
  11. Test does not handle asynchronous processing correctly.
  12. No idempotency key is provided.
  13. Duplicate transaction behavior is not tested.
  14. No transaction or audit-record validation.
  15. Monetary values are validated as integers.
  16. No logging or meaningful failure message.
  17. No cleanup or isolated test-data strategy.
  18. API client logic is embedded inside the test.
  19. Exact success status may be incorrect depending on the API
      contract.
  20. No negative-path or rollback validation.

STRONG CANDIDATE EXPLANATION
  "I would replace the fixed wait with polling that checks for a
   defined completed or failed state. The polling mechanism should
   have an interval, timeout, and meaningful error message. A fixed
   sleep makes the suite slow and unreliable."

Q8  PARTIAL TRANSFER FAILURE
  Source account is debited $1,000 · destination is not credited ·
  API returns 500 Internal Server Error.

  "The system should maintain transactional consistency. It should
   either complete both sides of the transfer or roll back the
   debit. If the architecture uses asynchronous processing, the
   transaction may temporarily remain pending, but there must be a
   controlled recovery or compensation process.
   I would validate the transfer status, both account balances,
   transaction records, event messages, correlation IDs, retry
   behavior, dead-letter queues, compensation activity, and audit
   logs.
   I would trace the request using the correlation ID across the API
   gateway, transfer service, database operations, event broker, and
   destination account service. I would also verify that retrying
   the request with the same idempotency key cannot cause an
   additional debit."
````

## Exercise

Rewrite the broken test correctly — config-driven, polled, idempotent, schema-validated — in under 20 minutes.
