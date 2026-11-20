# Loss, gradients & how models learn

[04 · Module topics](../../modules/04-computational-logic.md) · [Curriculum index](../../README.md)

**Category** · MATH FOR AI ENGINEERING

Enough training math to reason about model behavior: a LOSS function scores how wrong a prediction is; GRADIENT DESCENT nudges millions of weights downhill on that loss, batch after batch. You don't train frontier models — but this explains overfitting (memorized, didn't generalize), why eval sets must stay out of training data, and what a learning curve is telling you.

## Worked reference

````text
# Gradient descent on y = w·x — the whole idea, tiny
xs = [1.0, 2.0, 3.0]
ys = [2.1, 3.9, 6.2]        # true w ≈ 2

w, lr = 0.0, 0.05
for step in range(40):
    # MSE loss and its gradient dL/dw
    grad = sum(2 * (w * x - y) * x
               for x, y in zip(xs, ys)) / len(xs)
    w -= lr * grad           # step downhill

print(round(w, 2))           # → ~2.03

# Overfitting demo — polynomial degree vs truth
import numpy as np
rng = np.random.default_rng(7)
X = np.linspace(0, 1, 10)
Y = 2 * X + rng.normal(0, 0.15, 10)   # line + noise
for deg in (1, 9):
    coeffs = np.polyfit(X, Y, deg)
    train_err = np.mean((np.polyval(coeffs, X) - Y) ** 2)
    # held-out points BETWEEN the training xs:
    Xh = np.linspace(0.05, 0.95, 9)
    hold_err = np.mean((np.polyval(coeffs, Xh) - 2 * Xh) ** 2)
    print(deg, round(train_err, 4), round(hold_err, 4))
# degree 9: train ~0, held-out explodes — memorization

# Overfit check you can run on ANY model:
# score on training-like data vs truly held-out data;
# a big gap = memorization, not understanding
````

## Exercise

Fit y = wx by hand with 5 gradient-descent steps on 3 data points. Then explain train-vs-validation curves to a teammate: what does a widening gap mean?
