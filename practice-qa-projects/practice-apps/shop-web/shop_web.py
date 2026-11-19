"""K11 Practice Shop: a small web app for UI automation practice (Selenium, Playwright, Appium).

Standard library only. Run it, then point your tests at http://127.0.0.1:8091.

    python shop_web.py                 # http://127.0.0.1:8091
    python shop_web.py --host 0.0.0.0  # reachable from an Android emulator as http://10.0.2.2:8091

Pages (every element tests need has a data-testid attribute):
    /login                  form with validation errors
    /products?q=<text>      search; results load asynchronously behind a spinner (practise waits)
    /products/<id>          details, quantity dropdown, "Add to cart" (shows a toast)
    /cart                   table of lines, total, "Checkout"
    /orders/<orderId>       order confirmation
    /logout

JSON API (for API-based test setup and network mocking):
    GET  /api/products?q=<text>   used by the products page (deliberately slow: 400-1200 ms)
    POST /api/login               {"username", "password"} -> sets the session cookie
    POST /api/cart                {"productId", "quantity"} (needs the session cookie)
    POST /api/checkout            places an order from the cart -> 201 with the order (409 if the cart is empty)
    GET  /api/orders/<orderId>    the order as JSON (only the user who placed it can read it)
    GET  /health

Database: products, orders, order lines and an audit log are stored in SQLite (default:
shop.db next to this file; change with --db). Money is stored as integer cents. Tables:
    products(product_id, name, category, price_cents)
    orders(order_id, username, status, total_cents, created_at)
    order_lines(order_id, line_no, product_id, quantity, unit_price_cents, line_total_cents)
    audit_log(audit_id, entity_type, entity_id, action, username, created_at)

Every user gets an isolated in-memory session, so parallel tests do not interfere.
Valid password for any username: Passw0rd!   (the username "locked" is rejected as locked)
Never expose this server beyond your own machine or emulator; it is a teaching tool.
"""
import argparse
import html
import json
import random
import secrets
import sqlite3
import threading
import time
from datetime import datetime, timezone
from decimal import Decimal
from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer
from pathlib import Path
from urllib.parse import parse_qs, urlparse

PASSWORD = "Passw0rd!"
PRODUCTS = [
    {"id": 1001, "name": "Blue Mug", "price": 12.50, "category": "Kitchen"},
    {"id": 1002, "name": "Travel Mug", "price": 18.00, "category": "Kitchen"},
    {"id": 1003, "name": "Desk Lamp", "price": 34.99, "category": "Office"},
    {"id": 1004, "name": "Smart Lamp", "price": 59.00, "category": "Office"},
    {"id": 1005, "name": "Hiking Backpack", "price": 79.95, "category": "Outdoor"},
    {"id": 1006, "name": "Eco Bottle", "price": 15.25, "category": "Outdoor"},
    {"id": 1007, "name": "Noise-Cancelling Headphones", "price": 149.00, "category": "Electronics"},
    {"id": 1008, "name": "Ergonomic Chair", "price": 229.00, "category": "Office"},
]
BY_ID = {p["id"]: p for p in PRODUCTS}

SESSIONS = {}  # session id -> {"user": str, "cart": [{"productId", "quantity"}], "orders": {...}}
LOCK = threading.Lock()


def cents(price) -> int:
    """Money is converted through Decimal, never float arithmetic."""
    return int((Decimal(str(price)) * 100).to_integral_value())


