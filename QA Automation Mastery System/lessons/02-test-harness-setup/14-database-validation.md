# Database validation

[02 · Module topics](../../modules/02-test-harness-setup.md) · [Curriculum index](../../README.md)

**Category** · DEVTOOLS & DATA CHECKS

The UI can lie — a green success toast doesn't prove the row was written, the status changed, or the job row was deleted. Database validation closes the loop: after an action, query the DB directly and assert the persisted state. Use a read-only test account, parameterized queries (never string-concat), and clean up what you insert. Common checks: row exists / count changed, a column holds the expected value, a soft-delete flag flipped, and no orphaned/duplicate rows.

## Worked reference

````text
import psycopg2   # or sqlite3 / mysql-connector / SQLAlchemy

conn = psycopg2.connect(dsn=os.environ["TEST_DB_URL"])
cur = conn.cursor()

# After the UI 'place order' action, verify PERSISTED state.
# ALWAYS parameterize — never f-string user data into SQL.
cur.execute(
    "SELECT status, total FROM orders WHERE id = %s", (order_id,))
row = cur.fetchone()
assert row is not None,          "order was not persisted"
assert row[0] == "pending",      f"status was {row[0]}"

# Count check: order_items must match the cart
cur.execute(
    "SELECT COUNT(*) FROM order_items WHERE order_id = %s",
    (order_id,))
assert cur.fetchone()[0] == 3

# Soft-delete check: cart cleared, not the account
cur.execute(
    "SELECT COUNT(*) FROM cart_items WHERE user_id = %s",
    (user_id,))
assert cur.fetchone()[0] == 0

conn.rollback()   # read-only test: undo anything you touched
cur.close(); conn.close()

# As a pytest fixture, connection setup/teardown is reusable:
#   @pytest.fixture
#   def db():
#       conn = psycopg2.connect(...); yield conn; conn.close()
````

## Exercise

For a 'place order' flow: assert via SQL that exactly one orders row was created with status='pending', the order_items count matches the cart, and the user's cart rows were cleared. Wrap it so the test data is deleted afterward.
