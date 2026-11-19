# Cycle Sort

## Description

Count how many keys are smaller to determine an item’s final position, then rotate displaced items through that cycle. Skip equal keys before writing.

## When to use it

Use on small inputs when reducing writes matters more than comparison speed. It is a comparison sort despite its placement in the workbook.

## Time complexity

Θ(n²) comparisons in best, average and worst cases for the supplied rank-scanning implementation; O(1) auxiliary space.

## Work through it

Trace [3,1,2] and count writes. Why must duplicates be skipped?



Read the [Java implementation](CycleSort.java). Before stepping through it, write down the state you expect after the first operation. Compare it with the next state in the code and explain what fact remains true.
