# Linear Search

Scan until the first match, even if the array is unsorted.

## Work through it

Each rejected element extends a prefix known not to contain the key. Trace [7,1,7] with key 1; then try an absent key. Worst case O(n) time and O(1) space.

Read the [Java implementation](LinearSearch.java). Before stepping through it, write down the state you expect after the first operation. Compare it with the next state in the code and explain what fact remains true.
