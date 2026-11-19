# Radix Sort

## Description

Process nonnegative integers one decimal digit at a time, from least to most significant. Stable bucket passes preserve earlier digit ordering.

## When to use it

Use for bounded-width integer identifiers when digit extraction is cheap. This implementation does not handle signed keys.

## Time complexity

Θ(d(n + b)) for d digits and b buckets. The workbook’s O(n) shorthand assumes fixed width and fixed base.

## Work through it

Trace [21,12,11]; preserving bucket order keeps earlier digit decisions intact.



Read the [Java implementation](RadixSort.java). Before stepping through it, write down the state you expect after the first operation. Compare it with the next state in the code and explain what fact remains true.
