# Sort012

## Description

Count the three possible values, then overwrite the input with the corresponding runs.

## When to use it

Use when keys are guaranteed to be 0, 1 or 2 and a simple two-pass solution is preferred.

## Time complexity

Θ(n) time, O(1) auxiliary space.

## Work through it

Trace [2,0,2,1]; this works because the allowed values are known in advance.

Read the [Java implementation](Sort012.java). Before stepping through it, write down the state you expect after the first operation. Compare it with the next state in the code and explain what fact remains true.
