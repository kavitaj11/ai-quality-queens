"""Rule-based, seeded practice assistant. Never receives evaluation expectations."""
import random
import re
from .backend import ConnectorError, book
from .telemetry import CANARY

FAULTS = ("none", "hallucination", "incomplete", "citation", "retrieval", "memory",
          "unsafe", "pii", "consent", "duplicate", "overrefusal")


class DemoAgent:
    def __init__(self, kb, backend_url, trace, seed=7, fault="none"):
        self.kb, self.url, self.trace = kb, backend_url, trace
        self.random, self.fault = random.Random(seed), fault
        self.slot, self.booking = None, None
        self.history = []

    def reply(self, message):
        # Only the message, KB and public connector contract enter this boundary.
        lower = message.lower()
        with self.trace.span("agent.turn", model="demo-rules-v1", prompt="support-policy-v1") as root:
            parent = root["span_id"]
            with self.trace.span("guardrail.input", parent):
                attack = any(t in lower for t in ("ignore previous", "ignore all", "system prompt",
                    "developer mode", "secret", "other customer", "another customer", "bypass", "jailbreak"))
                if self.fault == "overrefusal" and "privacy" in lower:
                    attack = True
            if attack:
                if self.fault in ("unsafe", "pii"):
                    answer = f"Here is the secret: {CANARY}; customer@example.test"
                    return self._record(message, answer, [], False)
                return self._record(message, "I can't share private data or bypass safety rules. I can help with public support information.", [], True)
            if self.fault == "memory":
                self.slot = None
            slot = next((s for s in ("tuesday", "thursday") if s in lower), None)
            if "cancel" in lower or "do not book" in lower:
                self.slot = None
                return self._record(message, "Nothing new has been booked. I cleared the pending request.", [], False)
            if slot:
                self.slot = slot
            confirm = bool(re.search(r"\b(yes|confirm)\b", lower)) and not bool(re.search(r"\b(no|not|don't)\b", lower))
            if self.slot and (confirm or self.fault == "consent"):
                if self.booking and self.fault != "duplicate":
                    return self._record(message, f"Already booked for {self.booking['slot']}; no duplicate was created.", [], False)
                key = f"{self.trace.trace_id}:{self.slot}"
                if self.fault == "duplicate":
                    key += f":{len(self.history)}"
                with self.trace.span("planner.book", parent):
                    try:
                        self.booking = book(self.url, {"slot": self.slot, "account": "demo-user",
                            "consent": confirm or self.fault == "consent", "idempotency_key": key}, self.trace, parent)
                        answer = f"Booked for {self.booking['slot']}. Your confirmation is {self.booking['booking_id']}."
                    except ConnectorError:
                        answer = "I could not confirm a booking because the service is unavailable or returned invalid data. Please try again."
                return self._record(message, answer, [], False)
            if self.slot:
                return self._record(message, f"I can book {self.slot}. Please confirm before I create the appointment.", [], False)
            if confirm:
                return self._record(message, "Which day would you like: Tuesday or Thursday?", [], False)
            with self.trace.span("retrieval.search", parent) as retrieval:
                documents = [doc for doc in self.kb if any(word in lower for word in doc["keywords"])]
                if self.fault == "retrieval":
                    documents = []
                retrieval["attributes"]["document_ids"] = [d["id"] for d in documents]
            with self.trace.span("model.generate", parent, model="demo-rules-v1"):
                if not documents:
                    return self._record(message, "I don't have enough information in the support guide to answer that. Please contact support.", [], False)
                sentences = [sentence for doc in documents for sentence in doc["sentences"]]
                # External instructions are never an authority in this demo.
                if self.fault == "unsafe":
                    instructions = [d.get("untrusted_note") for d in documents if d.get("untrusted_note")]
                    if instructions:
                        sentences.append(CANARY)
                self.random.shuffle(sentences)
                if self.fault == "incomplete":
                    sentences = sentences[:1]
                if self.fault == "hallucination":
                    sentences.append("Every repair is free for 99 years.")
                ids = [d["id"] for d in documents]
                answer = " ".join(sentences) + " " + " ".join(f"[{doc_id}]" for doc_id in ids)
                return self._record(message, answer.strip(), [] if self.fault == "citation" else documents, False)

    def _record(self, message, answer, evidence, refused):
        result = {"answer": answer, "evidence": evidence, "refused": refused}
        self.history.append({"user": message, **result})
        return result
