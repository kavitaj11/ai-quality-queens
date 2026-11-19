# Online Median Alternative

Balance a max-heap for the lower half against a min-heap for the upper half.

## Work through it

Trace [5,1,8]; use both roots when the sizes match. Shared heaps mean concurrent calls need isolation.

**Cost in this implementation:** O(n * log(n)).

Read the [Java implementation](OnlineMedianAlternative.java). Before stepping through it, write down the state you expect after the first operation. Compare it with the next state in the code and explain what fact remains true.
