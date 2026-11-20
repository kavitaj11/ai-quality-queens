# Framework standards & ISTQB alignment

[08 · Module topics](../../modules/08-frameworks-bdd.md) · [Curriculum index](../../README.md)

**Category** · FOUNDATION

Framework design isn't just personal taste — ISTQB's syllabi (Foundation Level + the Test Automation Engineering extension, TAE) codify vocabulary and a reference architecture worth aligning to: the generic Test Automation Architecture (TAA) layers — test generation, test definition, test execution, test adaptation — map directly onto your test/flow/page/driver layers. Naming things the ISTQB way makes your framework legible to auditors, new hires, and interviewers alike.

## Worked reference

````text
FRAMEWORK STANDARDS & ISTQB ALIGNMENT

  ISTQB'S GENERIC TEST AUTOMATION ARCHITECTURE (TAA)
  (from the CT-TAE — Test Automation Engineering —
  extension syllabus)

  TEST GENERATION      designs test cases/data
  LAYER                (often manual/AI-assisted input)
         │
  TEST DEFINITION      the SCRIPT layer — human- or
  LAYER                DSL-readable test logic
         │             (your flows + BDD features)
  TEST EXECUTION       runs tests, schedules,
  LAYER                collects results
         │             (your runner: pytest/TestNG)
  TEST ADAPTATION       talks to the SUT — driver
  LAYER                calls, API clients
                        (your page objects + core)

  MAPPING YOUR FRAMEWORK TO IT
  test generation   → data factories, BDD Examples
  test definition   → flows, Gherkin features, test
                      method bodies
  test execution    → pytest/TestNG runner + CI wiring
  test adaptation   → page objects, API clients,
                      driver/context factory

  WHY ALIGN AT ALL
  • shared VOCABULARY across teams/orgs — 'adaptation
    layer' means the same thing everywhere ISTQB is
    taught (strong in enterprises, EU, banks — see
    Standards & certifications lesson in QE Process
    Quality)
  • auditors/compliance reviewers in regulated shops
    literally check for TAA-shaped layering
  • interview signal: naming your layers this way
    reads as deliberate architecture, not accident

  WHERE ISTQB STOPS AND YOUR JUDGMENT STARTS
  TAA is a REFERENCE shape, not a prescription —
  it says nothing about page objects vs screenplay,
  BDD vs plain asserts, or AI-augmented seams. Use
  it for the vocabulary and the layer BOUNDARIES;
  make the concrete choices yourself (see the rest
  of this module, and Test Automation System Design)

  THE HONEST CAVEAT
  most product-company frameworks were never built
  reading ISTQB and work fine — treat this as a
  legibility/interview tool, not a compliance
  requirement, unless your org is regulated (see
  Standards & certifications landscape lesson)
````

## Exercise

Map your framework's actual layers (test/flow/page/core) onto ISTQB's TAA four layers. Where do they diverge, and is that divergence deliberate?
