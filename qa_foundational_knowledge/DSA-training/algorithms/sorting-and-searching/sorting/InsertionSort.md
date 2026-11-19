# Insertion Sort

## Description

Insert each new item into the sorted prefix by moving it past larger neighbours. Equal keys retain their order.

## When to use it

Choose it for small or nearly sorted lists, or as the small-partition stage of a hybrid sort. Work follows the number of inversions.

## Time complexity

Best Θ(n); average and worst Θ(n²). More precisely Θ(n + I) for I inversions.

## Work through it

Trace [1,4,2]: only 4 needs to move. Compare the work on sorted and reversed inputs.



Read the [Java implementation](InsertionSort.java). Before stepping through it, write down the state you expect after the first operation. Compare it with the next state in the code and explain what fact remains true.

## What the inner loop proves

Consider [2, 5, 7, 3]. Before inserting 3, the prefix [2, 5, 7] is already sorted. Compare 3 with its left neighbour: swap past 7, then past 5, then stop at 2. The prefix has grown by one and remains sorted. Unlike selection sort, there is no need to search the rest of the input for a minimum.

The number of adjacent swaps equals the number of inverted pairs. A nearly sorted array needs few swaps, while a reverse-sorted array of n distinct keys needs n(n−1)/2. Strict comparison lets equal keys keep their original order. Trace [3a, 2, 3b] to verify this yourself.
