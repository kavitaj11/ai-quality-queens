# Sort012Alternative

## Description

Partition the array into zeros, ones, an unknown region and twos. Each step shrinks the unknown region.

## When to use it

Use when sorting three known values in one pass is useful. The relative order within a group is not preserved.

## Time complexity

Θ(n) time, O(1) auxiliary space.

## Work through it

Trace [2,1,0]; inspect the incoming item again after a swap from the right.

Read the [Java implementation](Sort012Alternative.java). Before stepping through it, write down the state you expect after the first operation. Compare it with the next state in the code and explain what fact remains true.
