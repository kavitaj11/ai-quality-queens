# Functions, closures & decorators

[03 · Module topics](../../modules/03-language-deep-dives.md) · [Curriculum index](../../README.md)

**Category** · PYTHON DEEP DIVE

Python functions are first-class objects — pass them, return them, wrap them. A closure remembers its enclosing scope; a decorator uses that to wrap behavior around a function. This is exactly how pytest fixtures, @pytest.mark, and retry helpers work under the hood.

## Worked reference

````text
import functools, time

# Closure: inner() remembers 'prefix' after make_logger returns
def make_logger(prefix):
    def inner(msg):
        print(f"[{prefix}] {msg}")
    return inner
qa_log = make_logger("QA")
qa_log("started")                 # [QA] started

# Decorator: wrap ANY function with retry behavior
def retry(times=3, delay=0.5):
    def deco(fn):
        @functools.wraps(fn)      # keep name/docstring
        def wrapper(*args, **kwargs):
            for attempt in range(1, times + 1):
                try:
                    return fn(*args, **kwargs)
                except Exception as e:
                    if attempt == times:
                        raise
                    time.sleep(delay)
        return wrapper
    return deco

@retry(times=3)
def fetch_status(page):
    return page.locator(".status").inner_text()
````

## Exercise

Write a @retry(times=3) decorator that re-runs a flaky function and returns the first success.
