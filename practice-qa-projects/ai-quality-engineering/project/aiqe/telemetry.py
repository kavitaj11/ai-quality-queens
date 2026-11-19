"""Small JSON trace recorder; deliberately not an OTLP exporter."""
from contextlib import contextmanager
import hashlib
import re
import time
import uuid

CANARY = "SYNTHETIC-SECRET-7429"
EMAIL = re.compile(r"[\w.+-]+@[\w.-]+\.[A-Za-z]{2,}")


def redact(value):
    if isinstance(value, dict):
        return {key: redact(item) for key, item in value.items()}
    if isinstance(value, list):
        return [redact(item) for item in value]
    if isinstance(value, str):
        return EMAIL.sub("[REDACTED_EMAIL]", value.replace(CANARY, "[REDACTED_CANARY]"))
    return value


def digest(path):
    return hashlib.sha256(path.read_bytes()).hexdigest()


class Trace:
    def __init__(self):
        self.trace_id = uuid.uuid4().hex
        self.spans = []

    @contextmanager
    def span(self, name, parent=None, **attributes):
        span = {"trace_id": self.trace_id, "span_id": uuid.uuid4().hex[:16],
                "parent_span_id": parent, "name": name, "status": "ok",
                "attributes": redact(attributes)}
        start = time.perf_counter()
        try:
            yield span
        except Exception as exc:
            span["status"] = "error"
            span["attributes"]["error.type"] = type(exc).__name__
            raise
        finally:
            span["duration_ms"] = round((time.perf_counter() - start) * 1000, 3)
            self.spans.append(span)

