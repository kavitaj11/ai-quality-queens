# End-to-end RAG eval & regression

[16 · Module topics](../../modules/16-ai-testing-llm-evaluation.md) · [Curriculum index](../../README.md)

**Category** · RAG EVALUATION

Component metrics catch component bugs; users experience the whole pipeline. The e2e layer: golden Q&A pairs scored for final answer quality, 'I don't know' probes (questions the corpus can't answer — the system should say so, not improvise), freshness checks after re-indexing, and regression runs on every chunking/embedding/prompt change — the RAG-specific triggers your CI needs.

## Worked reference

````text
END-TO-END RAG EVALUATION — the whole pipeline

  GOLDEN Q&A SET
  (question, expected answer, source chunk ids)
  score: correctness judge vs expected + citation
  accuracy; aggregate pass-rate gates the merge

  THE 'I DON'T KNOW' PROBE SET — most-skipped, most
  valuable: questions the corpus CANNOT answer
    good system:  'the docs don't cover this'
    bad system:   fluent confident improvisation
  measure: honest-decline rate on unanswerable set
  (pairs with under/over-refusal in failure modes)

  RAG-SPECIFIC REGRESSION TRIGGERS (add to CI)
  re-chunking or chunk-size change   → retrieval evals
  embedding model swap               → FULL re-eval;
    old and new vectors are incomparable spaces
  corpus update / re-index           → freshness probe:
    ask about the NEW content; verify old deleted
    content is gone (stale-answer bug)
  prompt or k change                 → triad + e2e

  METRIC LADDER (what runs when)
  PR      retrieval recall@k + schema/assertion checks
          (fast, deterministic)
  nightly triad judges + full golden Q&A + IDK probes
  release + adversarial: injection-in-chunks set —
          malicious instructions planted in documents
          (retrieval delivers the attack payload)

  THE NUMBER THAT LIES
  a high e2e score over a dataset of easy questions.
  Grade the DATASET too: does it cover paraphrases,
  multi-hop questions, and unanswerables? Coverage
  of hard cases is the real quality bar.
````

## Exercise

Add 5 unanswerable questions to your golden set. Score the current system: how many does it honestly decline vs confidently improvise?
