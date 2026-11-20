# The detect–fix–learn loop

[15 · Module topics](../../modules/15-autonomous-qa-pipelines.md) · [Curriculum index](../../README.md)

**Category** · PIPELINE ARCHITECTURE

What makes a pipeline autonomous rather than just automated: a memory. DETECT (failure lands, enriched with context), FIX (agent consults historical records — past failures like this one, which fixes were accepted, which were rejected — before proposing action), LEARN (the human verdict on each proposal is written back to memory). Over cycles, proposals get better and confidence scores rise; only proposals below the confidence bar route to human-in-the-loop review. The loop is how agents earn the autonomy ladder's promotions with evidence instead of hope.

## Worked reference

````text
DETECT → FIX → LEARN — the loop with a memory

            ┌────────────────────────────────┐
            │      HISTORICAL MEMORY         │
            │ past failures · fixes proposed │
            │ · accepted/rejected verdicts · │
            │ flake history · heal outcomes  │
            └──────▲──────────────┬──────────┘
        LEARN:     │              │  consult BEFORE
        write back │              │  proposing
        the verdict│              ▼
  ┌────────┐   ┌───┴────┐    ┌─────────┐
  │ DETECT ├──▶│ triage │───▶│   FIX   │
  │ failure│   │ + match│    │ propose │
  │ + ctx  │   │ history│    │ action  │
  └────────┘   └────────┘    └────┬────┘
                                  │ confidence?
                     high ────────┼──────── low
                      ▼                       ▼
              auto-apply (L2+:         HUMAN-IN-THE-
              PR / quarantine          LOOP: review
              with expiry)             queue → verdict
                      │                       │
                      └──── verdict ──────────┘
                        (accept / reject / edit)
                             = training data

  WHAT THE MEMORY HOLDS (per episode)
  failure signature · env + timing context ·
  matched precedents · proposed fix · confidence ·
  human verdict · did the fix STICK (no recurrence
  in 30 days)?

  HOW LEARNING ACTUALLY HAPPENS (no fine-tuning
  needed)
  • retrieval: 'failures like this' precedents go
    into the agent's context (RAG over memory)
  • calibration: accept-rate per failure class sets
    the confidence bar — classes with 95% accepts
    graduate to auto-apply; 60% classes stay human-
    gated
  • rejected proposals are gold: they become replay
    -suite cases (see Testing the agents)

  THE RATCHET
  every cycle narrows the human queue to the genuinely
  novel — humans review the interesting 10%, the
  memory handles the recurring 90%. That narrowing,
  measured, IS the autonomy ladder promotion evidence.
````

## Exercise

For locator failures in your suite, define the memory record: what fields would let an agent find 'failures like this one', and what does an accepted vs rejected fix teach it?
