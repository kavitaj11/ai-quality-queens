# Heaps in Java

## Description

A binary min-heap keeps every parent no greater than its children. Only the root is guaranteed to be globally smallest. A max-heap reverses that relationship.

## When to use it

Use for repeatedly taking the next highest-priority job or keeping the largest k values with a bounded min-heap.

## Advantages and trade-offs

Fast root access and logarithmic repair; arbitrary membership and sorted traversal are not efficient. Equal-priority items need an explicit tie-breaker if order matters.

## Java classes and utilities

PriorityQueue defaults to natural order; Comparator.reverseOrder() gives a max-priority ordering. Use peek, offer and poll.

## Traversal

An iterator visits heap storage order. To visit by priority without mutating the original, copy the queue and poll the copy.

## Search

peek reads the extreme in O(1). contains(value) scans in O(n); the heap invariant does not pick a single search branch.

## Sorting

Heap sort builds a heap in O(n) and repeatedly extracts in O(log n), giving O(n log n) time. A sorted array has a stronger invariant than a heap.

## Insertion

Append at the end and sift up: O(log n) heap repair. Array growth can add copying overhead.

## Deletion

Remove the root, move the last item to the root, then sift down: O(log n). Removing an arbitrary value includes an O(n) search.

## Work through it

Predict successive removals of 7,1,3; iterating the heap itself does not guarantee sorted order.

Read the [Java implementation](HeapDemo.java). Before stepping through it, write down the state you expect after the first operation. Compare it with the next state in the code and explain what fact remains true.
