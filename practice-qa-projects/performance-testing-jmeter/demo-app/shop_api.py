"""A tiny shop API to practise performance testing against.

Standard library only. It has two deliberate bottlenecks:
  1. every request that touches the "database" must borrow a connection from a small pool;
     under load, requests queue for a connection, throughput stops growing and response
     times climb;
  2. a small TCP listen backlog, so a burst of new connections can be refused outright.

Run:
    python shop_api.py                                # pool of 4, backlog of 5 (the bottlenecks)
    python shop_api.py --pool-size 16 --backlog 128   # the "fix"

Endpoints (JSON):
    POST /api/login                {"username": "...", "password": "..."} -> {"token": "..."}
    GET  /api/products?q=<text>    -> {"items": [{"id": ..., "name": ..., "price": ...}]}
    GET  /api/products/<id>        -> product details
    POST /api/cart                 {"productId": ..., "quantity": ...} -> {"cartId": "..."}
    POST /api/orders               {"cartId": "..."} -> {"orderId": "...", "status": "CONFIRMED"}
    GET  /health                   -> {"status": "UP"}
    GET  /metrics                  -> pool usage and wait times (server-side view)
    GET  /metrics?reset=true       -> same, then resets the counters (start of a new test step)

Every /api call except login needs the header  Authorization: Bearer <token>.
Never expose this server outside your own machine; it is a teaching tool.
"""
import argparse
import json
import random
import secrets
import threading
import time
from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer
from urllib.parse import parse_qs, urlparse

PRODUCTS = [
    {"id": i, "name": f"{adj} {noun}", "price": round(random.Random(i).uniform(3, 120), 2)}
    for i, (adj, noun) in enumerate(
        ((a, n) for a in ("Blue", "Green", "Red", "Smart", "Classic", "Travel", "Eco", "Pro")
         for n in ("Mug", "Lamp", "Backpack", "Notebook", "Bottle", "Headphones", "Chair", "Desk")),
        start=1001)
]
PRODUCT_BY_ID = {p["id"]: p for p in PRODUCTS}

# Query cost in seconds while holding a DB connection.
QUERY_COST = {"login": 0.015, "search": 0.040, "product": 0.010, "cart": 0.020, "order": 0.080}


class Metrics:
    def __init__(self):
        self.lock = threading.Lock()
        self.requests = 0
        self.errors = 0
        self.active_connections = 0
        self.waiting = 0
        self.max_waiting = 0
        self.total_wait = 0.0

    def reset(self):
        with self.lock:
            self.requests = self.errors = self.max_waiting = 0
            self.total_wait = 0.0

    def snapshot(self, pool_size):
        with self.lock:
            avg_wait_ms = (self.total_wait / self.requests * 1000) if self.requests else 0.0
            return {
                "pool_size": pool_size,
                "active_connections": self.active_connections,
                "waiting_for_connection": self.waiting,
                "max_waiting_seen": self.max_waiting,
                "requests": self.requests,
                "errors": self.errors,
                "avg_pool_wait_ms": round(avg_wait_ms, 2),
            }


class ShopState:
    def __init__(self, pool_size):
        self.pool_size = pool_size
        self.pool = threading.BoundedSemaphore(pool_size)
        self.metrics = Metrics()
        self.tokens = {}
        self.carts = {}
        self.lock = threading.Lock()

    def query(self, kind):
        """Borrow a DB connection, do the 'query', give it back. Waiting is the bottleneck."""
        m = self.metrics
        with m.lock:
            m.waiting += 1
            m.max_waiting = max(m.max_waiting, m.waiting)
        start = time.perf_counter()
        with self.pool:
            waited = time.perf_counter() - start
            with m.lock:
                m.waiting -= 1
                m.active_connections += 1
                m.total_wait += waited
            try:
                time.sleep(QUERY_COST[kind] * random.uniform(0.8, 1.2))
            finally:
                with m.lock:
                    m.active_connections -= 1


