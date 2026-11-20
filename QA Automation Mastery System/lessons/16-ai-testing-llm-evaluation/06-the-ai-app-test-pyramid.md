# The AI-app test pyramid

[16 · Module topics](../../modules/16-ai-testing-llm-evaluation.md) · [Curriculum index](../../README.md)

**Category** · FUNDAMENTALS OF TESTING AI

AI apps still deserve a pyramid — most checks cheap and deterministic, few expensive end-to-end evals. Unit-test the plumbing and guardrails, integration-test tool calls and RAG retrieval, and reserve graded LLM evals for output quality. A layer many teams add: fast, deterministic 'assertion evals' (regex, JSON-schema, contains) before the costly model-graded ones.

## Worked reference

````text
TEST PYRAMID FOR AN AI / LLM APPLICATION

        /\    LLM EVALS (model-graded)     few · slow · $$
       /  \   faithfulness, correctness, tone
      /----\  — graded by an LLM judge or humans, over a dataset
     /      \ INTEGRATION                 some
    /        \ tool calls, RAG retrieval hit-rate, guardrail
   /          \ triggers, API contract of the AI endpoint
  /------------\ ASSERTION EVALS + UNIT   many · fast · cheap
 /______________\ deterministic checks on output & plumbing:
                   JSON-schema valid · required fields · regex ·
                   "contains X" · refuses banned topic · no PII ·
                   latency & cost budget · retry/timeout logic

RUN CHEAP-FIRST (fail fast before spending model $ )
  1. deterministic assertion evals   (ms, free)
  2. retrieval / tool-call checks     (fast)
  3. model-graded quality evals       (slow, costs tokens)

REGRESSION GATE (the point of it all)
  every prompt edit, model swap, or RAG change re-runs the eval
  set; block the change if any dimension drops below its
  threshold — the LLM equivalent of a failing unit test.
````

## Exercise

For a RAG chatbot, place these checks on the pyramid: JSON-schema of the response, retrieval hit-rate, answer faithfulness, prompt-injection resistance, p95 latency.
