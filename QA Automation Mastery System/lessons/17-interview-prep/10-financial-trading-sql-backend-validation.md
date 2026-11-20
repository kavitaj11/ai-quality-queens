# Financial trading: SQL & backend validation

[17 · Module topics](../../modules/17-interview-prep.md) · [Curriculum index](../../README.md)

**Category** · DOMAIN SPECIFIC INTERVIEW PREP

Question 6 of the assessment: prove with SQL that exactly one transaction exists for an idempotency key, that its details match the API response, and that both balances landed — plus the risk of seeding test data straight into the database.

## Worked reference

````text
sql
Q6  SQL AND BACKEND VALIDATION

DATABASE TABLES
  accounts        account_id · customer_id · available_balance ·
                  currency · status · updated_timestamp
  transactions    transaction_id · idempotency_key ·
                  source_account_id · destination_account_id ·
                  amount · currency · status · created_timestamp

  6A  Confirm only ONE transaction exists for the idempotency key
      → expected result: transaction_count = 1
  6B  Validate the transaction details
  6C  Validate both account balances
  (queries on the "sql" tab)

FOLLOW-UP QUESTION
  What risk exists if automated tests directly update database
  records to create test data?

STRONG ANSWER
  "Direct database updates may bypass application validation,
   auditing, events, and business logic. I prefer creating test data
   through supported APIs or test-data services. If controlled
   database setup is necessary, it should be isolated, documented,
   and limited to approved nonproduction environments."
````

## Exercise

Write the same three queries against your own schema, then add a fourth that proves total funds are unchanged.
