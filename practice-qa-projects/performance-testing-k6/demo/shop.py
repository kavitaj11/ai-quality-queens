"""Local shop API with a configurable pool for the search endpoint."""

import argparse
import json
from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer
from threading import BoundedSemaphore
from time import perf_counter, sleep
from urllib.parse import parse_qs, urlsplit


PRODUCTS = {"P100": "Laptop", "P200": "Headphones", "P300": "Keyboard"}


class ShopHandler(BaseHTTPRequestHandler):
    def send_json(self, status, payload, queue_ms=None):
        body = json.dumps(payload).encode()
        self.send_response(status)
        self.send_header("Content-Type", "application/json")
        self.send_header("Content-Length", str(len(body)))
        if queue_ms is not None:
            self.send_header("X-Queue-Wait-Ms", f"{queue_ms:.2f}")
        try:
            self.end_headers()
            self.wfile.write(body)
        except (BrokenPipeError, ConnectionResetError):
            # During overload the client may time out before the response arrives.
            self.close_connection = True

    def do_GET(self):
        url = urlsplit(self.path)
        if url.path == "/health":
            return self.send_json(200, {"status": "ok"})
        if url.path == "/api/products":
            term = parse_qs(url.query).get("q", [""])[0].lower()
            started = perf_counter()
            with self.server.search_slots:
                queue_ms = (perf_counter() - started) * 1000
                sleep(self.server.search_seconds)
                matches = [
                    {"id": product_id, "name": name}
                    for product_id, name in PRODUCTS.items()
                    if term in name.lower()
                ]
            return self.send_json(200, {"products": matches}, queue_ms)
        if url.path.startswith("/api/products/"):
            product_id = url.path.removeprefix("/api/products/")
            if product_id in PRODUCTS:
                return self.send_json(200, {"id": product_id, "name": PRODUCTS[product_id]})
        self.send_json(404, {"error": "route or product not found"})

    def log_message(self, format, *args):
        pass  # Avoid per-request logging overhead in this local experiment.


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--slots", type=int, default=2)
    parser.add_argument("--delay-ms", type=int, default=120)
    args = parser.parse_args()
    if args.slots < 1 or args.delay_ms < 0:
        parser.error("slots must be positive and delay-ms must be nonnegative")

    with ThreadingHTTPServer(("127.0.0.1", 8080), ShopHandler) as server:
        server.search_slots = BoundedSemaphore(args.slots)
        server.search_seconds = args.delay_ms / 1000
        print(f"Shop listening on http://127.0.0.1:8080 with {args.slots} search slots", flush=True)
        try:
            server.serve_forever()
        except KeyboardInterrupt:
            pass
