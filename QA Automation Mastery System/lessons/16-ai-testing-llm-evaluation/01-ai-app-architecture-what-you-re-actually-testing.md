# AI app architecture: what you're actually testing

[16 · Module topics](../../modules/16-ai-testing-llm-evaluation.md) · [Curriculum index](../../README.md)

**Category** · FOUNDATION

An 'AI app' is a pipeline, not just a model: input handling → prompt assembly (template + context + history) → maybe retrieval (RAG) → the model call → output parsing/guardrails → the app around it. Each stage fails differently and is tested differently — most 'the AI is wrong' bugs live in retrieval or prompt assembly, not in the model. Map the architecture first; it tells you where evals attach.

## Worked reference

````text
AI APP ARCHITECTURE — the pipeline behind 'the model'

  user input
     │
  ┌──▼──────────────────────────────────────────────┐
  │ INPUT HANDLING  validation · length limits ·    │
  │                 injection screening             │
  ├─────────────────────────────────────────────────┤
  │ PROMPT ASSEMBLY system prompt + template +      │
  │                 history + retrieved context     │
  ├─────────────────────────────────────────────────┤
  │ RETRIEVAL (RAG) embed query → vector search →   │
  │  (optional)     rank → stuff top-k into prompt  │
  ├─────────────────────────────────────────────────┤
  │ MODEL CALL      model id · temperature ·        │
  │                 max tokens · tool definitions   │
  ├─────────────────────────────────────────────────┤
  │ OUTPUT STAGE    parse (JSON?) · guardrails ·    │
  │                 moderation · fallbacks          │
  └─────────────────────────────────────────────────┘
     │
  app UI / downstream systems

WHERE 'BAD AI' BUGS ACTUALLY LIVE
  retrieval    wrong/stale chunks retrieved → model
               answers faithfully from bad context
  assembly     truncated history · context overflow
               silently dropping the system prompt
  output       model was right, JSON parser choked
  model        actual hallucination / capability gap
               — the minority of cases

TESTING CONSEQUENCE
  test STAGES separately (retrieval hit-rate, prompt
  snapshot tests, parser unit tests) + END-TO-END
  evals for the whole pipeline. A failing e2e eval
  with passing stage tests localizes the bug.
````

## Exercise

Draw this pipeline for one AI feature you know. For each box, name one failure that would look like 'bad AI output' to a user but is actually that box's bug.
