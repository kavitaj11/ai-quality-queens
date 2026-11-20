# Your first pytest test

[08 · Module topics](../../modules/08-frameworks-bdd.md) · [Curriculum index](../../README.md)

**Category** · PYTEST DEEP DIVE

Start with the essentials: a test is a plain function named test_* containing a bare assert — no class, no boilerplate. pytest discovers any test_*.py file, runs every test_* function, and its assert rewriting prints a rich diff on failure. Group related tests in a class Test*. This is the whole core; fixtures and parametrize build on it.

## Worked reference

````text
# test_login.py  — pytest finds files named test_*.py
import pytest
from shop import login, add, InvalidCredentials

# 1. A test is just a function named test_* with a bare assert
def test_valid_login():
    user = login("qa@test.dev", "s3cret")
    assert user.is_authenticated          # rich diff if this fails

# 2. Assert on the exact value
def test_addition():
    assert add(2, 3) == 5

# 3. Assert that something RAISES
def test_bad_password_rejected():
    with pytest.raises(InvalidCredentials):
        login("qa@test.dev", "wrong")

# 4. Group related tests in a Test* class (no inheritance needed)
class TestCart:
    def test_starts_empty(self):
        assert Cart().count == 0

    def test_add_item(self):
        cart = Cart()
        cart.add("A-100")
        assert cart.count == 1

# Run:
#   pytest                 discover + run everything
#   pytest -v              one line per test
#   pytest test_login.py::test_addition   a single test
#   pytest -k "cart"       only tests matching "cart"
````

## Exercise

Write a test_math.py with three functions (a pass, a deliberate fail to see the diff, and one using pytest.raises) and run `pytest -v`.
