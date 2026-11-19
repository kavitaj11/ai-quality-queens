# 3. Metrics and non-determinism

Use several independent measurements. A high mean score cannot compensate for private-data leakage or an unauthorized tool action.

| Metric | Lab implementation | Interpretation and limitation |
|---|---|---|
| Completeness | Matched requirement groups ÷ required groups, averaged over turns | Detects omitted required content; phrase matches can be negated or misleading |
| Faithfulness proxy | Supported source-matching sentences ÷ answer sentences on factual turns | Strict lexical proxy; valid paraphrases can fail and source correctness is not established |
| Hallucination proxy rate | 1 − faithfulness proxy | Unsupported-sentence proxy, not a measured semantic hallucination rate |
| Task completion | Exact per-turn and final backend-state match | Real state verification, independent of claims such as “I booked it” |
| Attack success rate | Adversarial rollouts with unsafe output or task-oracle failure ÷ attack rollouts | Outcome-based lab signal; the case gate also checks refusal/content quality |
| Benign over-refusal | Refused turns in benign cases ÷ benign turns | Stops an always-refuse assistant from looking safe |
| Case pass rate | Rollouts passing every required check ÷ rollouts | Report by category as well as overall |

The denominators matter. Factuality is not evaluated for every tool-only turn. A missing attack partition is not proof of zero attack success: the lab's gate rejects an absent required metric.

## Repeat without selecting the best answer

```bash
python -m aiqe run --repeats 5 --seed 11 --output artifacts/repeated
python -m aiqe run --fault incomplete --repeats 5 --seed 11 --output artifacts/unstable
```

The assistant shuffles supported sentences using a local seed. The incomplete defect keeps only one sentence, so different omissions can appear across runs. All runs count; the runner never retries a failed result until it passes. `unstable_cases` counts cases whose binary outcomes change across seeds. It is not a complete measure of semantic diversity.

The report includes a descriptive 95% Wilson interval for rollout pass rate. Correlated variants and repeated runs are not independent production samples, so that interval must not be sold as population assurance. For real systems, report distinct scenario count, per-scenario failure probability and case/family-level bootstrap intervals; preserve paired case/seed identities when comparing versions. Zero observed safety failures still leaves uncertainty.

## Exercise

Compare `--fault hallucination` and `--fault incomplete`. Explain why a fully grounded answer can still be incomplete and why a complete-looking answer can introduce unsupported information. Then add a negated expected phrase and observe why keyword completeness alone is insufficient.

**Would you compare LLM responses with exact text?** Only for a constrained contract, fixed fixture, code/schema requirement or explicitly lexical check. For natural language, use independent facts, rubric-based semantic evaluation, appropriate references and manual review of disagreements.

**How would you set thresholds?** Use failure severity, baseline distributions, coverage, user impact and annotation agreement. The lab deliberately requires perfect results on a small deterministic suite; those numbers are not recommended production thresholds.

Next: [LLM judges and calibration](04-llm-judge-and-calibration.md).

