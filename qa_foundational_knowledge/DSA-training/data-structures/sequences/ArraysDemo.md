# Arrays in Java

## Description

A fixed-length, indexed sequence with one declared component type. Primitive arrays store primitive values; reference arrays can hold compatible subtypes.

## When to use it

Use for a known number of samples, a lookup table, or random access by position.

## Advantages and trade-offs

Constant-time indexing and low structural overhead; resizing requires another array. Search by value is not constant-time just because indexing is.

## Java classes and utilities

Arrays.copyOf, Arrays.equals, Arrays.fill and System.arraycopy. Use Arrays.deepEquals for nested arrays.

## Traversal

Use an indexed loop for positions, an enhanced for loop for values, or Arrays.stream for supported element types. Each full scan costs O(n).

## Search

Linear scan: O(n). Arrays.binarySearch: O(log n) on an array sorted using the same ordering. A negative result encodes the insertion point.

## Sorting

Arrays.sort changes the input. Copy first to preserve a baseline; match the comparator when searching afterwards.

## Insertion

Allocate a larger array and copy the prefix and suffix around the new slot: O(n). If a buffer has spare capacity, append at the logical end in O(1).

## Deletion

Shift the suffix left and reduce the logical size, or copy survivors into a shorter array: O(n). Filtering by value can remove multiple matches.

## Work through it

Follow the sort before the binary search: the returned index belongs to the sorted array, not the original one.

Read the [Java implementation](ArraysDemo.java). Before stepping through it, write down the state you expect after the first operation. Compare it with the next state in the code and explain what fact remains true.
