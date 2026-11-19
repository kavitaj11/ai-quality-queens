# Practice Apps

Small apps that run on your own machine, used as the system under test in the tutorials. They need only Python 3.10+ (standard library).

| App | Port | Used by | Run |
|---|---|---|---|
| [K11 Practice Shop (web)](shop-web/shop_web.py) | 8091 | [Selenium](../selenium-java/README.md), [Appium](../appium-java/README.md) (mobile web), [Playwright](../playwright-python/README.md), [Playwright for Java](../playwright-java/README.md) | `python shop-web/shop_web.py` |
| [Shop API with bottlenecks](../performance-testing-jmeter/demo-app/shop_api.py) | 8099 | [JMeter](../performance-testing-jmeter/README.md) | `python ../performance-testing-jmeter/demo-app/shop_api.py` |

## K11 Practice Shop

Pages: login (with validation and a locked account), products with **asynchronous search** (400–1200 ms behind a spinner, for practising waits), product details with a quantity dropdown, cart with totals, checkout and order confirmation. Every element tests need has a `data-testid`.

JSON API for test setup and mocking: `GET /api/products?q=`, `POST /api/login`, `POST /api/cart`, `POST /api/checkout`, `GET /api/orders/{orderId}` (owner only), `GET /health`. Unauthenticated `/api/*` calls get a JSON 401.

Database: products, orders, order lines and an audit log are stored in SQLite (`shop-web/shop.db`, change with `--db`), with money as integer cents, so tests can verify data with SQL. The file is created on first start and is git-ignored.

- Password for any username: `Passw0rd!` (the username `locked` is always rejected as locked).
- Every login gets its own in-memory session and cart, so tests can run in parallel.
- From an Android emulator, start it with `--host 0.0.0.0` and browse to `http://10.0.2.2:8091`.

These apps are for local practice only. Don't expose them to a network you don't control.
