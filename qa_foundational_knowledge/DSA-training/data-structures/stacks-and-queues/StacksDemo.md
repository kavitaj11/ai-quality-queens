# Stacks in Java

## Description

A last-in, first-out structure: the last item pushed is the first popped.

## When to use it

Use for nested actions, undo history, or iterative depth-first traversal.

## Advantages and trade-offs

Direct access to the top is inexpensive; searching deeper is linear. A stack cannot serve the oldest item first without extra work.

## Java classes and utilities

Prefer Deque<E> backed by ArrayDeque for a local stack. Legacy Stack extends Vector and supplies search.

## Traversal

For ArrayDeque used with push, iteration starts at the top. Legacy Stack iteration follows its list order, not its pop order.

## Search

contains is O(n). Legacy Stack.search returns a one-based distance from the top, or −1 if absent.

## Sorting

Sorting is not a stack operation. Transfer items through an auxiliary stack for an insertion-style O(n²) exercise; decide which end should contain the minimum.

## Insertion

push adds at the head of ArrayDeque in amortized O(1).

## Deletion

pop removes the top and throws on empty; pollFirst returns null on empty. peek inspects without removal.

## Work through it

Push start and finish; predict the first pop and compare it with a queue over the same events.

Read the [Java implementation](StacksDemo.java). Before stepping through it, write down the state you expect after the first operation. Compare it with the next state in the code and explain what fact remains true.
