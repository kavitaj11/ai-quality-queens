# Sorting and searching

## Compare searching algorithms

Both Java methods return the **first matching index**, or −1 if the key is absent. Neither modifies the input.

| Algorithm | When to use it | Required input | Best / worst time | Extra space |
|---|---|---|---|---|
| [Linear search](searching/LinearSearch.md) | One lookup, or data that arrives in no particular order | Any array | Θ(1) / Θ(n) | O(1) |
| [Binary search](searching/BinarySearch.md) | Repeated lookups on already sorted data | Ascending array | Θ(log n) / Θ(log n) for this lower-bound loop on nonempty, growing inputs | O(1) |

**Why no constant-time best case for this binary search?** If the midpoint matches, the method still checks the left half for an earlier duplicate. Also account for the cost of sorting: for one query on unsorted input, a linear scan is usually less work and preserves the original indices.

## Compare sorting algorithms

| Algorithm | When to use it | Typical time | Worst time | Extra space | Stable? |
|---|---|---|---|---|---|
| [Bubble sort](sorting/BubbleSort.md) | Trace adjacent swaps on small inputs; an unchanged pass stops early | Θ(n²); sorted Θ(n) | Θ(n²) | O(1) | Yes |
| [Insertion sort](sorting/InsertionSort.md) | Small or nearly sorted runs | Θ(n + I) | Θ(n²) | O(1) | Yes |
| [Selection sort](sorting/SelectionSort.md) | Small inputs where limiting swaps matters more than comparisons | Θ(n²) | Θ(n²) | O(1) | No |
| [Cycle sort](sorting/CycleSort.md) | Minimize writes by rotating misplaced values to their destinations | Θ(n²) | Θ(n²) | O(1) | No |
| [Merge sort](sorting/MergeSort.md) | Preserve equal-key order with a predictable time bound | Θ(n log n) | Θ(n log n) | O(n) | Yes |
| [Heap sort](sorting/HeapSort.md) | Guarantee a time bound without allocating a merge buffer | Θ(n log n) | Θ(n log n) | O(1) | No |
| [Quick sort](sorting/QuickSort.md) | Partition an in-memory array with mostly distinct keys | Expected O(n log n) | O(n²) | Stack: expected O(log n), worst O(n) | No |
| [Three-way quick sort](sorting/QuickSortAlternative.md) | Many repeated keys; skip the equal region after partitioning | Expected O(n log n); all equal Θ(n) | O(n²) | Stack: expected O(log n), worst O(n) | No |
| [Hybrid quick sort](sorting/HybridQuickSort.md) | Combine three-way partitioning with insertion sort for short runs | Expected O(n log n) | O(n²) | Stack: expected O(log n), worst O(n) | No |
| [Counting sort](sorting/CountingSort.md) | Integer keys in a compact range | Expected O(n + R) | Scans all R keys; map costs depend on collisions | O(u) frequency map | Rebuilds keys only |
| [Radix sort](sorting/RadixSort.md) | Nonnegative integers with a bounded number of digits | Θ(d(n + b)) | Θ(d(n + b)) | O(n + b) | Yes, via stable digit passes |

**Notation:** n = item count; I = inversions (out-of-order pairs); R = inclusive numeric range; u = distinct keys; d = digit passes; b = radix (10 here). Space excludes the input but includes recursive call stacks. A stable sort preserves the relative order of records with equal keys. The counting-sort demo emits integer values from frequencies, so it does not preserve associated records.

**Counting-sort edge case:** The implementation scans the numeric range with an `int` loop counter. A maximum key of `Integer.MAX_VALUE` would wrap the counter; use a wider counter before testing that boundary.
