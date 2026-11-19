# Sort Characters

## Description

Count occurrences of each ASCII character, then append each as many times as it occurred.

## When to use it

Use for ASCII-only data with many repeated characters. Do not apply the 128-entry table to unrestricted Unicode input.

## Time complexity

Θ(n + 128), hence Θ(n) for this fixed alphabet.

## Work through it

Trace [b,a,b]; input outside the 128-slot alphabet needs another representation.



Read the [Java implementation](SortCharacters.java). Before stepping through it, write down the state you expect after the first operation. Compare it with the next state in the code and explain what fact remains true.
