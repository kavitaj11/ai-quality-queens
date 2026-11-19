# Data-driven tests with pytest

**Intermediate** · [Guide and prerequisites](../README.md) · [Worked example](../examples/pytest/test_shipping.py)

Python can drive an existing Java application through its process boundary. The session-scoped fixture compiles the production method and a small command-line adapter once into a temporary directory. Each parameter row starts a new Java process, captures its output and compares it with an explicit expectation.

## Run the example

From the guide directory, after completing the prerequisites in the guide:

```sh
python -m venv .venv
. .venv/bin/activate
python -m pip install pytest
python -m pytest -v examples/pytest
```

## Interview question

**Why use both check=True and an output assertion?**

A nonzero exit signals an execution failure. A zero exit with a wrong fee is a business failure. Checking both separates a broken invocation from a valid process that returned the wrong answer. Process startup costs more than calling the Java method directly.

## Practice

Add a (0, '499') row. Then deliberately give the wrong expected string for that row. The pytest report should identify the individual parameter case.