class ShopDb:
    """SQLite storage for products, orders and the audit log, so tests can verify data with SQL."""

    SCHEMA = """
    CREATE TABLE IF NOT EXISTS products (
        product_id INTEGER PRIMARY KEY, name TEXT NOT NULL, category TEXT NOT NULL, price_cents INTEGER NOT NULL);
    CREATE TABLE IF NOT EXISTS orders (
        order_id TEXT PRIMARY KEY, username TEXT NOT NULL, status TEXT NOT NULL,
        total_cents INTEGER NOT NULL, created_at TEXT NOT NULL);
    CREATE TABLE IF NOT EXISTS order_lines (
        order_id TEXT NOT NULL REFERENCES orders(order_id), line_no INTEGER NOT NULL,
        product_id INTEGER NOT NULL REFERENCES products(product_id), quantity INTEGER NOT NULL,
        unit_price_cents INTEGER NOT NULL, line_total_cents INTEGER NOT NULL,
        PRIMARY KEY (order_id, line_no));
    CREATE TABLE IF NOT EXISTS audit_log (
        audit_id INTEGER PRIMARY KEY AUTOINCREMENT, entity_type TEXT NOT NULL, entity_id TEXT NOT NULL,
        action TEXT NOT NULL, username TEXT NOT NULL, created_at TEXT NOT NULL);
    """

    def __init__(self, path):
        self.path = str(path)
        self.lock = threading.Lock()
        self.conn = sqlite3.connect(self.path, check_same_thread=False)
        with self.lock, self.conn:
            self.conn.executescript(self.SCHEMA)
            self.conn.executemany(
                "INSERT OR REPLACE INTO products(product_id, name, category, price_cents) VALUES (?, ?, ?, ?)",
                [(p["id"], p["name"], p["category"], cents(p["price"])) for p in PRODUCTS])

    def save_order(self, order_id, username, lines):
        """Write the order, its lines and an audit entry in one transaction."""
        now = datetime.now(timezone.utc).isoformat(timespec="seconds")
        rows = []
        for n, line in enumerate(lines, start=1):
            unit = cents(BY_ID[line["productId"]]["price"])
            rows.append((order_id, n, line["productId"], line["quantity"], unit, unit * line["quantity"]))
        total = sum(r[5] for r in rows)
        with self.lock, self.conn:
            self.conn.execute("INSERT INTO orders VALUES (?, ?, 'CONFIRMED', ?, ?)", (order_id, username, total, now))
            self.conn.executemany("INSERT INTO order_lines VALUES (?, ?, ?, ?, ?, ?)", rows)
            self.conn.execute(
                "INSERT INTO audit_log(entity_type, entity_id, action, username, created_at) "
                "VALUES ('ORDER', ?, 'CREATED', ?, ?)", (order_id, username, now))
        return total, now


DB = None  # set in main()


def place_order(session):
    """Turn the session's cart into an order. Returns (order_id, order) or (None, None) if the cart is empty."""
    with LOCK:
        if not session["cart"]:
            return None, None
        lines = [dict(line) for line in session["cart"]]
        session["cart"] = []
    order_id = "ORD-" + secrets.token_hex(4).upper()
    total_cents, created_at = DB.save_order(order_id, session["user"], lines)
    order = {"lines": lines, "total": total_cents / 100, "created_at": created_at}
    with LOCK:
        session["orders"][order_id] = order
    return order_id, order


def order_json(order_id, username, order):
    lines = []
    for line in order["lines"]:
        p = BY_ID[line["productId"]]
        unit = cents(p["price"])
        lines.append({"productId": p["id"], "name": p["name"], "quantity": line["quantity"],
                      "unitPrice": unit / 100, "lineTotal": unit * line["quantity"] / 100})
    return {"orderId": order_id, "status": "CONFIRMED", "username": username, "currency": "USD",
            "total": order["total"], "createdAt": order["created_at"], "lines": lines}

CSS = """
body{font-family:system-ui,Segoe UI,Arial,sans-serif;margin:0;background:#f6f7f9;color:#1d2330}
header{background:#1f4e79;color:#fff;padding:12px 16px;display:flex;justify-content:space-between;align-items:center}
header a{color:#fff;margin-left:12px}
main{max-width:860px;margin:0 auto;padding:16px}
.card{background:#fff;border:1px solid #dde2ea;border-radius:8px;padding:12px;margin:8px 0}
.grid{display:grid;grid-template-columns:repeat(auto-fill,minmax(180px,1fr));gap:12px}
input,select,button{font-size:16px;padding:8px;margin:4px 0;border-radius:6px;border:1px solid #b8c1cf}
button{background:#1f4e79;color:#fff;border:none;cursor:pointer}
.error{color:#b00020}.toast{background:#e6f4ea;border:1px solid #9bd3ae;padding:8px;border-radius:6px}
.spinner{color:#555;font-style:italic}
table{width:100%;border-collapse:collapse}td,th{padding:8px;border-bottom:1px solid #dde2ea;text-align:left}
"""


