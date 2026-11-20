# Eval frameworks & CI gating

[16 · Module topics](../../modules/16-ai-testing-llm-evaluation.md) · [Curriculum index](../../README.md)

**Category** · LLM EVALUATION

Turn evals into a regression suite that runs in CI on every prompt/model/RAG change. Tools: DeepEval (pytest-native LLM metrics), Promptfoo (YAML-configured eval + red-teaming), Ragas (RAG-specific: faithfulness, context precision/recall), plus vendor tools (OpenAI Evals, LangSmith, Braintrust). Gate the merge on threshold scores — the same discipline as any test gate.

## Worked reference

````text
# DeepEval — LLM metrics as pytest assertions
from deepeval import assert_test
from deepeval.test_case import LLMTestCase
from deepeval.metrics import (
    AnswerRelevancyMetric, FaithfulnessMetric)

def test_rag_answer():
    case = LLMTestCase(
        input="How long do refunds take?",
        actual_output=rag_app(query),
        retrieval_context=retrieved_docs)   # for RAG faithfulness
    assert_test(case, [
        AnswerRelevancyMetric(threshold=0.8),
        FaithfulnessMetric(threshold=0.9),   # no hallucination
    ])
    #  pytest -> runs in CI -> red gate blocks the merge

# Promptfoo — YAML config, great for red-teaming
#   promptfooconfig.yaml:
#     prompts: [prompt.txt]
#     providers: [anthropic:claude-sonnet-4-5]
#     tests:
#       - vars: {q: "..."}
#         assert:
#           - type: contains
#             value: "30 days"
#           - type: llm-rubric
#             value: "declines out-of-policy refunds"
#   run:  npx promptfoo eval
#
# Ragas: RAG-specific — faithfulness, context precision/recall
# Vendor: OpenAI Evals, LangSmith, Braintrust
````

## Exercise

Wire one eval into CI with DeepEval or Promptfoo; make a merge fail when faithfulness drops below 0.9 across your dataset.
