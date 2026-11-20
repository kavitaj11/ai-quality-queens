# Recursion & memoization

[04 · Module topics](../../modules/04-computational-logic.md) · [Curriculum index](../../README.md)

**Category** · CORE ALGORITHMS

Recursion is how you process anything self-similar: nested JSON, DOM subtrees, directory trees, deep-equality of two payloads. Every recursive function needs a base case and progress toward it. Memoization — cache results keyed by input — turns repeated expensive work (fixture builds, 2ⁿ branching) into O(1) reuse.

## Worked reference

````text
from functools import lru_cache

# Deep diff — recursion over self-similar JSON
def deep_diff(a, b, path="$"):
    if a == b: return []
    if isinstance(a, dict) and isinstance(b, dict):
        out = []
        for k in a.keys() | b.keys():
            out += deep_diff(a.get(k), b.get(k), f"{path}.{k}")
        return out
    if isinstance(a, list) and isinstance(b, list):
        return [d for i, (x, y) in enumerate(zip(a, b))
                for d in deep_diff(x, y, f"{path}[{i}]")]
    return [(path, a, b)]

for path, got, want in deep_diff(actual, expected):
    print(f"MISMATCH {path}: {got!r} != {want!r}")

@lru_cache(maxsize=None)          # memoized fixture
def seeded_user(role): return api.create_user(role=role)
````

## Exercise

Write deep_diff(a, b) that returns the JSON path of every difference between two nested payloads. Then memoize an expensive fixture factory and measure the saving.
