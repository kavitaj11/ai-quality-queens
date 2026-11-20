# Coding standards for test code

[02 · Module topics](../../modules/02-test-harness-setup.md) · [Curriculum index](../../README.md)

**Category** · LANGUAGE ESSENTIALS

Test code is production code — it's read far more than it's written, and a messy suite rots into flakiness no one trusts. Apply the same rigor: consistent naming, small focused tests (one behavior each, Arrange-Act-Assert), no magic values or sleeps, DRY via fixtures/helpers, and an auto-formatter + linter enforcing it in CI. Each tab shows the conventions and the tooling for that stack.

## Worked reference

````text
# ── NAMING: test_<unit>_<scenario>_<expected> ──
def test_login_with_expired_token_returns_401(): ...   # ✓ intent-revealing
def test_1(): ...                                       # ✗ meaningless

# ── STRUCTURE: one behavior, Arrange–Act–Assert ──
def test_checkout_applies_member_discount(cart, member):
    cart.add("A-100")                       # Arrange
    total = cart.checkout(as_user=member)   # Act
    assert total == 449.10                  # Assert (one reason to fail)

# ── NO magic values / NO sleeps ──
MAX_RETRIES = 3                             # named constant
wait.until(EC.visibility_of_element_located(TOAST))  # not time.sleep(3)

# ── DRY: share setup via fixtures, not copy-paste ──
#   data via factories, selectors in page objects

# TOOLING (enforce in CI, not by review nagging)
#   black .              # auto-format (PEP 8)
#   ruff check .         # fast linter
#   isort .              # import order
#   mypy .               # type checks
#   pre-commit run -a    # git hook runs them all
# pyproject.toml pins the config so everyone agrees.
````

## Exercise

Add a formatter + linter to a suite and make CI fail on violations; then refactor one bloated test into AAA form with a descriptive name and zero magic literals.
