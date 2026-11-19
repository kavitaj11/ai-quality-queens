# 07. Validate persisted outcomes with SQL

**Advanced** · [Guide and prerequisites](../README.md) · [Worked example](../examples/sql/shipping.sql)

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
