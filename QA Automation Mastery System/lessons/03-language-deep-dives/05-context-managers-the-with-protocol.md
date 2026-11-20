# Context managers & the with protocol

[03 · Module topics](../../modules/03-language-deep-dives.md) · [Curriculum index](../../README.md)

**Category** · PYTHON DEEP DIVE

A context manager guarantees setup and cleanup around a block — __enter__ runs on entry, __exit__ always runs on exit, even on exception. That 'always cleans up' guarantee is why every browser, file, and DB handle in a good suite lives inside a with block or a yield fixture.

## Worked reference

````text
from contextlib import contextmanager
import time

# Class form: __enter__ / __exit__
class Timer:
    def __enter__(self):
        self.start = time.perf_counter()
        return self
    def __exit__(self, exc_type, exc, tb):
        print(f"took {time.perf_counter() - self.start:.2f}s")
        return False                   # don't swallow exceptions

with Timer():
    run_regression_suite()             # timed, even if it raises

# Decorator form: everything before yield = enter,
# after = exit (guaranteed)
@contextmanager
def temp_user(api):
    u = api.create_user()
    try:
        yield u                        # hand to the block
    finally:
        api.delete_user(u.id)          # always cleans up

with temp_user(api) as u:
    login(u)
````

## Exercise

Write a context manager that starts a timer on enter and prints the elapsed time on exit — even if the block raises.
