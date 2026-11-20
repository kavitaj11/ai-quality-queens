# Testing the agents themselves

[15 · Module topics](../../modules/15-autonomous-qa-pipelines.md) · [Curriculum index](../../README.md)

**Category** · AGENT ORCHESTRATION

An autonomous pipeline is itself an AI app — it needs the whole eval discipline from the AI Testing phase turned on itself. Golden tasks with known-good outcomes, replay suites from logged episodes, agreement metrics against human decisions (triage labels, heal accept-rate), and canary rollouts for prompt/model changes to any agent. If you can't eval an agent, you can't promote it up the autonomy ladder.

## Worked reference

````text
TESTING THE AGENTS — the pipeline is an AI app too

  GOLDEN TASKS (per agent role)
  planner   given this diff → plan MUST include the
            auth suite (known-critical path)
  executor  given this task → reaches goal within
            step budget, no forbidden tools called
  triage    given these 50 failures → clusters ≈ 3,
            labels match human ground truth
  healer    given this broken locator + DOM → heals
            to the semantically-correct element

  REPLAY SUITES — the agent regression test
  log every episode (inputs, tool calls, outputs) →
  curate failures + interesting cases → re-run on
  every agent change. Same idea as 'every prod bug
  becomes a test case', applied to agent behavior.

  METRICS THAT GATE PROMOTION
  heal accept-rate      agent PRs merged unchanged
  triage agreement      vs human labels (kappa)
  plan recall           critical suites included?
  false-quarantine rate healthy tests wrongly benched
  cost per episode      tokens + minutes — autonomy
                        that costs more than the
                        humans it replaced is a demo

  CHANGE MANAGEMENT
  agent prompt/model changes go through CI like any
  code: replay suite + golden tasks + canary (agent
  v2 shadows v1 on live traffic, humans compare) —
  see Evals in CI/CD; it's the same machinery.
````

## Exercise

Build a 10-episode replay suite for a triage agent from real logged failures: given the same inputs, does today's agent reach the same (correct) verdicts?
