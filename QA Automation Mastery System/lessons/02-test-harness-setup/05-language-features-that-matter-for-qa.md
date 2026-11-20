# Language features that matter for QA

[02 · Module topics](../../modules/02-test-harness-setup.md) · [Curriculum index](../../README.md)

**Category** · LANGUAGE ESSENTIALS

Before the test frameworks, the language itself. Each tab is the working subset an automation engineer actually leans on: Python's comprehensions, context managers and typing; JavaScript's async/await, promises and destructuring; TypeScript's type system as a quality gate; Java's OOP, collections and streams.

## Worked reference

````text
# Comprehensions — build test data in one line
emails = [f"user{i}@test.dev" for i in range(5)]
admins = {u.id: u for u in users if u.role == "admin"}

# Context managers — guaranteed cleanup (the 'with' block)
with open("data.json") as f, sync_playwright() as p:
    data = json.load(f)
    browser = p.chromium.launch()
# both closed automatically, even on exception

# f-strings, unpacking, walrus
name, *rest = ["Ada", "QA", "Lead"]
if (n := len(rest)) > 1:
    print(f"{name} has {n} tags")

# Type hints + dataclasses (self-documenting tests)
from dataclasses import dataclass
@dataclass
class Order:
    sku: str
    qty: int = 1

# Generators — stream large data without loading all
def rows(path):
    with open(path) as f:
        for line in f:          # lazy, memory-friendly
            yield line.strip()
````

## Exercise

In your primary language, rewrite one messy sample test using the idioms here (comprehension / async-await / typed builder / stream). Note what got shorter.
