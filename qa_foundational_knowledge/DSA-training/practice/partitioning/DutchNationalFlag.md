# Dutch National Flag

## Description

Move each color into its region by swapping at moving boundaries.

## When to use it

Use for grouping exactly three categories in place; category order must be defined before partitioning.

## Time complexity

Θ(n) time, O(1) auxiliary space.

## Work through it

Start with one ball of each color in reverse order and trace which boundary moves on each swap.



Read the [Java implementation](DutchNationalFlag.java). Before stepping through it, write down the state you expect after the first operation. Compare it with the next state in the code and explain what fact remains true.
