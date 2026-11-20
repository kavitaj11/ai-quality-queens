# Data types & collections

[03 · Module topics](../../modules/03-language-deep-dives.md) · [Curriculum index](../../README.md)

**Category** · PYTHON DEEP DIVE

Start here: Python's built-in types and when to reach for each. Immutable scalars (int, float, str, bool, None); the four collections that carry test data everywhere — list (ordered, mutable), tuple (ordered, immutable), dict (key→value, the workhorse), set (unique, fast membership). Know their mutability, their big-O for lookup, and the comprehension syntax that builds them in one line.

## Worked reference

````text
# ── SCALARS (immutable) ──
n: int = 42
price: float = 19.99
name: str = "checkout"
ok: bool = True
missing = None                    # the null object

# ── LIST — ordered, mutable, allows duplicates ──
emails = ["a@t.dev", "b@t.dev"]
emails.append("c@t.dev")
emails[0]                         # index; emails[-1] last
emails[1:3]                       # slice

# ── TUPLE — ordered, IMMUTABLE (safe as dict keys) ──
point = (10, 20)
x, y = point                      # unpacking
user = ("Ada", "admin")           # fixed record

# ── DICT — key -> value, the QA workhorse ──
order = {"id": 1, "sku": "A-100", "qty": 2}
order["status"] = "pending"       # add / update
order.get("total", 0)             # safe read + default
for k, v in order.items(): ...    # iterate pairs

# ── SET — unique elements, O(1) membership ──
seen = {"a@t.dev", "b@t.dev"}
seen.add("a@t.dev")               # no-op, already present
"b@t.dev" in seen                 # fast lookup
{1, 2, 3} & {2, 3, 4}             # intersection

# ── COMPREHENSIONS — build collections in one line ──
squares   = [i * i for i in range(5)]
by_id     = {u["id"]: u for u in users}      # dict comp
domains   = {e.split("@")[1] for e in emails} # set comp

# ── collections module: the daily extras ──
from collections import Counter, defaultdict
tally = Counter(r["status"] for r in results)  # {'pass': 8,..}
groups = defaultdict(list)
for r in results: groups[r["suite"]].append(r)
````

## Exercise

Given a list of test-result dicts, use a dict comprehension to index them by test name, a set to find which tests ran, and a Counter to tally pass/fail.
