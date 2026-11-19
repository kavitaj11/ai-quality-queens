# Hash tables in Java

## Description

A hash function maps a key to a bucket; equality distinguishes keys that share a bucket. Java Hashtable is a synchronized map and rejects null keys and values.

## When to use it

Use key-based lookup when order is irrelevant. Choose Hashtable for compatible legacy code; consider HashMap or ConcurrentHashMap for new designs.

## Advantages and trade-offs

Expected constant-time key operations depend on good hashing. Collisions, resizing and contention can increase cost; synchronization of separate calls does not make a compound action atomic.

## Java classes and utilities

get, put, containsKey, remove, clear and entrySet. Implement compatible equals and hashCode for custom keys.

## Traversal

Iterate entrySet for key/value pairs; iteration order is unspecified. Legacy Enumeration also exists.

## Search

get and containsKey are expected O(1); searching for a value scans entries.

## Sorting

Sort a copy of the entries with a comparator, or use a TreeMap for continuously ordered keys. Sorting a view does not reorder Hashtable.

## Insertion

put adds a mapping or replaces its value. Normally expected O(1); rehashing can take O(n).

## Deletion

remove(key) finds the bucket and removes the matching entry; expected O(1), slower with collisions.

## Work through it

Run the merge twice and inspect the old and new count; Hashtable synchronizes individual operations and disallows null keys.

Read the [Java implementation](HashTableDemo.java). Before stepping through it, write down the state you expect after the first operation. Compare it with the next state in the code and explain what fact remains true.
