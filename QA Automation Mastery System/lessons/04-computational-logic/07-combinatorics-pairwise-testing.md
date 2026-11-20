# Combinatorics & pairwise testing

[04 · Module topics](../../modules/04-computational-logic.md) · [Curriculum index](../../README.md)

**Category** · MATH & LOGIC FOR TESTERS

Test-case explosion is a counting problem: 4 parameters with 3 values each = 81 combinations — and it multiplies from there. Combinatorics tells you the size of the space; pairwise (all-pairs) testing exploits the fact that most bugs involve at most two parameters interacting, covering every value PAIR in ~a dozen cases instead of the full product.

## Worked reference

````text
from itertools import product
from allpairspy import AllPairs   # pip install allpairspy

params = [
    ["chrome", "firefox", "safari"],
    ["win", "mac", "linux"],
    ["admin", "user"],
    ["en", "de", "ja"],
]

full = list(product(*params))
print(len(full))                  # 54 — the whole space

pairs = list(AllPairs(params))
print(len(pairs))                 # ~12 — every pair covered

# feed straight into pytest:
# @pytest.mark.parametrize("browser,os,role,locale",
#                          list(AllPairs(params)))
````

## Exercise

Browser {Chrome, Firefox, Safari} × OS {Win, Mac, Linux} × role {admin, user} × locale {en, de, ja}: compute the full product, then build a pairwise set and count the reduction.
