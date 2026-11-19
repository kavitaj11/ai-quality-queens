# Stream Kth Largest

Append each arrival and sort the accumulated history before reading the kth value from the end.

## Work through it

Compare this baseline with a bounded heap as history grows.

**Cost in this implementation:** O(m * (m + n) * log(n + m)).

Read the [Java implementation](StreamKthLargest.java). Before stepping through it, write down the state you expect after the first operation. Compare it with the next state in the code and explain what fact remains true.
