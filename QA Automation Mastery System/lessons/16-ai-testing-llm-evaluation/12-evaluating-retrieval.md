# Evaluating retrieval

[16 · Module topics](../../modules/16-ai-testing-llm-evaluation.md) · [Curriculum index](../../README.md)

**Category** · RAG EVALUATION

RAG answers are only as good as the chunks retrieved — so evaluate retrieval on its own, with classic IR metrics: recall@k (did the needed chunk make the top-k?), precision@k (how much of the top-k is relevant?), MRR (how high does the first relevant chunk rank?). The dataset is (question → relevant chunk ids); no LLM needed, so these run fast and deterministic in CI.

## Worked reference

````text
RETRIEVAL EVALUATION — grade the search, not the prose

  dataset:  question → ids of chunks that answer it
  run:      retrieve top-k for each question
  score:    pure set/rank math — no LLM, no noise

  METRICS
  recall@k     needed chunks that made the top-k
               THE metric: if the answer isn't
               retrieved, the model CANNOT be right
  precision@k  how much of the top-k is relevant —
               junk chunks waste context and invite
               hallucination
  MRR          1/rank of first relevant chunk —
               position matters (lost-in-the-middle)
  hit rate     % of questions with ≥1 relevant
               chunk in top-k — the blunt version

  WHAT MOVES THESE NUMBERS (your tuning knobs)
  chunk size + overlap · embedding model · k ·
  hybrid search (vector + keyword) · reranker ·
  metadata filters

  WHY EVALUATE RETRIEVAL SEPARATELY
  • deterministic + cheap → runs on every PR
  • localizes failures: bad answer + good retrieval
    = generation problem; bad answer + bad retrieval
    = search problem (fix embeddings/chunking first)
  • tuning retrieval by 'does the final answer look
    better' is optimizing through two layers of noise

  TRAP
  measuring recall on questions whose phrasing
  copies the chunk verbatim — real users paraphrase;
  build the dataset from real query logs.
````

## Exercise

Build a 20-question retrieval dataset for a doc set you know (question → which doc/chunk answers it). Measure recall@5; then change chunk size and measure again.
