# Flake detection & AI triage

[01 · Module topics](../../modules/01-foundations.md) · [Curriculum index](../../README.md)

**Category** · AI-ASSISTED QA

A test that passes on retry is flaky, not fixed. Detect flakes mechanically (retries + report mining), cluster failures by error signature, and hand the clusters to an LLM for root-cause triage.

## Worked reference

````text
FLAKE DETECTION & AI TRIAGE — theory

THE PROBLEM
  A test that passes on retry is FLAKY, not fixed. Flakiness
  erodes trust until people ignore red builds entirely.

DETECT MECHANICALLY (not by gut feel)
  • enable retries; any test that fails-then-passes is a flake
  • mine the test report for pass-on-retry cases
  • track flake RATE per test over time (history)

TRIAGE WITH AI
  • normalize error messages (strip numbers/ids) into a signature
  • CLUSTER failures by signature — many tests, one root cause
  • hand each cluster to an LLM: "same cause? race, env, or app bug?"

COMMON ROOT CAUSES
  race conditions (missing waits) · shared state / test order ·
  time & timezone · network/env instability · animations

FIX, DON'T HIDE
  retries are a safety net, not a cure — quarantine, find the
  real cause, and only then return the test to the main suite.

  → Report-mining + clustering code lives in the AI-Augmented QA phase.
````

## Exercise

Mine your last test report: list every pass-on-retry test and cluster the failures by normalized error message.
