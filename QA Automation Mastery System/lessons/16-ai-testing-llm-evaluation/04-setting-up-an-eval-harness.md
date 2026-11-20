# Setting up an eval harness

[16 · Module topics](../../modules/16-ai-testing-llm-evaluation.md) · [Curriculum index](../../README.md)

**Category** · FOUNDATION

The minimum viable eval setup: a golden dataset (versioned cases: input, context, expected/rubric), a runner that executes cases against the pipeline, graders (assertions first, judges where needed), and a report (per-case scores, aggregates, diff vs baseline). Start with 20 cases from real usage, grow with every production failure — each bug becomes a case, exactly like regression tests.

## Worked reference

````text
EVAL HARNESS — minimum viable setup

  evals/
  ├── dataset/golden.jsonl     versioned WITH prompts
  │     {"id": "c01",
  │      "input": "...",
  │      "context": "...",      for RAG faithfulness
  │      "expected": "...",     or rubric ref
  │      "tags": ["happy"|"edge"|"injection"|...]}
  ├── run.py                   pipeline runner
  ├── graders/                 assertions → judges
  └── reports/                 scores + baseline diff

  RUN FLOW
  for case in dataset:
      output = pipeline(case.input)     # REAL pipeline,
      scores = grade(output, case)      # not raw model
  aggregate → compare vs baseline → report

DATASET DESIGN (the actual hard part)
  • source from REAL usage + production failures —
    every prod bug becomes a case (regression logic)
  • cover the failure-mode taxonomy, not just happy
    paths: tag cases by mode so gaps are visible
  • 20 good cases beat 500 synthetic ones; grow
    incrementally
  • version in git NEXT TO the prompts they test —
    prompt change + dataset change reviewed together

GRADER LADDER (cheapest that works)
  1. assertions: schema, contains, regex, length
  2. reference metrics: similarity vs expected
  3. LLM-as-judge with the anchored rubric
     (calibrate: judge vs human labels on 20 cases)

REPORT MUST SHOW
  per-case scores · aggregates by tag · regressions
  vs baseline · judge transcripts for failures
  (this harness is what CI runs — see Evals in CI/CD)
````

## Exercise

Build a 20-case golden dataset for one AI feature: 12 happy path, 4 edge, 4 failure-mode probes. Version it in git next to the prompts it tests.
