# Evaluation criteria: what 'good' means

[16 · Module topics](../../modules/16-ai-testing-llm-evaluation.md) · [Curriculum index](../../README.md)

**Category** · FOUNDATION

Before measuring anything, define the axes. The core criteria: correctness (factually right), faithfulness (grounded in the provided context — the RAG axis), relevance (answers the actual question), completeness, format compliance (parseable, follows instructions), safety (no harmful/leaky output), and tone. Each feature weights them differently — a summarizer lives on faithfulness, a code generator on correctness, a support bot on safety + tone.

## Worked reference

````text
EVALUATION CRITERIA — the axes of 'good'

  CORRECTNESS   factually right (vs the world)
  FAITHFULNESS  grounded in PROVIDED context —
                the anti-hallucination axis for RAG:
                every claim traceable to a chunk
  RELEVANCE     answers the question asked,
                not a nearby easier one
  COMPLETENESS  covers what was required —
                misses nothing the rubric demands
  FORMAT        parseable JSON · length limits ·
                followed instructions ('cite sources')
  SAFETY        no harmful content · no PII/prompt
                leakage · refuses what it should
  TONE/STYLE    brand voice · reading level ·
                language

CORRECT ≠ FAITHFUL — the distinction that matters
  faithful+wrong: context was stale, model quoted it
    → fix the DATA, not the model
  unfaithful+right: model ignored context, knew the
    answer anyway → lucky hallucination; will burn
    you when its knowledge is wrong

MAKING CRITERIA MEASURABLE
  criterion → RUBRIC (1-5 with anchored examples)
  → metric (judge score / assertion / pass-rate)
  vague 'be helpful' rubrics produce noisy judges;
  anchor each level with a concrete example output

PER-FEATURE WEIGHTING (no universal ranking)
  summarizer     faithfulness > completeness > style
  code gen       correctness (it runs!) > style
  support bot    safety > relevance > tone
  extraction     format > correctness > all else
````

## Exercise

For one AI feature, pick the 3 criteria that matter most, define a 1–5 rubric for each, and write one example of a 2 and a 5.
