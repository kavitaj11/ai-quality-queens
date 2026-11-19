# Hybrid Quick Sort

## Description

Use quicksort on large partitions and insertion sort below a fixed cutoff. Small runs avoid the overhead of further recursive partitioning.

## When to use it

Use for in-memory arrays with many small subproblems. Benchmark the cutoff on representative data rather than treating it as a universal constant.

## Time complexity

Expected O(n log n) for randomized quicksort on distinct keys; worst O(n²). A fixed insertion-sort cutoff does not remove the quadratic worst case.

## Work through it

Compare arrays just below and above the cutoff; the result should agree despite the change of technique.

Read the [Java implementation](HybridQuickSort.java). Before stepping through it, write down the state you expect after the first operation. Compare it with the next state in the code and explain what fact remains true.
