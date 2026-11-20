# Stacks, queues, trees & graphs

[04 · Module topics](../../modules/04-computational-logic.md) · [Curriculum index](../../README.md)

**Category** · COMPLEXITY & DATA STRUCTURES

The DOM is a tree. JSON is a tree. Your test-dependency ordering is a graph. Stacks (LIFO) power DFS and undo; queues (FIFO) power BFS and job runners. Knowing BFS vs DFS is knowing how to walk any page, payload, or dependency chain — and why 'find the shallowest match' vs 'exhaust one branch first' are different tools.

## Worked reference

````text
# One walker, two orders: pop() = DFS, pop(0) = BFS
def find_nodes(root, pred):
    frontier, hits = [root], []
    while frontier:
        node = frontier.pop()          # stack → DFS
        if pred(node):
            hits.append(node)
        frontier.extend(node.get("children", []))
    return hits

# Find every deep field in a JSON payload
buttons = find_nodes(dom, lambda n:
    n.get("role") == "button" and not n.get("label"))
# → unlabeled buttons = accessibility bugs

# Crawler rule: BFS + visited set = no infinite loops
from collections import deque
q, seen = deque([start_url]), {start_url}
````

## Exercise

Write a walker that finds every node with an aria-label in a DOM/JSON tree, first with DFS (stack), then BFS (queue). When does each order matter?
