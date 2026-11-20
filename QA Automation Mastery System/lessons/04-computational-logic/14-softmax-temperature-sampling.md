# Softmax, temperature & sampling

[04 · Module topics](../../modules/04-computational-logic.md) · [Curriculum index](../../README.md)

**Category** · MATH FOR AI ENGINEERING

Why the same prompt gives different answers: an LLM outputs a probability distribution over next tokens (softmax of logits), then SAMPLES from it. Temperature rescales the distribution — 0 makes it (nearly) deterministic argmax, higher flattens it toward creative/chaotic. Top-p cuts the tail. This math is why judges run at temperature 0 and why eval gates use aggregates.

## Worked reference

````text
import numpy as np

def softmax(logits, temperature=1.0):
    z = np.asarray(logits) / max(temperature, 1e-9)
    e = np.exp(z - z.max())          # stable softmax
    return e / e.sum()

logits = [2.0, 1.0, 0.5]
for T in (1.0, 0.1, 2.0):
    print(T, softmax(logits, T).round(3))
# T=0.1 → top token ~1.0  (why judges use temp 0)
# T=2.0 → flattened      (why creative mode varies)

# Per-token error compounds over a long answer
print(0.99 ** 500)     # ≈ 0.007 — long outputs drift
````

## Exercise

For logits [2.0, 1.0, 0.5], compute the softmax at temperature 1.0, 0.1, and 2.0. What happens to the top token's probability each time?
