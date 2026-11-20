# String & collection patterns

[17 · Module topics](../../modules/17-interview-prep.md) · [Curriculum index](../../README.md)

**Category** · SDET CODING DRILLS

Reverse words, dedupe, anagram checks, frequency maps — 80% of SDET coding screens are these patterns in your language of choice.

## Worked reference

````text
def first_unique(s: str) -> str | None:
    from collections import Counter
    counts = Counter(s)
    return next((c for c in s if counts[c] == 1), None)

assert first_unique("automation") == "u"
````

## Exercise

Solve: first non-repeating character, in Python AND Java, in under 10 minutes each.
