# Setup & teardown

[02 · Module topics](../../modules/02-test-harness-setup.md) · [Curriculum index](../../README.md)

**Category** · LANGUAGE ESSENTIALS

Create state before the test and guarantee cleanup after — even on failure. pytest yield fixtures, Playwright hooks, custom TS fixtures, and TestNG lifecycle annotations.

## Worked reference

````text
import pytest

@pytest.fixture
def user(api):
    u = api.create_user(User())
    yield u                       # test runs here
    api.delete_user(u.id)         # always runs

@pytest.fixture(scope="session")
def api():
    client = ApiClient(BASE_URL)
    yield client
    client.close()
````

## Exercise

Write a fixture/hook that creates a user via API and always deletes it, even when the test fails. Prove it with a deliberately failing test.
