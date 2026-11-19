# Kth Largest

Maintain a min-heap of the largest k items.

## Work through it

Trace k=2 on [4,1,7]; the smallest retained value is the answer, not the maximum.

**Cost in this implementation:** O(k + (n - k) * log(k)).

Read the [Java implementation](KthLargest.java). Before stepping through it, write down the state you expect after the first operation. Compare it with the next state in the code and explain what fact remains true.
