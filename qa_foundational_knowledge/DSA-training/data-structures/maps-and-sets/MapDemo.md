# Maps in Java

## Description

Map associates each distinct key with one value. HashMap is one implementation; ordering, concurrency and null handling depend on the implementation.

## When to use it

Use for expected results keyed by business ID, configuration lookup, or counting repeated events.

## Advantages and trade-offs

Keys enable direct lookup, but duplicate keys replace previous values unless the code merges them. HashMap does not guarantee iteration order.

## Java classes and utilities

getOrDefault, containsKey, putIfAbsent, computeIfAbsent, merge and entrySet. Choose TreeMap for ordering, LinkedHashMap for predictable encounter order.

## Traversal

Iterate entrySet when both key and value are needed; use keySet or values for one side.

## Search

HashMap key lookup is expected O(1); containsValue is O(n). If null values are allowed, get alone cannot distinguish absent from present-null.

## Sorting

Order by keys with TreeMap or sort a list of entries by value. For tied values, add a key comparator for reproducible output.

## Insertion

put adds or replaces; merge(key,1,Integer::sum) increments a frequency. HashMap insertion is expected amortized O(1).

## Deletion

remove(key) deletes a mapping; remove(key,value) deletes only the matching pair. HashMap removal is expected O(1).

## Work through it

Trace a,b,a and explain why the final count of a is two, regardless of iteration order.

Read the [Java implementation](MapDemo.java). Before stepping through it, write down the state you expect after the first operation. Compare it with the next state in the code and explain what fact remains true.
