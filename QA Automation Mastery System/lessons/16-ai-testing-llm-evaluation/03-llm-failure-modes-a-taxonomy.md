# LLM failure modes: a taxonomy

[16 · Module topics](../../modules/16-ai-testing-llm-evaluation.md) · [Curriculum index](../../README.md)

**Category** · FOUNDATION

Name the ways it breaks and you can test for them. Hallucination (confident fabrication), instruction drift (ignores constraints), context failures (lost-in-the-middle, truncation), format breakage, refusal errors in both directions (over- and under-refusal), sycophancy (agrees with wrong premises), prompt-injection susceptibility, and regression-on-update (provider model changes under you). Every mode maps to a test type.

## Worked reference

````text
LLM FAILURE MODES — taxonomy → test type

  MODE                 LOOKS LIKE              TEST WITH
  hallucination        confident wrong facts,  faithfulness judge
                       invented citations      vs golden context
  instruction drift    ignores 'max 3 items',  deterministic
                       adds forbidden content  assertions on rules
  context failure      forgets mid-convo,      long-context probes:
                       lost-in-the-middle      plant fact at pos 0/
                       (middle chunks ignored) mid/end, ask for it
  format breakage      invalid JSON, markdown  schema validation
                       when asked for plain    on every output
  over-refusal         declines legitimate     benign-but-edgy
                       requests                case set
  under-refusal        complies with harmful   red-team suite
                       requests                (see Red-teaming)
  sycophancy           agrees with user's      wrong-premise
                       false premise           question set
  injection            obeys instructions      adversarial inputs
                       embedded in data/docs   in retrieved chunks
  update regression    provider ships new      pinned model ids +
                       model, behavior shifts  full eval on upgrade

TWO HABITS THE TAXONOMY BUYS YOU
  1. eval set COVERAGE: golden cases per mode, not
     just happy paths — a suite with no injection or
     wrong-premise cases isn't testing the model,
     it's testing the demo
  2. TRIAGE vocabulary: 'hallucination' vs 'context
     truncation' route to different fixes (data vs
     prompt vs model)
````

## Exercise

Take 10 real failures from an AI feature (or generate them) and classify each against this taxonomy. Which mode dominates? That's where your eval set needs cases.
