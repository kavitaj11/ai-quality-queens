# Iterators, generators & lazy evaluation

[03 · Module topics](../../modules/03-language-deep-dives.md) · [Curriculum index](../../README.md)

**Category** · PYTHON DEEP DIVE

An iterator produces values on demand; a generator (yield) is the easy way to build one. Lazy evaluation means you never load a million-row CSV into memory — you stream it. Generators also power fixtures: the code after yield is your teardown.

## Worked reference

````text
# Generator: values produced one at a time, lazily
def read_emails(path):
    with open(path) as f:
        for line in f:
            yield line.strip()        # nothing loaded upfront

# Consume lazily — memory stays flat on huge files
for email in read_emails("users.csv"):
    if email.endswith("@test.dev"):
        break                          # stops early, rest never read

# Generator expression (like a comprehension, but lazy)
first_five = (u for u in read_emails("users.csv"))
next(first_five)                       # pull one value

# The fixture pattern is just a generator:
#   yield  -> hand the value to the test
#   after  -> teardown, always runs
def db_conn():
    conn = connect()
    yield conn
    conn.close()
````

## Exercise

Write a generator that yields test users in batches of 100 from a large file without loading it all.
