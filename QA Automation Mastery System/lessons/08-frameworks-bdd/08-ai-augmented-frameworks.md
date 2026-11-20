# AI-augmented frameworks

[08 · Module topics](../../modules/08-frameworks-bdd.md) · [Curriculum index](../../README.md)

**Category** · FOUNDATION

Where AI plugs into framework design itself (deep dive: AI-Augmented QA phase): LLM-assisted test/step generation from specs, self-healing locators as a framework-level concern (not bolted on later), AI-assisted flake triage built into the reporting layer, and MCP-exposed framework internals so agents can run/query your suite. Design the seams for this NOW — a retrofit is much more expensive than a framework built extensible from day one.

## Worked reference

````text
AI-AUGMENTED FRAMEWORKS — designing the seams

  WHERE AI PLUGS IN (mapped to the build order)
  CONFIG/CORE      expose typed, documented interfaces
                   — agents (and LLMs) read these first
  PAGE OBJECTS      semantic locators (getByRole,
                   data-testid) — the substrate self-
                   healing and LLM generation both need
  DATA LAYER        LLM-generated edge-case data —
                   quarantined until reviewed (see AI
                   Testing phase)
  REPORTING          structured, machine-readable output
                   → flake clustering + LLM triage read
                   this layer, not raw logs
  CI WIRING          the seam for an MCP surface:
                   run_suite() / get_failure_history()
                   — wrap what already exists

  DESIGN PRINCIPLE: BUILD FOR BOTH READERS
  every layer should be readable by a HUMAN and
  QUERYABLE by an AGENT — structured over stringly-
  typed, semantic over positional, documented over
  implicit

  THE RETROFIT COST ARGUMENT
  adding semantic locators after 500 tests use CSS
  selectors is a rewrite; choosing them on day one
  (previous lesson's step 3) is free

  WHAT NOT TO DO YET
  don't build agentic pipelines before the framework
  itself is solid — see Autonomous QA Pipelines phase
  for the full system; this lesson is about making
  THIS framework ready to plug into it later
````

## Exercise

Take your framework's build order (previous lesson). Mark which step is the right seam for: test generation, self-healing, flake triage, and an MCP surface.
