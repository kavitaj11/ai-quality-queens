# Invariants, contracts & properties

[04 · Module topics](../../modules/04-computational-logic.md) · [Curriculum index](../../README.md)

**Category** · MATH & LOGIC FOR TESTERS

An invariant is something that must ALWAYS hold — cart total ≥ 0, ids unique, sorted output is a permutation of input. Preconditions say when an operation is legal; postconditions say what it guarantees. This is the logic behind property-based testing: instead of hand-picking examples, assert properties over generated inputs and let the machine hunt counterexamples.

## Worked reference

````text
from hypothesis import given, strategies as st
import json

# Property: discount postcondition + invariants hold
@given(total=st.floats(min_value=0.01, max_value=9999),
       pct=st.integers(min_value=1, max_value=100))
def test_discount_contract(total, pct):
    new_total = apply_discount(total, pct)
    assert new_total >= 0                        # invariant
    assert new_total <= total                    # monotone
    assert abs(new_total - total * (1 - pct/100)) < 0.01

# Property: round-trip — serializer never loses data
@given(st.recursive(st.none() | st.booleans() | st.text(),
       lambda kids: st.lists(kids) |
                    st.dictionaries(st.text(), kids)))
def test_roundtrip(payload):
    assert json.loads(json.dumps(payload)) == payload
````

## Exercise

Write 3 invariants for a shopping cart (total, quantity, dedupe). Then express 'apply_discount' as pre/postconditions and turn one into a property-based test.
