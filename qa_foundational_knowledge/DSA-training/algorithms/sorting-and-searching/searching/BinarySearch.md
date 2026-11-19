# Binary Search

Find the first occurrence in a sorted array.

## Work through it

Keep a half-open interval [lo, hi); values before lo are strictly below the key. On equality, keep looking left. Trace [2,4,4,9] with key 4: the answer is index 1. An unsorted input invalidates the discard step. O(log n) time, O(1) space.

Read the [Java implementation](BinarySearch.java). Before stepping through it, write down the state you expect after the first operation. Compare it with the next state in the code and explain what fact remains true.
