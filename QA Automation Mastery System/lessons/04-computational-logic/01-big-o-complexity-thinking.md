# Big-O & complexity thinking

[04 · Module topics](../../modules/04-computational-logic.md) · [Curriculum index](../../README.md)

**Category** · COMPLEXITY & DATA STRUCTURES

How runtime and memory grow with input size — the difference between a suite that scales and one that times out. O(1) hash lookups, O(log n) binary search, O(n) scans, O(n²) nested loops, O(2ⁿ) brute force. Testers hit this constantly: comparing datasets, mining reports, generating test data. The skill is spotting the n² before it ships.

## Worked reference

````text
ids_a = list(range(100_000))
ids_b = list(range(50_000, 150_000))

# O(n²) — list membership inside a loop: ~10¹⁰ ops
# missing = [x for x in ids_a if x not in ids_b]   # DON'T

# O(n) — build a set once, O(1) lookups after
b = set(ids_b)
missing = [x for x in ids_a if x not in b]

# Same shape for response comparison:
by_id = {row["id"]: row for row in resp_a}   # index O(n)
for row in resp_b:                            # scan  O(n)
    assert by_id[row["id"]]["total"] == row["total"]
````

## Exercise

Your test compares two 10,000-row API responses with nested loops. Estimate the operations, then redesign it as O(n) and measure the difference.
