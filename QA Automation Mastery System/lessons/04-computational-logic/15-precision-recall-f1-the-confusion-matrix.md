# Precision, recall, F1 & the confusion matrix

[04 · Module topics](../../modules/04-computational-logic.md) · [Curriculum index](../../README.md)

**Category** · MATH FOR AI ENGINEERING

The vocabulary of every classifier you'll ever test — including your own flake detectors and LLM judges. Precision: of what I flagged, how much was real? Recall: of what was real, how much did I catch? F1 balances them. The confusion matrix (TP/FP/FN/TN) is where they all come from, and base rates are why 95% accuracy can still mean mostly-false alarms.

## Worked reference

````text
def prf(tp, fp, fn):
    precision = tp / (tp + fp)
    recall    = tp / (tp + fn)
    f1 = 2 * precision * recall / (precision + recall)
    return precision, recall, f1

# Validate an LLM judge against human labels
tp = sum(1 for j, h in zip(judge, human) if j and h)
fp = sum(1 for j, h in zip(judge, human) if j and not h)
fn = sum(1 for j, h in zip(judge, human) if not j and h)

p, r, f1 = prf(tp, fp, fn)
print(f"precision {p:.2f}  recall {r:.2f}  F1 {f1:.2f}")

# Gate rule: don't trust the judge in CI until
# precision vs humans clears your bar (e.g. 0.9)
assert p >= 0.9, "judge not aligned enough to gate merges"
````

## Exercise

Your judge flags 60 outputs as bad; humans confirm 45. Humans found 70 bad total. Compute precision, recall, and F1 — then argue which matters more in a merge gate.
