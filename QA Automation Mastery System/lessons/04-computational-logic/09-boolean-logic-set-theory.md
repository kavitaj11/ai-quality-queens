# Boolean logic & set theory

[04 · Module topics](../../modules/04-computational-logic.md) · [Curriculum index](../../README.md)

**Category** · MATH & LOGIC FOR TESTERS

Predicates are your assertions; De Morgan's laws are how you negate them without bugs: not(A and B) = notA or notB. Truth tables expose the condition combinations your branch coverage misses — MC/DC coverage demands each condition independently flips the outcome. Set theory is the same logic on data: subset checks, intersections, complements.

## Worked reference

````text
from itertools import product

# Truth-table a predicate straight into parametrize
rule = lambda member, bulk, promo: member and (bulk or promo)

table = [(m, b, p, rule(m, b, p))
         for m, b, p in product([True, False], repeat=3)]

# De Morgan: negate WITHOUT re-deriving by hand
no_discount = lambda m, b, p: (not m) or (not b and not p)
assert all(no_discount(m, b, p) == (not rule(m, b, p))
           for m, b, p in product([True, False], repeat=3))

# Set theory as assertions
assert set(actual_perms) <= set(allowed_perms)  # subset
assert not (set(admin_only) & set(user_perms))  # disjoint
````

## Exercise

Take a 3-condition discount rule from your app. Build its truth table (8 rows), decide the minimum test set for MC/DC, and negate the rule correctly with De Morgan.
