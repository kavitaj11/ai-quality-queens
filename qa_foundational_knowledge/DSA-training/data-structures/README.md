# Data structures

## Compare the ten data structures

Choose by the operation your code performs most often. Costs below apply to the named Java implementation; an interface such as `Map` or `Queue` does not itself guarantee those costs.

| Structure | Best fit | Lookup | Insert | Remove | Ordering or trade-off |
|---|---|---|---|---|---|
| [Array](sequences/ArraysDemo.md) | Fixed-size samples read by position | Index O(1); value O(n), or O(log n) if sorted | O(n) to grow and copy | O(n) to shift or shrink | Fixed length; allows duplicates |
| [ArrayList](sequences/ArrayListDemo.md) | Growing list with frequent indexed reads | Index O(1); value O(n) | Append amortized O(1); front O(n) | Last O(1); front O(n) | Preserves sequence; middle edits shift elements |
| [LinkedList](sequences/LinkedListDemo.md) | Edits at either end or through a positioned iterator | End O(1); arbitrary index or value O(n) | End or positioned iterator O(1) | Same O(1); by value O(n) | Preserves sequence; locating a node can outweigh the cheap edit |
| [Stack: ArrayDeque](stacks-and-queues/StacksDemo.md) | Undo history or nested calls | Peek O(1); membership O(n) | Push amortized O(1) | Pop O(1) | Last in, first out |
| [Queue: ArrayDeque](stacks-and-queues/QueueDemo.md) | Process jobs in arrival order | Head O(1); membership O(n) | Offer amortized O(1) | Poll head O(1) | First in, first out |
| [Hash table: Hashtable](maps-and-sets/HashTableDemo.md) | Legacy synchronized key/value lookup | Key expected O(1) | Expected amortized O(1) | By key expected O(1) | Unspecified order; no null keys or values |
| [Map: HashMap](maps-and-sets/MapDemo.md) | Expected result or count by business ID | Key expected O(1); value O(n) | Expected amortized O(1) | By key expected O(1) | Unspecified order; putting an existing key replaces its value |
| [Set: HashSet](maps-and-sets/SetDemo.md) | Membership and deduplication | Membership expected O(1) | Expected amortized O(1) | Expected O(1) | Unspecified order; duplicate additions do not change membership |
| [Balanced tree: TreeSet](trees-and-heaps/TreeDemo.md) | Sorted unique keys and range queries | Key or nearest ceiling O(log n) | O(log n) | O(log n) | Iteration follows comparator order |
| [Binary heap: PriorityQueue](trees-and-heaps/HeapDemo.md) | Repeatedly take the next priority item | Head O(1); arbitrary value O(n) | O(log n) amortized | Head O(log n); by value O(n) | Only the head is guaranteed to have highest priority; iteration is not sorted |

**Reading the costs:** `n` is the number of stored elements. *Expected* hash costs assume well-distributed hashes. *Amortized* accounts for occasional backing-array growth across a series of insertions. The tree row describes a balanced search tree; an unbalanced search tree can take O(n) for lookup, insertion, or deletion.

## Maps and sets

- [Hash Table Demo](maps-and-sets/HashTableDemo.md)
- [Map Demo](maps-and-sets/MapDemo.md)
- [Set Demo](maps-and-sets/SetDemo.md)

## Sequences

- [Array List Demo](sequences/ArrayListDemo.md)
- [Arrays Demo](sequences/ArraysDemo.md)
- [Linked List Demo](sequences/LinkedListDemo.md)

## Stacks and queues

- [Queue Demo](stacks-and-queues/QueueDemo.md)
- [Stacks Demo](stacks-and-queues/StacksDemo.md)

## Trees and heaps

- [Heap Demo](trees-and-heaps/HeapDemo.md)
- [Tree Demo](trees-and-heaps/TreeDemo.md)

