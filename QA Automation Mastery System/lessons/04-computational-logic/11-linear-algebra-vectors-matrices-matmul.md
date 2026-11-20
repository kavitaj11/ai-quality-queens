# Linear algebra: vectors, matrices & matmul

[04 · Module topics](../../modules/04-computational-logic.md) · [Curriculum index](../../README.md)

**Category** · MATH FOR AI ENGINEERING

The substrate of all ML: data is vectors, models are matrices, and inference is matrix multiplication. A weight matrix is a learned transformation; stacking layers = composing transformations; attention is dot products between query and key vectors at scale. Knowing shapes ((n,k)·(k,m)→(n,m)) lets you sanity-check any pipeline and read any model bug report.

## Worked reference

````text
import numpy as np

# A 'layer' is just matmul + bias
x = np.array([[1.0, 2.0, 3.0]])          # (1,3) input
W = np.array([[1.0, 0.0],
              [2.0, 1.0],
              [1.0, 1.0]])               # (3,2) weights
h = x @ W                                 # (1,2) → [[8, 5]]

# Shape assertions — cheap tests that catch real bugs
assert x.shape[1] == W.shape[0], "inner dims must match"

# Attention in one line: scores = Q · Kᵀ
Q, K = np.random.rand(4, 64), np.random.rand(4, 64)
scores = Q @ K.T                          # (4,4) token-pair

# The QA check for any embedding pipeline:
assert embed("probe").shape[0] == INDEX_DIM, \
    "embedding model and vector index disagree on dims"
````

## Exercise

By hand, multiply a (2,3) matrix by a (3,1) vector. Then explain why embedding dims must match between your embedding model and your vector database.
