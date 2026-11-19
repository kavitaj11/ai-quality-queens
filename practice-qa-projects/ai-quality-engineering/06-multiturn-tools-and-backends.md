# 6. Multi-turn tools and backend contracts

A successful conversation must preserve user intent and produce the right state transition. In this lab, “Book Tuesday” creates a pending request. “Yes” confirms it. “Actually Thursday” changes the pending slot. Repeated confirmation must not create another booking.

## Inspect the transition contract

| User turn | Expected pending state | Expected stored bookings |
|---|---|---|
| Book Tuesday | Tuesday | None |
| Actually Thursday | Thursday | None |
| Yes | Thursday, completed | One Thursday booking |
| Yes again | Completed | Still one booking |

`expected_bookings_after` supplies an independent oracle after each turn. The runner reads the HTTP service's actual in-memory state, rather than trusting the assistant's `Booked` message. Every rollout creates a fresh backend, while turns within a rollout share state.

## Service checks

`POST /bookings` validates the account, consent flag, allowed slot and idempotency key. Repeating the same key with the same payload returns the original booking; reusing it with a different payload returns 409. Invalid slots return 400, and absent consent or a different account returns 403. Direct HTTP tests verify these boundaries even when the assistant's own guard is bypassed.

The demo's `demo-user` field and boolean consent flag are test fixtures, **not authentication**. A real connector must derive identity from verified credentials and bind user approval to the exact action and arguments. A model-supplied `consent: true` is not proof of user authorization.

```bash
python -m aiqe inspect --case change-pending-slot
python -m aiqe run --fault memory --output artifacts/lost-memory
python -m aiqe run --fault duplicate --output artifacts/duplicate
```

The latter two runs must fail. Inspect the differing backend state rather than only the response wording.

## Failure and retry policy

Golden cases inject a 503 and a malformed successful response. The assistant must not announce a confirmed appointment when either occurs. Separate tests cover client timeouts and returned account/slot mismatches. There is no automatic retry in this lab.

For production, a timeout can occur after the server commits an action. Reconcile by idempotency key or transaction ID before retrying. Test lost acknowledgments, duplicate delivery, eventual consistency, concurrency and cancellation races. The small single-session lab does not implement those distributed guarantees.

**How would you evaluate agent task completion?** Define observable postconditions and forbidden side effects, check them throughout the conversation, and separate final success from a valid sequence of authorized actions.

**How do you test backend API changes?** Run schema and business-contract checks, negative responses and fault injection, then replay agent workflows to detect planning or interpretation changes caused by the new API.

Exercise: implement a simulated commit-then-timeout response and a reconciliation endpoint. The acceptance criterion is exactly one booking after a safe retry, with evidence showing why the retry was authorized.

Next: [Traces and triage](07-traces-and-triage.md).

