# SQL for testers

**Hands-on project** · [SQL and data pipeline testing lab](../../../../practice-qa-projects/data-quality-testing-lab/)

> **Learning objective** · Design a small dataset that exposes join, aggregation and ranking errors.

## ◈ Lab setup · SQLite

Run these blocks in order in an empty SQLite database. Two customers share a name and one has no orders. Those details reveal errors that a fixture with unique names and one order per customer would miss.

```sql
PRAGMA foreign_keys = ON;
CREATE TABLE customers (id INTEGER PRIMARY KEY, name TEXT NOT NULL);
CREATE TABLE orders (
    id INTEGER PRIMARY KEY,
    customer_id INTEGER NOT NULL REFERENCES customers(id),
    cents INTEGER NOT NULL CHECK (cents >= 0)
);
INSERT INTO customers VALUES (1, 'Lee'), (2, 'Lee'), (3, 'Pat');
INSERT INTO orders VALUES (11, 1, 400), (12, 1, 200), (13, 3, 600);
```

## Count and total the orders

```sql
SELECT c.id, c.name, COUNT(o.id) AS order_count,
       COALESCE(SUM(o.cents), 0) AS total_cents
FROM customers c LEFT JOIN orders o ON o.customer_id = c.id
GROUP BY c.id, c.name
ORDER BY c.id;
```

| Customer ID | Name | Orders | Total cents |
|---|---|---:|---:|
| 1 | Lee | 2 | 600 |
| 2 | Lee | 0 | 0 |
| 3 | Pat | 1 | 600 |

Grouping by name alone would merge the two Lees. Counting `o.id` gives the second Lee zero orders because the joined order ID is NULL. Using `COUNT(*)` would count the retained customer row. To count only orders above a given amount while retaining every customer, put the amount condition in the join. To select only customers whose **combined** spend exceeds 500, add `HAVING SUM(o.cents) > 500` after grouping.

## Rank distinct amounts

```sql
WITH ranked AS (
    SELECT id, cents, DENSE_RANK() OVER (ORDER BY cents DESC) AS amount_rank
    FROM orders
)
SELECT id, cents FROM ranked WHERE amount_rank = 2 ORDER BY id;
```

Order 11 is second at 400 cents. Add another 600-cent order in a disposable copy. Both 600-cent rows share first place and 400 remains second. With `RANK()`, the next rank would be third.

## ✎ Practice · Verify a state change

```sql
BEGIN;
UPDATE orders SET cents = 450 WHERE id = 11;
SELECT SUM(cents) FROM orders WHERE customer_id = 1;
DELETE FROM orders WHERE id = 12;
SELECT COUNT(*) FROM orders WHERE customer_id = 1;
ROLLBACK;
```

Predict **650** after the update and **1** after the delete. Rerun the first report after rollback. Restoring the original totals is part of the exercise.

---

[Return to the training guide](../README.md)