def make_handler(state: ShopState):
    class Handler(BaseHTTPRequestHandler):
        server_version = "ShopAPI/1.0"

        def log_message(self, fmt, *args):  # keep the console quiet under load
            pass

        def _send(self, status, body):
            data = json.dumps(body).encode()
            self.send_response(status)
            self.send_header("Content-Type", "application/json")
            self.send_header("Content-Length", str(len(data)))
            self.end_headers()
            self.wfile.write(data)
            with state.metrics.lock:
                state.metrics.requests += 1
                if status >= 500:
                    state.metrics.errors += 1

        def _json_body(self):
            length = int(self.headers.get("Content-Length") or 0)
            if not length:
                return {}
            try:
                return json.loads(self.rfile.read(length))
            except json.JSONDecodeError:
                return None

        def _user(self):
            auth = self.headers.get("Authorization", "")
            token = auth[7:] if auth.startswith("Bearer ") else ""
            return state.tokens.get(token)

        def do_GET(self):
            url = urlparse(self.path)
            if url.path == "/health":
                return self._send(200, {"status": "UP"})
            if url.path == "/metrics":
                snapshot = state.metrics.snapshot(state.pool_size)
                if parse_qs(url.query).get("reset", [""])[0] == "true":
                    state.metrics.reset()
                return self._send(200, snapshot)
            if not self._user():
                return self._send(401, {"error": "missing or invalid token"})
            if url.path == "/api/products":
                q = parse_qs(url.query).get("q", [""])[0].lower()
                state.query("search")
                items = [p for p in PRODUCTS if q in p["name"].lower()][:10]
                return self._send(200, {"items": items, "count": len(items)})
            if url.path.startswith("/api/products/"):
                try:
                    product = PRODUCT_BY_ID[int(url.path.rsplit("/", 1)[1])]
                except (ValueError, KeyError):
                    return self._send(404, {"error": "product not found"})
                state.query("product")
                return self._send(200, {**product, "stock": 50})
            return self._send(404, {"error": "not found"})

        def do_POST(self):
            path = urlparse(self.path).path
            body = self._json_body()
            if body is None:
                return self._send(400, {"error": "invalid JSON"})
            if path == "/api/login":
                if not body.get("username") or body.get("password") != "Passw0rd!":
                    return self._send(401, {"error": "invalid credentials"})
                state.query("login")
                token = secrets.token_hex(16)
                state.tokens[token] = body["username"]
                return self._send(200, {"token": token, "expiresIn": 3600})
            user = self._user()
            if not user:
                return self._send(401, {"error": "missing or invalid token"})
            if path == "/api/cart":
                product_id, qty = body.get("productId"), body.get("quantity", 1)
                if product_id not in PRODUCT_BY_ID or not isinstance(qty, int) or qty < 1:
                    return self._send(400, {"error": "invalid productId or quantity"})
                state.query("cart")
                cart_id = "CART-" + secrets.token_hex(6)
                with state.lock:
                    state.carts[cart_id] = {"user": user, "productId": product_id, "quantity": qty}
                return self._send(201, {"cartId": cart_id})
            if path == "/api/orders":
                with state.lock:
                    cart = state.carts.pop(body.get("cartId", ""), None)
                if not cart or cart["user"] != user:
                    return self._send(400, {"error": "unknown cartId"})
                state.query("order")
                total = round(PRODUCT_BY_ID[cart["productId"]]["price"] * cart["quantity"], 2)
                return self._send(201, {"orderId": "ORD-" + secrets.token_hex(6),
                                        "status": "CONFIRMED", "total": total})
            return self._send(404, {"error": "not found"})

    return Handler


def main():
    parser = argparse.ArgumentParser(description="Demo shop API for performance testing practice")
    parser.add_argument("--port", type=int, default=8099)
    parser.add_argument("--pool-size", type=int, default=4, help="DB connection pool size")
    parser.add_argument("--backlog", type=int, default=5,
                        help="TCP listen backlog: connections allowed to wait to be accepted (Python's default is 5)")
    args = parser.parse_args()
    state = ShopState(args.pool_size)

    class Server(ThreadingHTTPServer):
        request_queue_size = args.backlog
        daemon_threads = True

    server = Server(("127.0.0.1", args.port), make_handler(state))
    print(f"Shop API on http://127.0.0.1:{args.port}  (DB pool size = {args.pool_size}, backlog = {args.backlog})")
    try:
        server.serve_forever()
    except KeyboardInterrupt:
        pass


if __name__ == "__main__":
    main()