def page(title, body, user=None):
    nav = (f'<span data-testid="welcome">Hi, {html.escape(user)}</span>'
           '<span><a href="/products" data-testid="nav-products">Products</a>'
           '<a href="/cart" data-testid="nav-cart">Cart</a>'
           '<a href="/logout" data-testid="nav-logout">Log out</a></span>') if user else "<span></span>"
    return f"""<!doctype html><html lang="en"><head><meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1"><title>{html.escape(title)} | K11 Practice Shop</title>
<style>{CSS}</style></head><body><header><strong>K11 Practice Shop</strong>{nav}</header><main>{body}</main></body></html>"""


def login_page(error=""):
    err = f'<p class="error" role="alert" data-testid="login-error">{html.escape(error)}</p>' if error else ""
    return page("Log in", f"""
<h1>Log in</h1>{err}
<form method="post" action="/login" class="card" data-testid="login-form">
  <label for="username">Username</label><br><input id="username" name="username" data-testid="username" autocomplete="username"><br>
  <label for="password">Password</label><br><input id="password" name="password" type="password" data-testid="password" autocomplete="current-password"><br>
  <button type="submit" data-testid="login-button">Log in</button>
</form>""")


PRODUCTS_JS = """
<script>
const q = new URLSearchParams(location.search).get('q') || '';
document.getElementById('search').value = q;
const results = document.getElementById('results');
const spinner = document.getElementById('spinner');
spinner.hidden = false;
fetch('/api/products?q=' + encodeURIComponent(q))
  .then(r => { if (!r.ok) throw new Error('HTTP ' + r.status); return r.json(); })
  .then(data => {
    spinner.hidden = true;
    document.getElementById('result-count').textContent = data.count + ' result(s)';
    if (!data.items.length) { results.innerHTML = '<p data-testid="no-results">No products match your search.</p>'; return; }
    results.innerHTML = data.items.map(p =>
      `<div class="card" data-testid="product-card" data-product-id="${p.id}">
         <div data-testid="product-name">${p.name}</div>
         <div data-testid="product-price">$${p.price.toFixed(2)}</div>
         <a href="/products/${p.id}" data-testid="view-product">View</a></div>`).join('');
  })
  .catch(err => {
    spinner.hidden = true;
    results.innerHTML = '<p class="error" role="alert" data-testid="search-error">Could not load products. Please try again.</p>';
  });
</script>"""


def products_page(user):
    return page("Products", f"""
<h1>Products</h1>
<form method="get" action="/products" data-testid="search-form">
  <input id="search" name="q" placeholder="Search products" data-testid="search-input" aria-label="Search products">
  <button type="submit" data-testid="search-button">Search</button>
</form>
<p id="spinner" class="spinner" data-testid="spinner" hidden>Loading products...</p>
<p id="result-count" data-testid="result-count"></p>
<div id="results" class="grid" data-testid="results"></div>
{PRODUCTS_JS}""", user)


def product_page(user, product):
    options = "".join(f'<option value="{n}">{n}</option>' for n in range(1, 6))
    return page(product["name"], f"""
<div class="card" data-testid="product-detail" data-product-id="{product['id']}">
  <h1 data-testid="product-title">{html.escape(product['name'])}</h1>
  <p>Category: <span data-testid="product-category">{product['category']}</span></p>
  <p>Price: <strong data-testid="product-detail-price">${product['price']:.2f}</strong></p>
  <form method="post" action="/cart" data-testid="add-to-cart-form">
    <input type="hidden" name="productId" value="{product['id']}">
    <label for="quantity">Quantity</label>
    <select id="quantity" name="quantity" data-testid="quantity">{options}</select>
    <button type="submit" data-testid="add-to-cart">Add to cart</button>
  </form>
</div>""", user)


