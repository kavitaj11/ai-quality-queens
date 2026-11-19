# Subsets

Enumerate subsets of distinct values.

## Work through it

At each position choose to skip or take it; undo the addition after the recursive call. Trace [1,2]: exactly four leaf paths exist. Copy each completed path or later mutations will change previous answers. Output takes O(n × 2^n) time and space in total.

Read the [Java implementation](Subsets.java). Before stepping through it, write down the state you expect after the first operation. Compare it with the next state in the code and explain what fact remains true.
