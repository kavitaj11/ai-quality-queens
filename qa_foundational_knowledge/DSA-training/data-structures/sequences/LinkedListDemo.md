# Linked lists in Java

## Description

Java LinkedList is a doubly linked sequence that also implements Deque. Each node holds an element and links to its neighbours.

## When to use it

Choose it for changes at either end or repeated edits through an iterator already at the desired position.

## Advantages and trade-offs

Splicing links is cheap, but locating an arbitrary position takes time; nodes add memory overhead. Frequent inserts alone do not make it faster than ArrayList.

## Java classes and utilities

addFirst, addLast, pollFirst, descendingIterator and listIterator.

## Traversal

Use a single iterator or enhanced for: O(n). Repeated get(i) in an indexed loop can turn a traversal into O(n²).

## Search

contains and arbitrary indexed retrieval are O(n) worst case. Java can walk from the nearer end.

## Sorting

Use list.sort(comparator); allow O(n log n) comparisons. A sequential search may be cheaper than sorting for a single lookup.

## Insertion

At an end or an already-positioned iterator: O(1). add(index,value) first locates the node: O(n) worst case.

## Deletion

At an end or via a positioned iterator: O(1). remove(value) includes a linear search.

## Work through it

Predict the item removed after adding A at the front; contrast that operation with looking up a middle index.

Read the [Java implementation](LinkedListDemo.java). Before stepping through it, write down the state you expect after the first operation. Compare it with the next state in the code and explain what fact remains true.
