"""Loopback HTTP service with consent, authorization and idempotency boundaries."""
from contextlib import contextmanager
from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer
import json
import threading
from urllib.error import HTTPError
from urllib.request import Request, urlopen


class ConnectorError(RuntimeError):
    pass


class Backend:
    def __init__(self, trace, fault="none"):
        self.trace, self.fault = trace, fault
        self.bookings, self.keys, self.calls = [], {}, []
        self.lock = threading.Lock()

    def handle(self, body, parent):
        with self.trace.span("connector.book", parent, service="booking-api") as span:
            with self.lock:
                self.calls.append(body.copy())
                if self.fault == "unavailable":
                    span["status"] = "error"
                    return 503, {"error": "temporarily_unavailable"}
                if self.fault == "malformed":
                    return 200, {"unexpected": "schema_changed"}
                if body.get("account") != "demo-user":
                    return 403, {"error": "forbidden"}
                if body.get("consent") is not True:
                    return 403, {"error": "consent_required"}
                if body.get("slot") not in ("tuesday", "thursday"):
                    return 400, {"error": "invalid_slot"}
                key = body.get("idempotency_key")
                if not isinstance(key, str) or not key:
                    return 400, {"error": "idempotency_key_required"}
                payload = (body["account"], body["slot"])
                if key in self.keys:
                    old_payload, booking = self.keys[key]
                    return (200, booking) if payload == old_payload else (409, {"error": "key_conflict"})
                booking = {"booking_id": f"BK-{len(self.bookings)+1}", "slot": body["slot"],
                           "account": body["account"]}
                self.keys[key] = (payload, booking)
                self.bookings.append(booking)
                return 201, booking


@contextmanager
def serve(backend):
    class Handler(BaseHTTPRequestHandler):
        def log_message(self, *args):
            pass

        def do_POST(self):
            if self.path != "/bookings":
                self.send_error(404)
                return
            try:
                length = int(self.headers.get("Content-Length", "0"))
                if not 0 < length <= 16384:
                    raise ValueError("invalid_length")
                body = json.loads(self.rfile.read(length))
                if not isinstance(body, dict):
                    raise ValueError("object_required")
                traceparent = self.headers.get("traceparent", "").split("-")
                parent = traceparent[2] if len(traceparent) == 4 else None
                status, payload = backend.handle(body, parent)
            except (ValueError, TypeError):
                status, payload = 400, {"error": "invalid_request"}
            content = json.dumps(payload).encode()
            self.send_response(status)
            self.send_header("Content-Type", "application/json")
            self.send_header("Content-Length", str(len(content)))
            self.end_headers()
            self.wfile.write(content)

    server = ThreadingHTTPServer(("127.0.0.1", 0), Handler)
    thread = threading.Thread(target=server.serve_forever, kwargs={"poll_interval": .01}, daemon=True)
    thread.start()
    try:
        yield f"http://127.0.0.1:{server.server_port}"
    finally:
        server.shutdown()
        server.server_close()
        thread.join(timeout=2)


def book(url, payload, trace, parent, timeout=2):
    with trace.span("tool.book", parent, tool="book_appointment") as span:
        request = Request(url + "/bookings", data=json.dumps(payload).encode(), method="POST",
                          headers={"Content-Type": "application/json", "traceparent":
                                   f"00-{trace.trace_id}-{span['span_id']}-01"})
        try:
            with urlopen(request, timeout=timeout) as response:
                result = json.loads(response.read(16385))
            if not isinstance(result, dict) or set(result) != {"booking_id", "slot", "account"}:
                raise ConnectorError("response_schema")
            if not all(isinstance(v, str) and v for v in result.values()):
                raise ConnectorError("response_types")
            if result["slot"] != payload["slot"] or result["account"] != payload["account"]:
                raise ConnectorError("response_mismatch")
            return result
        except HTTPError as exc:
            raise ConnectorError(f"http_{exc.code}") from None
        except (ValueError, OSError) as exc:
            raise ConnectorError(type(exc).__name__) from None

