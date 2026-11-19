# Merge Sort

## Description

Split into halves, sort each half, then merge the ordered runs. Taking from the left on ties preserves stability.

## When to use it

Use when a predictable upper bound matters or when merging sequentially accessible runs, such as external files. The array version needs temporary storage.

## Time complexity

Best, average and worst Θ(n log n) for the supplied split-and-merge implementation.

## Work through it

Merge [2,5] and [1,4] by hand; notice why a temporary buffer helps.



Read the [Java implementation](MergeSort.java). Before stepping through it, write down the state you expect after the first operation. Compare it with the next state in the code and explain what fact remains true.
