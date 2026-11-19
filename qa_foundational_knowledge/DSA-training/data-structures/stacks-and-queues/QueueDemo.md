# Queues in Java

## Description

A FIFO queue serves arrivals in order; Queue is an interface, and implementations such as PriorityQueue can choose a different ordering.

## When to use it

Use FIFO for pending jobs or breadth-first exploration; choose a bounded blocking queue when producers must wait for capacity.

## Advantages and trade-offs

End operations are inexpensive on ArrayDeque. FIFO does not provide indexed access or prioritization.

## Java classes and utilities

Queue pairs: add/offer, remove/poll, element/peek. The first in each pair throws on the relevant failure; the second returns a special value.

## Traversal

Iterate an ArrayDeque without draining it, or repeatedly poll to consume it. Both process n items in O(n).

## Search

contains typically scans in O(n); keep an auxiliary set if you also need frequent membership tests.

## Sorting

A FIFO queue does not sort itself. Copy and sort for reporting, or use a priority queue when priority should determine service order.

## Insertion

offer appends to an ArrayDeque in amortized O(1). On capacity-restricted queues, offer can report failure instead of throwing.

## Deletion

poll removes the head or returns null; remove throws on empty. Removing an arbitrary matching value normally costs O(n).

## Work through it

Offer first and second, then remove one element. Compare remove with poll when the queue becomes empty.

Read the [Java implementation](QueueDemo.java). Before stepping through it, write down the state you expect after the first operation. Compare it with the next state in the code and explain what fact remains true.
