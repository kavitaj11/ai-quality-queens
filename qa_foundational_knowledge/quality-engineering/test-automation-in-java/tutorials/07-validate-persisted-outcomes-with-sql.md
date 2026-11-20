# 07. Validate persisted outcomes with SQL

**Advanced** · [Guide and prerequisites](../README.md) · [Worked example](../examples/sql/shipping.sql)

**QA Automation Mastery** · [Database validation](../../../../QA%20Automation%20Mastery%20System/lessons/02-test-harness-setup/14-database-validation.md)

## Query the invariant at the right grain

First define what one row represents and which key identifies the entity under test. Then translate the business invariant into a query that returns violations. An empty result supports a pass only after confirming that the intended fixture exists and that the query examined the required population.

For a notification service, suppose each scheduled reminder must produce exactly one delivery receipt. Start from expected reminders, left join receipts by reminder ID, group by reminder and count a non-null receipt key. Both zero and two receipts violate the rule. Starting from receipts alone hides missing deliveries. Before trusting the check, seed a known duplicate and a missing receipt and predict which identifiers the query should return.

## Apply the concepts in the lab

A correct browser message cannot establish what was stored. The SQL fixture includes four order rows and derives the expected fee independently from the requirement. Its query returns exceptions, making an empty result a clear acceptance condition on clean input.

## Run the example

From the guide directory, after completing the prerequisites in the guide:

```sh
sqlite3 :memory: < examples/sql/shipping.sql
```

## Interview question

**Why test for NULL explicitly?**

SQL comparisons involving NULL evaluate to unknown. A WHERE clause that checks only inequality would hide the missing fee. Include the missing-value condition and seed that defect to prove the query detects it.

## Practice

Run the script in an empty SQLite database. Expect IDs 3 and 4: an incorrect charge and a missing charge. Correct both fixture values and rerun in a fresh database. The query should return zero rows.
