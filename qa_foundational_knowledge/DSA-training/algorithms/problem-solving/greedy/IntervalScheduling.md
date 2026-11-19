# Interval Scheduling

Choose the maximum number of non-overlapping meetings.

## Work through it

Pick the earliest finishing available meeting: exchanging the first chosen meeting for this one cannot leave less time for the rest. Trace [1,4), [1,2), [2,3): the two short meetings fit. Sorting costs O(n log n); the scan is linear. Assumes valid start-before-end intervals.

Read the [Java implementation](IntervalScheduling.java). Before stepping through it, write down the state you expect after the first operation. Compare it with the next state in the code and explain what fact remains true.
