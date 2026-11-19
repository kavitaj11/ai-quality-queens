# 4. LLM-as-a-judge and calibration

The live judge evaluates faithfulness, completeness, safety and tool use on a 0–4 rubric. Backend task completion still has its own deterministic oracle. The rubric anchors severe failures, partial success, acceptable behavior and fully satisfied requirements; a fluent answer cannot earn full credit merely for sounding confident.

## Inspect the contract

Read `data/rubric.json` and `aiqe/judge.py`. The request uses strict JSON Schema, and the client independently validates fields, integer ranges, finite confidence and a nonempty rationale. A refusal, truncated output, HTTP error or malformed judgment is an evaluator failure, never an implicit pass. Scores below 3 fail the rollout. Confidence below 0.6 requests review and keeps the gate closed.

The transcript is serialized as untrusted data. Instructions embedded in an answer cannot legitimately alter the rubric. That prompt boundary reduces confusion but does not prove judge-injection resistance; add adversarial judge cases and independent human review. The fixture dataset includes an answer attempting to instruct its evaluator.

## Run the calibration mechanics

```bash
python -m aiqe calibrate --judge fixture --output artifacts/calibration.json
```

This compares stored synthetic predictions with illustrative author labels. It demonstrates the math without calling an LLM. It is **not evidence that a real judge is calibrated**. There are eight development records focused on return-policy answers and eight holdout records from separate task families.

The report gives per-dimension exact agreement, mean absolute error and quadratic weighted Cohen's kappa. Kappa adjusts for marginal score distributions and penalizes larger ordinal disagreements more strongly. When expected disagreement is zero, it is undefined and reported as `null`, not 1. These eight-case partitions are too small for production confidence.

## Calibrate a real evaluator

With the environment variables described in the [project README](project/README.md):

```bash
python -m aiqe calibrate --judge live --output artifacts/live-calibration.json
```

This makes 16 paid requests. Reference labels and fixture predictions are removed before each request. Production work requires independently rated examples, rater-to-rater agreement, adjudication, enough borderline and severe cases, and a holdout never used for tuning. Tune rubric language only on development examples, then freeze it and inspect holdout errors by dimension and failure type. Track judge model, rubric, sampling settings and dataset versions together. Decide acceptance thresholds before seeing the final holdout scores.

**What biases would you investigate?** Position, verbosity, presentation, self-preference, unsupported certainty, and susceptibility to instructions inside evaluated material. Swap response order for pairwise judgments, blind model identity and compare results with human annotations.

**Can structured output guarantee a correct judgment?** No. It constrains the result's shape. Semantic correctness still needs calibration and review. See [Structured Outputs](https://developers.openai.com/api/docs/guides/structured-outputs?api-mode=chat) and the [Chat Completions API contract](https://developers.openai.com/api/reference/resources/chat/subresources/completions/methods/create).

Next: [Adversarial safety](05-adversarial-safety.md).
