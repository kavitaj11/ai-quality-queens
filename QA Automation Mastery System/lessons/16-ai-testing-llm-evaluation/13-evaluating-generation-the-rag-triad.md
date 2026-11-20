# Evaluating generation: the RAG triad

[16 · Module topics](../../modules/16-ai-testing-llm-evaluation.md) · [Curriculum index](../../README.md)

**Category** · RAG EVALUATION

With retrieval fixed, grade what the model did with the chunks — the RAG triad: faithfulness (every claim grounded in retrieved context), answer relevance (addresses the question), and context relevance (were the chunks even useful?). The triad localizes failure: unfaithful → generation problem; faithful but irrelevant context → retrieval problem; everything scores well but users unhappy → your criteria are wrong.

## Worked reference

````text
THE RAG TRIAD — three judgments that localize failure

              question
             ╱        ╲
     ANSWER RELEVANCE  CONTEXT RELEVANCE
     does the answer   were the retrieved
     address the       chunks useful for
     question?         this question?
             ╲        ╱
              answer ←── FAITHFULNESS ──→ context
              every claim in the answer
              supported by the chunks?

  HOW FAITHFULNESS IS ACTUALLY SCORED
  1. decompose the answer into atomic CLAIMS
  2. for each claim: supported by any chunk? (judge)
  3. faithfulness = supported / total claims
  citation checking is the strict variant: does the
  cited chunk contain the cited fact?

  READING THE TRIAD (triage table)
  faithful? relevant  ctx-relevant  diagnosis
  ✗         —         ✓            GENERATION: model
                                   embellished — tighten
                                   prompt, lower temp
  ✓         ✗         ✓            answered the wrong
                                   question — prompt
  ✓         ✓         ✗            lucky parametric
                                   answer; retrieval is
                                   broken (see pAm0t2)
  ✗         ✗         ✗            everything upstream
                                   failed — fix retrieval
                                   first, re-eval

  TOOLING
  Ragas · TruLens · DeepEval implement the triad;
  the concepts above are what you configure and
  CALIBRATE — judge scores vs 20 human labels first.
````

## Exercise

Take 5 real RAG answers, decompose each into individual claims, and check every claim against the retrieved chunks. Count unsupported claims — that's your faithfulness score.
