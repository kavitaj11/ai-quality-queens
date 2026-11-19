# Counting Sort

## Description

Count occurrences by integer key, then emit the keys in order. Offsetting by the minimum supports negative values.

## When to use it

Use for many items in a compact key range. A few values spread across a very wide range make the counter array wasteful.

## Time complexity

Θ(n + R), where R = maximum − minimum + 1; auxiliary space Θ(R).

## Work through it

Trace [-1,2,-1]; the range, not just the list length, determines memory use.



Read the [Java implementation](CountingSort.java). Before stepping through it, write down the state you expect after the first operation. Compare it with the next state in the code and explain what fact remains true.