def cart_page(user, cart, added=False):
    toast = '<p class="toast" role="status" data-testid="toast">Added to cart</p>' if added else ""
    if not cart:
        return page("Cart", f'{toast}<h1>Your cart</h1><p data-testid="empty-cart">Your cart is empty.</p>', user)
    rows, total = [], 0.0
    for line in cart:
        p = BY_ID[line["productId"]]
        subtotal = p["price"] * line["quantity"]
        total += subtotal
        rows.append(f'<tr data-testid="cart-row"><td data-testid="cart-item-name">{html.escape(p["name"])}</td>'
                    f'<td data-testid="cart-item-qty">{line["quantity"]}</td>'
                    f'<td data-testid="cart-item-subtotal">${subtotal:.2f}</td></tr>')
    return page("Cart", f"""{toast}<h1>Your cart</h1>
<table data-testid="cart-table"><thead><tr><th>Item</th><th>Qty</th><th>Subtotal</th></tr></thead>
<tbody>{''.join(rows)}</tbody></table>
<p>Total: <strong data-testid="cart-total">${total:.2f}</strong></p>
<form method="post" action="/checkout"><button type="submit" data-testid="checkout">Checkout</button></form>""", user)


def order_page(user, order_id, order):
    return page("Order confirmed", f"""
<div class="card" data-testid="order-confirmation">
  <h1>Thank you!</h1>
  <p>Order <strong data-testid="order-id">{order_id}</strong> is <span data-testid="order-status">CONFIRMED</span>.</p>
  <p>Total paid: <strong data-testid="order-total">${order['total']:.2f}</strong></p>
  <a href="/products" data-testid="continue-shopping">Continue shopping</a>
</div>""", user)


