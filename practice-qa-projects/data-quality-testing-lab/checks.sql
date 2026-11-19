-- Each returned row is a failure. Compare by business key before comparing totals.
SELECT 'duplicate source key' AS issue, order_id AS key FROM staging
GROUP BY order_id HAVING COUNT(*) > 1
UNION ALL
SELECT 'unknown customer', s.order_id FROM staging s
LEFT JOIN customer c USING(customer_id) WHERE c.customer_id IS NULL
UNION ALL
SELECT 'missing or changed fact', s.order_id FROM staging s
LEFT JOIN fact f USING(order_id)
WHERE f.order_id IS NULL OR (s.customer_id,s.order_date,s.cents) IS NOT (f.customer_id,f.order_date,f.cents)
UNION ALL
SELECT 'unexpected fact', f.order_id FROM fact f
LEFT JOIN staging s USING(order_id) WHERE s.order_id IS NULL
UNION ALL
SELECT 'daily count or amount mismatch', order_date FROM (
 SELECT order_date, 1 AS n, cents AS amount FROM staging
 UNION ALL SELECT order_date, -1, -cents FROM fact
) GROUP BY order_date HAVING SUM(n) <> 0 OR SUM(amount) <> 0;
