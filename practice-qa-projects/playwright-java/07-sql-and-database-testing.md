# 7. SQL and Database Testing

The practice shop stores products, orders, order lines and an audit log in SQLite (`practice-apps/shop-web/shop.db`). Money is stored as **integer cents**, a common pattern in financial systems that avoids floating-point errors entirely.

```
products(product_id, name, category, price_cents)
orders(order_id, username, status, total_cents, created_at)
order_lines(order_id, line_no, product_id, quantity, unit_price_cents, line_total_cents)
audit_log(audit_id, entity_type, entity_id, action, username, created_at)
```

## Why test the database?

The UI and API can both show the right number while the stored data is wrong: a missing audit row, a line saved twice, a total that doesn't match its lines. In regulated domains such as banking, the stored record **is** the truth.

## JDBC done right

`db/ShopDb.java`:

```java
private <T> Optional<T> queryOne(String sql, RowMapper<T> mapper, Object... params) {
    try (Connection c = DriverManager.getConnection(url); PreparedStatement ps = c.prepareStatement(sql)) {
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
        try (ResultSet rs = ps.executeQuery()) {
            return rs.next() ? Optional.ofNullable(mapper.map(rs)) : Optional.empty();
        }
    }
    ...
}

private static BigDecimal money(long cents) {
    return BigDecimal.valueOf(cents, 2);     // 4025 → 40.25, exact
}
```

- **PreparedStatement with parameters**, never string concatenation: no SQL injection, correct quoting, reusable plans. (The first draft of `auditCount` concatenated the action name into the SQL; it was changed to a parameter before it ran.)
- **try-with-resources** closes connections, statements and result sets even when an assertion fails.
- **Optional** makes "row not found" explicit instead of a `NullPointerException`.

## The checks

**The order exists with the right values** (UI scenario, `DatabaseSteps`):

```java
ShopDb.OrderRow row = ctx.db().findOrder(ctx.lastOrderId)
        .orElseThrow(() -> new AssertionError("Order " + ctx.lastOrderId + " is not in the database"));
assertThat(row.status()).isEqualTo(status);
assertThat(row.username()).isEqualTo(ctx.username);
assertThat(row.total()).isEqualByComparingTo(total);
```

**Reconciliation: header total = sum of the lines**:

```sql
SELECT o.total_cents - COALESCE(SUM(l.line_total_cents), 0)
FROM orders o LEFT JOIN order_lines l ON l.order_id = o.order_id
WHERE o.order_id = ?
GROUP BY o.order_id, o.total_cents
```

The test expects `0`. The `LEFT JOIN` and `COALESCE` make an order with **no** lines show up as a difference instead of disappearing from the result.

**Audit trail: exactly one CREATED entry**:

```sql
SELECT COUNT(*) FROM audit_log WHERE entity_type = 'ORDER' AND entity_id = ? AND action = ?
```

Exactly one: zero means the audit is missing, two means the order was processed twice.

**API vs database: prices agree** (`ProductApiTest.apiPricesMatchTheDatabase`): every product price returned by the API equals `price_cents / 100` in the `products` table.

## Additional SQL practice

```sql
-- Orders with no audit entry (LEFT JOIN ... IS NULL)
SELECT o.order_id FROM orders o
LEFT JOIN audit_log a ON a.entity_id = o.order_id AND a.action = 'CREATED'
WHERE a.entity_id IS NULL;

-- Duplicate lines for the same product in one order
SELECT order_id, product_id, COUNT(*) FROM order_lines
GROUP BY order_id, product_id HAVING COUNT(*) > 1;

-- Each user's most recent order (window function)
SELECT * FROM (
  SELECT o.*, ROW_NUMBER() OVER (PARTITION BY username ORDER BY created_at DESC) AS rn FROM orders o
) WHERE rn = 1;

-- Revenue by product
SELECT p.name, SUM(l.line_total_cents) / 100.0 AS revenue
FROM order_lines l JOIN products p ON p.product_id = l.product_id
GROUP BY p.name ORDER BY revenue DESC;
```

## Review questions

**How do you validate data in the database from Java tests?**
JDBC with PreparedStatements and try-with-resources in a small DB client class; map rows to records; compare money as BigDecimal.

**Why PreparedStatement?**
Parameter binding prevents SQL injection and quoting bugs, and the database can reuse the plan.

**What's a reconciliation check?**
Comparing two representations that must agree: header total vs sum of lines, source vs target counts, API vs database. The result should be zero differences.

**Why store money as integer cents (or DECIMAL)?**
Floating-point types can't represent most decimal fractions exactly, so sums drift. Integers or fixed-point DECIMAL are exact.

Next: [8. CI, debugging and reliability](08-ci-debugging-and-reliability.md)
