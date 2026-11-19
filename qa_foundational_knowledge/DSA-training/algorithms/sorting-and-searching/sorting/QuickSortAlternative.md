# Quick Sort Alternative

## Description

Split into three regions: below, equal to, and above the pivot. Recurse only on the two outer regions.

## When to use it

Choose for arrays with repeated keys: equal values can be removed from further work in one partition.

## Time complexity

All equal Θ(n); expected O(n log n) with random pivots; worst Θ(n²).

## Work through it

Trace [2,1,2,3,2]; only the less-than and greater-than regions need recursion.

Read the [Java implementation](QuickSortAlternative.java). Before stepping through it, write down the state you expect after the first operation. Compare it with the next state in the code and explain what fact remains true.
