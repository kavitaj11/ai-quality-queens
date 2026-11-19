# Dijkstra

Compute shortest distances with nonnegative edge weights.

## Work through it

Extract the least known distance first; discard stale queue entries after an improvement. With A→B (5), A→C (1), C→B (2), B improves to 3. A negative edge breaks the greedy guarantee. With lazy heap entries, time is O((V+E) log(E+2)).

Read the [Java implementation](Dijkstra.java). Before stepping through it, write down the state you expect after the first operation. Compare it with the next state in the code and explain what fact remains true.
