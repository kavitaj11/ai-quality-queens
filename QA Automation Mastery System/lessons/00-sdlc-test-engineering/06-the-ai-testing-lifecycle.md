# The AI testing lifecycle

[00 · Module topics](../../modules/00-sdlc-test-engineering.md) · [Curriculum index](../../README.md)

**Category** · AI TESTING PROCESS

When the system under test contains a model, the STLC grows new phases: DATA validation before any build, MODEL evaluation against thresholds instead of pass/fail cases, and permanent PRODUCTION monitoring because quality decays without a code change. Requirements become measurable quality bars ('≥90% faithfulness on the golden set'), and the RTM traces requirements to eval datasets, not just test cases.

## Worked reference

````text
THE AI TESTING LIFECYCLE — STLC when a model is inside

  CLASSIC STLC             AI-SYSTEM STLC
  1 requirement analysis   1 requirement → QUALITY BARS
                             'must' → metric + threshold
                             (faithfulness ≥ 0.9, p95 ≤ 2s,
                              attack success ≈ 0%)
  2 test planning          2 planning + DATA STRATEGY
                             golden set source, size,
                             ownership, refresh cadence
  3 test design            3 dataset + rubric design
                             cases → eval datasets;
                             expected results → rubrics/judges
  4 environment setup      4 env + MODEL/PROMPT pinning
                             pin model version, temperature,
                             prompts — or results mean nothing
  5 execution              5 eval runs (tiered: smoke/full)
                             aggregates vs baseline, not
                             single pass/fail
  6 closure                6 closure → CONTINUOUS MONITORING
                             drift, cost, live quality —
                             this phase NEVER closes

NEW ARTIFACTS UNDER VERSION CONTROL
  golden dataset · judge prompts + rubrics · baselines ·
  red-team suite · model/prompt/config manifest

WHAT STAYS THE SAME
  entry/exit criteria · traceability · risk-based priority ·
  the discipline — only the artifacts changed shape.
````

## Exercise

Take the classic STLC from the previous module and redraw it for a support chatbot with a RAG pipeline: what is the deliverable of each phase, and which phases never end?
