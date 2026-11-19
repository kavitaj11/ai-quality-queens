# Counting Sort Alternative

## Description

Count character codes and rebuild the output in code order.

## When to use it

Use for strings restricted to the implementation’s 256-code-unit alphabet; larger Java char values need a different counter representation.

## Time complexity

Θ(n + R) with R = 256; Θ(n) for that fixed alphabet.

## Work through it

Trace the repeated letters in BABA. The 256-slot table does not cover all Java char values.

Read the [Java implementation](CountingSortAlternative.java). Before stepping through it, write down the state you expect after the first operation. Compare it with the next state in the code and explain what fact remains true.
