# Open-source frameworks: build vs adopt

[08 · Module topics](../../modules/08-frameworks-bdd.md) · [Curriculum index](../../README.md)

**Category** · FOUNDATION

Rarely start from zero: Selenium/Playwright/Cypress (engines), pytest/TestNG/JUnit/Jest (runners), Cucumber/SpecFlow/behave (BDD), Serenity/Robot Framework (opinionated all-in-ones). The real decision is BUILD (thin layer on an engine + runner, full control) vs ADOPT (an opinionated framework like Robot/Serenity, faster start, less flexibility) vs HYBRID (most common: adopt the engine+runner, build your own thin layers on top).

## Worked reference

````text
OPEN-SOURCE FRAMEWORKS — build vs adopt vs hybrid

  THE BUILDING BLOCKS YOU RARELY WRITE YOURSELF
  ENGINES     Selenium · Playwright · Cypress
  RUNNERS     pytest · TestNG · JUnit · Jest
  BDD TOOLS   Cucumber · SpecFlow · behave
  ALL-IN-ONE  Robot Framework (keyword-driven, low-
              code) · Serenity (BDD + rich reporting
              layered on Selenium/Playwright)

  BUILD (thin layer, full control)
  engine + runner + YOUR page objects/flows/config —
  most flexible, most upkeep, what this course
  teaches by default

  ADOPT (opinionated, less code, less control)
  Robot Framework: keyword tables, non-engineers can
  write cases — but you inherit its conventions and
  plugin ecosystem limits

  HYBRID (the common real-world answer)
  adopt the ENGINE (Playwright) + RUNNER (pytest) +
  BDD TOOL (Cucumber) — build your OWN page objects,
  flows, config, and reporting glue on top

  DECISION LENS
  • team size + skill: low-code (Robot) helps small/
    less-technical teams; hybrid suits SDETs
  • how unusual are your needs? generic CRUD app →
    adopt more; unusual domain → build more
  • exit cost: opinionated tools are harder to leave
    — same lock-in question as cloud platforms

  THE TRAP
  reinventing a driver factory or wait utility that
  Playwright/pytest already solved — check upstream
  before building
````

## Exercise

For your stack, list what you'd adopt (engine, runner, BDD tool) vs build (page objects, flows, config, reporting glue). Justify the split.
