# Online Median

Insert each arrival into the already-sorted prefix and read its middle.

## Work through it

Trace [5,1,8]; this method changes the input list and reports integer medians.

**Cost in this implementation:** O(n^2).

Read the [Java implementation](OnlineMedian.java). Before stepping through it, write down the state you expect after the first operation. Compare it with the next state in the code and explain what fact remains true.
