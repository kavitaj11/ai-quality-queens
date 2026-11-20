# AI test generation

[01 · Module topics](../../modules/01-foundations.md) · [Curriculum index](../../README.md)

**Category** · AI-ASSISTED QA

LLMs draft tests from specs, recordings, and prose — you review them like any PR. Generation is cheap; the discipline is in review, quarantine, and keeping locators semantic.

## Worked reference

````text
AI TEST GENERATION — theory

WHAT IT IS
  Use an LLM to DRAFT tests from a spec, a recording, a user
  story, or prose. Generation is cheap and fast; the value is
  in what you do AFTER.

WHERE IT HELPS
  • scaffolding tests for a new endpoint from its OpenAPI spec
  • turning a codegen recording into a page-object test
  • generating edge-case data / boundary inputs
  • drafting negative and error-path cases you'd forget

THE GOLDEN RULE
  AI generates a DRAFT — a human reviews it like any pull request.
  A generated test that merely "asserts the mock" catches nothing.

DISCIPLINE
  • prompt WITH context: page objects, naming conventions, rules
    ("only use methods that exist", "2 negative cases", "no sleeps")
  • audit each test: does it FAIL when the feature breaks?
  • quarantine generated tests until N consecutive green runs
  • keep locators semantic (getByRole/testid), not brittle CSS

  → Working generation code (OpenAPI → pytest, prompt patterns)
    lives in the AI-Augmented QA phase.
````

## Exercise

Generate tests for one API endpoint from its OpenAPI spec using an LLM. Review, fix, and count what you had to change.
