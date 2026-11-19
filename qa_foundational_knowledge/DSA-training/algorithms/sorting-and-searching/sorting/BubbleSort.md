# Bubble Sort

## Description

Compare adjacent entries and swap out-of-order neighbours. Each pass fixes the largest remaining item at the end; a pass without a swap stops the algorithm.

## When to use it

Use it to teach loop invariants or visualize swaps on a tiny list. Its quadratic growth makes it a poor default for large inputs.

## Time complexity

Best Θ(n) with early stopping; average and worst Θ(n²).

## Work through it

Trace [3,1,2]: one pass produces [1,2,3]. What tells the next pass to stop early?



Read the [Java implementation](BubbleSort.java). Before stepping through it, write down the state you expect after the first operation. Compare it with the next state in the code and explain what fact remains true.

## Why one pass is enough to fix one position

Start with [4, 2, 3, 1]. Compare adjacent values from left to right. Swapping 4 and 2 gives [2, 4, 3, 1]; swapping 4 and 3 gives [2, 3, 4, 1]; the last swap gives [2, 3, 1, 4]. The prefix is still unsorted, but 4 is now final. That is why the next pass can stop one position earlier.

In this implementation the boolean starts true and becomes false after a swap: read it as “still unchanged.” A complete unchanged pass proves every adjacent pair is in order. Already sorted input therefore takes linear time. Equal items never cross because the comparison is strict, so the sort is stable. Try two equal keys with different labels and track their relative order.
