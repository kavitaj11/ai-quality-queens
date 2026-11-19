# ArrayList in Java

## Description

An indexed List backed by a resizable array. Duplicates and nulls are allowed.

## When to use it

Use for ordered test steps that are frequently read by index and usually appended.

## Advantages and trade-offs

Fast indexed access and compact traversal; middle edits shift later elements and growth occasionally copies the backing array.

## Java classes and utilities

get, set, add, remove, ensureCapacity and List.copyOf for a snapshot when elements are non-null.

## Traversal

Use enhanced for, an index, Iterator, ListIterator or a stream. Prefer iterator.remove when deleting during iteration.

## Search

contains and indexOf scan in O(n); binary search a sorted list in O(log n).

## Sorting

list.sort(comparator) sorts in place. Use new ArrayList<>(list) first if callers need original order.

## Insertion

Appending is amortized O(1), worst-case O(n) on growth. Inserting near the front costs O(n).

## Deletion

Removing the last element costs O(1); removing at the front costs O(n). On List<Integer>, remove(1) removes index 1; remove(Integer.valueOf(1)) removes a value.

## Work through it

Predict what get(1) returns after inserting A before B and C.

Read the [Java implementation](ArrayListDemo.java). Before stepping through it, write down the state you expect after the first operation. Compare it with the next state in the code and explain what fact remains true.
