# Autonomous testing

[00 · Module topics](../../modules/00-sdlc-test-engineering.md) · [Curriculum index](../../README.md)

**Category** · QE PROCESS QUALITY

The autonomy ladder: scripted automation (human writes, machine repeats) → assisted (self-healing locators, AI-generated drafts — human still authors intent) → autonomous (agents explore, generate, and adapt tests toward a goal with humans reviewing findings). Each rung trades authorship for oversight. The mature position: autonomy for DISCOVERY, determinism for GATES.

## Worked reference

````text
AUTONOMOUS TESTING — the autonomy ladder

  L0 MANUAL       human designs + executes
  L1 SCRIPTED     human writes, machine repeats
                  (your Selenium/Playwright suites)
  L2 ASSISTED     machine helps author + maintain:
                  self-healing locators · AI-drafted
                  tests · flake triage (Phase 12)
                  — human still owns INTENT
  L3 AUTONOMOUS   agent pursues a GOAL: explores,
                  generates checks, adapts to UI
                  change, files findings (agentic QA)
  L4 SELF-DIRECTED agent decides WHAT to test from
                  risk signals (diffs, incidents,
                  traffic) — emerging, treat claims
                  with vendor-demo skepticism

THE TRADE AT EVERY RUNG
  ↑ autonomy = ↓ authorship, ↑ oversight burden
  an agent's 'pass' is an OPINION until a human
  or a deterministic check confirms it

THE MATURE SPLIT
  GATES (merge/release)  deterministic, scripted,
                         repeatable — L1/L2
  DISCOVERY              autonomous exploration,
                         findings → reviewed →
                         promoted to scripted — L3

ADOPTION ORDER THAT WORKS
  stabilize L1 → add L2 healing/generation with
  review discipline → pilot L3 on staging with
  guardrails (see Agentic AI QA) → measure escaped
  bugs, not demo sizzle
````

## Exercise

Place 6 tools you know on the autonomy ladder. For your product, decide which rung gates merges and which runs as a discovery layer — and write the review rule for agent findings.
