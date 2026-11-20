# Building a framework from scratch

[08 · Module topics](../../modules/08-frameworks-bdd.md) · [Curriculum index](../../README.md)

**Category** · FOUNDATION

The build order that avoids rework: config/env resolution first (everything else depends on it), then a driver/client factory, then one page object end-to-end, then data + parallel execution, then reporting, then CI wiring. Build against ONE real flow before generalizing — a framework designed on paper for hypothetical needs over-engineers; one built to pass one real scenario, then extended, stays lean.

## Worked reference

````text
BUILDING A FRAMEWORK FROM SCRATCH — the build order

  1. CONFIG        env resolution (base URL, creds,
                   browser) — CLI > env var > file.
                   Everything below depends on this.
  2. CORE FACTORY   driver/client creation — ONE
                   place, parallel-safe from day one
                   (ThreadLocal / fixture scope)
  3. ONE PAGE       build the FIRST page object fully
                   — locators, waits, actions — for
                   a single real flow (e.g. login)
  4. ONE TEST       prove the whole stack works
                   end-to-end before adding a second
                   page — resist premature abstraction
  5. DATA LAYER     factories/fixtures — only once
                   you have ≥2 tests needing varied data
  6. PARALLELISM    thread-safety, isolated state —
                   easy to bolt on early, painful late
  7. REPORTING      Allure/JUnit output — needed
                   before this goes near CI
  8. CI WIRING      the suite is now REAL — it gates

  THE ANTI-PATTERN TO AVOID
  designing a 'complete' framework on paper before
  writing test #1 — you'll guess wrong about what's
  needed and build abstractions nothing uses

  THE RULE
  extend the framework only when the SECOND real
  need for a capability appears — the first need is
  usually solved inline, then extracted
````

## Exercise

Starting from an empty repo, build a minimal framework for ONE login+one-assertion test, in this exact order. Note what you were tempted to add early that you deferred.
