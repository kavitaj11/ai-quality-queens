# Vectors, embeddings & cosine similarity

[04 · Module topics](../../modules/04-computational-logic.md) · [Curriculum index](../../README.md)

**Category** · MATH FOR AI ENGINEERING

An embedding maps text to a vector where MEANING becomes geometry: similar sentences sit close together. Cosine similarity measures the angle between two vectors (1 = same direction, 0 = unrelated) — the math behind semantic assertions ('output means the same as the gold answer'), RAG retrieval, and near-duplicate detection in test data.

## Worked reference

````text
import numpy as np

def cosine(a, b):
    a, b = np.asarray(a), np.asarray(b)
    return a @ b / (np.linalg.norm(a) * np.linalg.norm(b))

# Semantic assertion — catches what exact-match misses
v_out  = embed(model_output)     # any embedding API
v_gold = embed(case["expected"])
assert cosine(v_out, v_gold) > 0.85, \
    f"output drifted from gold: {cosine(v_out, v_gold):.2f}"

# Dedupe an eval dataset by near-identity
vecs = [embed(c["input"]) for c in dataset]
dupes = [(i, j) for i in range(len(vecs))
         for j in range(i + 1, len(vecs))
         if cosine(vecs[i], vecs[j]) > 0.97]
````

## Exercise

Embed 5 paraphrases of one support answer plus 5 unrelated sentences; compute pairwise cosine similarity and pick the threshold that separates them.
