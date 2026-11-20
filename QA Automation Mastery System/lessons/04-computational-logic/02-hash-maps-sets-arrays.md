# Hash maps, sets & arrays

[04 · Module topics](../../modules/04-computational-logic.md) · [Curriculum index](../../README.md)

**Category** · COMPLEXITY & DATA STRUCTURES

The three structures behind 80% of test utility code. Arrays keep order; hash maps give O(1) lookup by key; sets give O(1) membership plus the algebra (union, intersection, difference) that makes data validation one-liners. Frequency maps — count occurrences in one pass — solve half of all SDET screen questions.

## Worked reference

````text
from collections import Counter

# Set algebra — data validation one-liners
a = {r["id"] for r in resp_a}
b = {r["id"] for r in resp_b}
missing, extra, common = a - b, b - a, a & b

# Frequency map — one pass
errors = Counter(line.split()[2] for line in log
                 if " ERROR " in line)
print(errors.most_common(3))

# Dedupe preserving order
seen, unique = set(), []
for row in rows:
    if row["id"] not in seen:
        seen.add(row["id"]); unique.append(row)
````

## Exercise

Given two API responses, produce in one pass each: records only in A, only in B, and in both with different values.
