# Heap Sort

## Description

Build a max-heap in linear time. Swap its root with the last active slot, shrink the heap and restore heap order until the array is sorted.

## When to use it

Use when auxiliary memory is tight and a worst-case O(n log n) guarantee matters more than stability. A priority queue uses the same heap idea but need not sort the full input.

## Time complexity

Heap construction Θ(n); at most n−1 removals at O(log n) each. Average and worst Θ(n log n); O(n log n) upper bound for all inputs.

## Work through it

Trace [2,5,1]: after extracting 5, exclude that last slot when repairing the heap.



Read the [Java implementation](HeapSort.java). Before stepping through it, write down the state you expect after the first operation. Compare it with the next state in the code and explain what fact remains true.
