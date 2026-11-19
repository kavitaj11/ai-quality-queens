# Trees in Java

## Description

A hierarchy of nodes connected to children. A binary search tree (BST) adds an ordering invariant; not every tree is a search tree.

## When to use it

Use an ordered tree for range queries and sorted iteration; a general tree models hierarchy without requiring sorted keys.

## Advantages and trade-offs

BST operations depend on height h. A balanced tree gives logarithmic height; a skewed tree may degenerate to a list.

## Java classes and utilities

TreeSet and TreeMap expose ordered operations such as ceiling and subSet/subMap. Their public API does not expose child pointers.

## Traversal

Preorder visits the root first, postorder last, and inorder between children. Breadth-first uses a queue. All visit n nodes in O(n).

## Search

Compare with the current BST node and follow only the relevant branch: O(h). Searching an unordered tree may require O(n).

## Sorting

Inorder traversal yields sorted keys only when the BST ordering invariant holds. Building a balanced search tree then traversing costs O(n log n) overall.

## Insertion

Descend to the correct empty child position: O(h). Define duplicate handling; TreeSet rejects comparator-equivalent keys.

## Deletion

Leaf: unlink. One child: promote it. Two children: substitute the inorder successor or predecessor and remove its old node. Balanced trees may rebalance afterwards.

## Work through it

Trace the subset bounds on 2,5,9: the lower bound is included and the upper one excluded.

Read the [Java implementation](TreeDemo.java). Before stepping through it, write down the state you expect after the first operation. Compare it with the next state in the code and explain what fact remains true.
