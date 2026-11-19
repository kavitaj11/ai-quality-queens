# Selection Sort

## Description

Scan the unsorted suffix for its minimum and swap it into the next output slot. The conventional swap is not stable.

## When to use it

Use on small inputs when writes are expensive: there are at most n−1 swaps, even though comparisons remain quadratic.

## Time complexity

Best, average and worst Θ(n²) comparisons regardless of starting order.

## Work through it

Trace [4,1,3]; count comparisons separately from writes.



Read the [Java implementation](SelectionSort.java). Before stepping through it, write down the state you expect after the first operation. Compare it with the next state in the code and explain what fact remains true.
