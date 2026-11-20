# Searching & sorting

[04 · Module topics](../../modules/04-computational-logic.md) · [Curriculum index](../../README.md)

**Category** · CORE ALGORITHMS

Binary search finds anything in a sorted list in O(log n) — 14 steps for 10,000 items — and the same halving idea is how you bisect a broken build to the guilty commit. Sorting with custom keys (n log n) is the tester's preprocessing step: sort first, and dedupe, diff, and group all become single passes.

## Worked reference

````text
import bisect

# Binary search a sorted timeline: first event >= t
times = [e["ts"] for e in events]        # sorted
i = bisect.bisect_left(times, incident_start)
relevant = events[i:]

# Custom-key sorting = preprocessing
runs.sort(key=lambda r: (r["suite"], -r["duration"]))
slowest_per_suite = {}
for r in runs:                            # one O(n) pass
    slowest_per_suite.setdefault(r["suite"], r)

# git bisect is binary search over commits:
#   git bisect start HEAD v1.4.0
#   git bisect run pytest tests/smoke -x
````

## Exercise

You have 500 commits and a failing test. How many test runs does bisection need to find the breaking commit? Then implement binary search over a sorted log by timestamp.
