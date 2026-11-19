# Sets in Java

## Description

A Set represents distinct elements. Adding an element already present leaves membership unchanged.

## When to use it

Use for deduplication, visited nodes or checking whether an event ID has already appeared. Use a frequency map when counts matter.

## Advantages and trade-offs

HashSet provides fast expected membership, but no guaranteed encounter order and no record of duplicate frequency.

## Java classes and utilities

HashSet for membership, LinkedHashSet for insertion order, TreeSet for sorted keys. Use addAll, retainAll and removeAll on copies to calculate set relationships.

## Traversal

Use enhanced for or an iterator. Do not depend on HashSet iteration order in assertions.

## Search

HashSet.contains is expected O(1); TreeSet.contains is O(log n) with consistent ordering.

## Sorting

Build a TreeSet for sorted distinct values, or sort a list copy for a one-off display.

## Insertion

add returns true only when membership changes. HashSet insertion is expected amortized O(1).

## Deletion

remove returns whether the element was present. HashSet removal is expected O(1); TreeSet removal is O(log n).

## Work through it

Try inserting the same ID twice: the boolean result distinguishes a new event from one already observed.

Read the [Java implementation](SetDemo.java). Before stepping through it, write down the state you expect after the first operation. Compare it with the next state in the code and explain what fact remains true.
