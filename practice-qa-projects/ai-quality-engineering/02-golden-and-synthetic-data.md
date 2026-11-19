# 2. Golden and synthetic datasets

A golden dataset is a reviewed specification of acceptable behavior, not a folder of model outputs that happened to look plausible. This lab's 20 cases cover factual support, abstention, benign boundaries, privacy, injection, state changes, tool use and connector failures.

## Read a case

Each JSONL record has `id`, `family`, `category`, `split`, `attack`, `turns` and final `expected_bookings`. Each turn contains a user message, predicate groups, required citation IDs, expected refusal, and `expected_bookings_after`.

A predicate group represents alternatives for one requirement. All groups must pass, but any phrase within a group may satisfy that requirement. For a return-policy answer, the window and the proof-of-purchase requirement are separate groups. This is a narrow offline oracle, not a substitute for semantic understanding.

Backend state is checked after every turn. Waiting until the final turn could hide an unauthorized action that was later reversed. The agent cannot inspect these expected values.

## Generate candidates

```bash
python -m aiqe generate --count 24 --seed 7 --output artifacts/candidates.jsonl
python -m aiqe run --dataset artifacts/candidates.jsonl --allow-candidates --output artifacts/candidates
```

The generator adds four persona contexts: new user, busy parent, traveler and a request for short clear language. It preserves multi-turn sequences, records the source case and generator seed, and fingerprints both the dialogue and its context/connector scenario. Two identical user questions with different poisoned documents are different test scenarios. The generator is bounded to 80 combinations and rejects an excessive request.

Candidates are **unreviewed** and cannot enter the normal evaluation by accident. Generation creates coverage proposals; it does not establish that inherited labels remain correct. In particular, a new accessibility request may add obligations absent from the old answer rubric.

## Review and split policy

1. Verify that the scenario represents a real user need or threat boundary.
2. Check expected facts against authoritative evidence and expected actions against backend contracts.
3. Review all turns for contradictions, privacy exposure and unintended ambiguity.
4. Deduplicate semantically, not just by text hash.
5. Keep a base scenario and its persona/paraphrase descendants in the same split. Use `family` and `source_case_id` as starting points, then review near-duplicates.
6. Obtain independent annotations and adjudicate disagreements before promotion.

The calibration dataset demonstrates development/holdout family separation. Its labels are illustrative author judgments, not collected human-rater data. A production holdout should remain unavailable to prompt and rubric tuning.

## Exercise and review questions

Add an interrupted booking conversation in which the user asks a factual question before confirming. Decide whether the assistant should resume the pending action and how to preserve intent. Add independent state expectations before changing the agent.

**How would you generate high-quality synthetic data?** Sample a risk taxonomy and production failure patterns, vary persona, wording and conversation structure, validate schemas and provenance, then review labels and decontaminate splits. Model-generated examples are candidates until reviewed.

**How do you prevent evaluation leakage?** Keep answer labels out of the system-under-test input, split by scenario family, freeze holdouts, and record which data informed prompt or rubric changes.

Next: [Metrics and non-determinism](03-metrics-and-nondeterminism.md).

