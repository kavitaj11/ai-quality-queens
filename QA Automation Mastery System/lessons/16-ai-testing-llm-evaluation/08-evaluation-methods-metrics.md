# Evaluation methods & metrics

[16 · Module topics](../../modules/16-ai-testing-llm-evaluation.md) · [Curriculum index](../../README.md)

**Category** · LLM EVALUATION

Four ways to grade LLM output, cheapest first: (1) deterministic assertions (exact/contains/regex/JSON-schema); (2) reference-based metrics (exact-match, BLEU/ROUGE, embedding similarity) when you have a gold answer; (3) LLM-as-judge for open-ended quality; (4) human review, the gold standard for calibration. Pick the cheapest method that actually measures the quality you care about.

## Worked reference

````text
# 1. DETERMINISTIC ASSERTIONS — fast, free, exact
assert output.strip().startswith("{")          # format
assert "coupon" not in output.lower()           # safety
import json; json.loads(output)                 # valid JSON
import re; assert re.search(r"\border\b", output)

# 2. REFERENCE-BASED (you have a gold answer)
from difflib import SequenceMatcher
sim = SequenceMatcher(None, output, gold).ratio()
# better: cosine similarity of embeddings
#   score = cosine(embed(output), embed(gold))
#   captures MEANING, not just word overlap

# 3. LLM-AS-JUDGE — for open-ended quality (next topic)

# 4. HUMAN REVIEW — the calibration gold standard;
#    sample N outputs, have experts rate, and use those
#    ratings to VALIDATE that your automated judge agrees.

# METRIC CHEAT-SHEET
#   classification task   accuracy, precision/recall, F1
#   retrieval (RAG)       recall@k, MRR, hit-rate
#   generation vs gold    BLEU/ROUGE (overlap), BERTScore
#   open-ended quality    LLM-judge score + human spot-check
#   safety                refusal rate, toxicity classifier
````

## Exercise

For a summarization feature, choose a metric per property: factual accuracy, length limit, no PII, and readability. Justify each choice.
