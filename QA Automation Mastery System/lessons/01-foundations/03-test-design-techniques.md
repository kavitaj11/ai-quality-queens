# Test design techniques

[01 · Module topics](../../modules/01-foundations.md) · [Curriculum index](../../README.md)

**Category** · TESTING FUNDAMENTALS

The three interview favorites, shown side-by-side on the same problems: equivalence partitioning (one value per input class), boundary value analysis (the edges of each class), and decision tables (every rule combination gets a row). Together they turn infinite input space into a small, high-value case set.

## Worked reference

````text
import pytest

# 1. EQUIVALENCE PARTITIONING — one value per class
#    age: <18 invalid | 18–65 valid | >65 invalid
@pytest.mark.parametrize("age,valid",
    [(10, False), (40, True), (80, False)])
def test_age_partitions(age, valid):
    assert is_valid_age(age) == valid

# 2. BOUNDARY VALUE ANALYSIS — edges of each class
@pytest.mark.parametrize("age,valid",
    [(17, False), (18, True), (65, True), (66, False)])
def test_age_boundaries(age, valid):
    assert is_valid_age(age) == valid

# 3. DECISION TABLE — every rule combination
#    member? coupon?  ->  discount %
@pytest.mark.parametrize("member,coupon,discount", [
    (False, False,  0),
    (False, True,  10),
    (True,  False,  5),
    (True,  True,  15),
])
def test_discount_rules(member, coupon, discount):
    assert calc_discount(member, coupon) == discount
````

## Exercise

For a shipping calculator (weight 0.1–50kg, member + express flags): write the partitions, the boundary values, and the full decision table. Count your cases before and after.
