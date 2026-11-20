# String & two-pointer patterns

[04 · Module topics](../../modules/04-computational-logic.md) · [Curriculum index](../../README.md)

**Category** · CORE ALGORITHMS

Most SDET coding screens are string work: frequency maps for anagrams/duplicates, two pointers for pair-finding in sorted data, sliding windows for 'best subarray/substring' questions, and honest parsing for logs. Learn the four patterns, not a hundred problems.

## Worked reference

````text
from collections import Counter

# 1. Frequency map: first non-repeating char
def first_unique(s):
    freq = Counter(s)
    return next((c for c in s if freq[c] == 1), None)

# 2. Two pointers: pair summing to target (sorted)
def pair_sum(nums, target):
    lo, hi = 0, len(nums) - 1
    while lo < hi:
        s = nums[lo] + nums[hi]
        if s == target: return nums[lo], nums[hi]
        lo, hi = (lo + 1, hi) if s < target else (lo, hi - 1)

# 3. Sliding window: longest all-green streak
def longest_green(runs):
    best = start = 0
    for i, r in enumerate(runs):
        if r != "pass": start = i + 1
        best = max(best, i - start + 1)
    return best
````

## Exercise

Solve with the right pattern: first non-repeating char (freq map), longest error-free window in a run log (sliding window), two test durations summing to the budget (two pointers).
