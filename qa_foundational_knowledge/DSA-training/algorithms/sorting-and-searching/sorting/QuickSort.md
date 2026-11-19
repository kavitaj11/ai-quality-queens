# Quick Sort

## Description

Partition around a randomly chosen pivot and recursively sort each remaining side. The pivot strategy affects how balanced the subproblems are.

## When to use it

A good in-memory array sort when stability is unnecessary and average throughput matters. Prefer a guaranteed-bound sort if quadratic behaviour is unacceptable.

## Time complexity

Best and expected Θ(n log n) for distinct keys; worst Θ(n²). Randomization reduces the likelihood of poor splits, not their cost.

## Work through it

Trace a pivot of 3 in [4,1,3,2]; the partitions need not be equal in size.



Read the [Java implementation](QuickSort.java). Before stepping through it, write down the state you expect after the first operation. Compare it with the next state in the code and explain what fact remains true.
