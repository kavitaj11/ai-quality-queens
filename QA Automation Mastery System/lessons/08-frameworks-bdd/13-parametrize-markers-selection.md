# Parametrize, markers & selection

[08 · Module topics](../../modules/08-frameworks-bdd.md) · [Curriculum index](../../README.md)

**Category** · PYTEST DEEP DIVE

parametrize turns one test into a data-driven matrix; markers (smoke, regression, slow) plus -m expressions control exactly what runs where.

## Worked reference

````text
@pytest.mark.smoke
@pytest.mark.parametrize("user,ok", [
    ("standard_user", True),
    ("locked_out_user", False),
])
def test_login_matrix(page, user, ok):
    login(page, user)
    assert dashboard_visible(page) == ok

# Run:  pytest -m "smoke and not slow" -n 4
````

## Exercise

Mark your suite into smoke/regression sets; wire 'pytest -m smoke' as the PR gate.
