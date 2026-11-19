# Longest Unique Substring

Find the length of the longest substring without repeated Unicode code points.

## Work through it

Track the most recent index of each symbol; the left boundary never moves backwards. Trace abba: after the second b the left boundary stays at index 2 even when a returns. Expected O(n) time, O(n) space including the code-point array.

Read the [Java implementation](LongestUniqueSubstring.java). Before stepping through it, write down the state you expect after the first operation. Compare it with the next state in the code and explain what fact remains true.