class Handler(BaseHTTPRequestHandler):
    server_version = "K11PracticeShop/1.0"

    def log_message(self, fmt, *args):
        pass

    # ---- helpers -------------------------------------------------------------
    def _session(self):
        for part in self.headers.get("Cookie", "").split(";"):
            name, _, value = part.strip().partition("=")
            if name == "sid" and value in SESSIONS:
                return value, SESSIONS[value]
        return None, None

    def _send(self, status, body, content_type="text/html; charset=utf-8", headers=None):
        data = body.encode() if isinstance(body, str) else body
        self.send_response(status)
        self.send_header("Content-Type", content_type)
        self.send_header("Content-Length", str(len(data)))
        for k, v in (headers or {}).items():
            self.send_header(k, v)
        self.end_headers()
        self.wfile.write(data)

    def _json(self, status, obj, headers=None):
        self._send(status, json.dumps(obj), "application/json", headers)

    def _redirect(self, location, headers=None):
        self._send(303, "", headers={"Location": location, **(headers or {})})

    def _form(self):
        length = int(self.headers.get("Content-Length") or 0)
        raw = self.rfile.read(length).decode() if length else ""
        if "application/json" in self.headers.get("Content-Type", ""):
            try:
                return json.loads(raw or "{}")
            except json.JSONDecodeError:
                return None
        return {k: v[0] for k, v in parse_qs(raw).items()}

    def _start_session(self, username):
        sid = secrets.token_hex(16)
        with LOCK:
            SESSIONS[sid] = {"user": username, "cart": [], "orders": {}}
        return sid, {"Set-Cookie": f"sid={sid}; Path=/; HttpOnly; SameSite=Lax"}

    @staticmethod
    def _check_login(username, password):
        if not username or not password:
            return "Username and password are required."
        if username == "locked":
            return "This account is locked. Contact support."
        if password != PASSWORD:
            return "Invalid username or password."
        return ""

    # ---- routes --------------------------------------------------------------
    def do_GET(self):
        url = urlparse(self.path)
        path = url.path
        if path == "/health":
            return self._json(200, {"status": "UP"})
        if path == "/":
            return self._redirect("/products")
        if path == "/login":
            return self._send(200, login_page())
        if path == "/api/products":
            time.sleep(random.uniform(0.4, 1.2))  # realistic latency: tests must wait properly
            q = parse_qs(url.query).get("q", [""])[0].strip().lower()
            items = [p for p in PRODUCTS if q in p["name"].lower() or q in p["category"].lower()]
            return self._json(200, {"items": items, "count": len(items)})

        sid, session = self._session()
        if not session:
            if path.startswith("/api/"):
                return self._json(401, {"error": "not logged in"})
            return self._redirect("/login")
        if path == "/logout":
            with LOCK:
                SESSIONS.pop(sid, None)
            return self._redirect("/login", {"Set-Cookie": "sid=; Path=/; Max-Age=0"})
        if path == "/products":
            return self._send(200, products_page(session["user"]))
        if path.startswith("/products/"):
            try:
                product = BY_ID[int(path.rsplit("/", 1)[1])]
            except (ValueError, KeyError):
                return self._send(404, page("Not found", '<h1 data-testid="not-found">Product not found</h1>', session["user"]))
            return self._send(200, product_page(session["user"], product))
        if path == "/cart":
            added = parse_qs(url.query).get("added", [""])[0] == "1"
            return self._send(200, cart_page(session["user"], session["cart"], added))
        if path.startswith("/api/orders/"):
            order_id = path.rsplit("/", 1)[1]
            order = session["orders"].get(order_id)   # users can only read their own orders
            if not order:
                return self._json(404, {"error": "order not found"})
            return self._json(200, order_json(order_id, session["user"], order))
        if path.startswith("/orders/"):
            order_id = path.rsplit("/", 1)[1]
            order = session["orders"].get(order_id)
            if not order:
                return self._send(404, page("Not found", '<h1 data-testid="not-found">Order not found</h1>', session["user"]))
            return self._send(200, order_page(session["user"], order_id, order))
        return self._send(404, page("Not found", '<h1 data-testid="not-found">Page not found</h1>', session["user"]))

    def do_POST(self):
        path = urlparse(self.path).path
        form = self._form()
        if form is None:
            return self._json(400, {"error": "invalid JSON"})

        if path == "/login":
            error = self._check_login(form.get("username", "").strip(), form.get("password", ""))
            if error:
                return self._send(200, login_page(error))
            _, cookie = self._start_session(form["username"].strip())
            return self._redirect("/products", cookie)
        if path == "/api/login":
            error = self._check_login(str(form.get("username", "")).strip(), str(form.get("password", "")))
            if error:
                return self._json(401, {"error": error})
            sid, cookie = self._start_session(str(form["username"]).strip())
            return self._json(200, {"user": form["username"], "sid": sid}, cookie)

        _, session = self._session()
        if not session:
            if path.startswith("/api/"):
                return self._json(401, {"error": "not logged in"})
            return self._redirect("/login")

        if path in ("/cart", "/api/cart"):
            try:
                product_id, qty = int(form.get("productId")), int(form.get("quantity", 1))
            except (TypeError, ValueError):
                product_id, qty = None, 0
            if product_id not in BY_ID or not 1 <= qty <= 5:
                if path == "/api/cart":
                    return self._json(400, {"error": "invalid productId or quantity (1-5)"})
                return self._redirect("/products")
            with LOCK:
                for line in session["cart"]:
                    if line["productId"] == product_id:
                        line["quantity"] = min(5, line["quantity"] + qty)
                        break
                else:
                    session["cart"].append({"productId": product_id, "quantity": qty})
            if path == "/api/cart":
                return self._json(201, {"cart": session["cart"]})
            return self._redirect("/cart?added=1")
        if path == "/checkout":
            order_id, _ = place_order(session)
            if not order_id:
                return self._redirect("/cart")
            return self._redirect(f"/orders/{order_id}")
        if path == "/api/checkout":
            order_id, order = place_order(session)
            if not order_id:
                return self._json(409, {"error": "cart is empty"})
            return self._json(201, order_json(order_id, session["user"], order))
        return self._json(404, {"error": "not found"})


def main():
    parser = argparse.ArgumentParser(description="K11 Practice Shop for UI automation practice")
    parser.add_argument("--host", default="127.0.0.1", help="use 0.0.0.0 to reach it from an Android emulator")
    parser.add_argument("--port", type=int, default=8091)
    parser.add_argument("--db", default=str(Path(__file__).with_name("shop.db")), help="SQLite database file")
    args = parser.parse_args()

    global DB
    DB = ShopDb(args.db)

    class Server(ThreadingHTTPServer):
        daemon_threads = True
        request_queue_size = 64

    server = Server((args.host, args.port), Handler)
    print(f"K11 Practice Shop on http://{args.host}:{args.port}  (password for any user: {PASSWORD}, database: {args.db})")
    try:
        server.serve_forever()
    except KeyboardInterrupt:
        pass


if __name__ == "__main__":
    main()
