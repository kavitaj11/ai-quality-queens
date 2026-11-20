# LLM-assisted test generation

[14 · Module topics](../../modules/14-ai-augmented-qa.md) · [Curriculum index](../../README.md)

**Category** · LLMS IN QA

Use LLMs to draft tests from requirements, generate edge-case data, and summarize failures — with you as the reviewer. The skill is prompting with context (POM, conventions) and ruthlessly auditing output.

## Worked reference

````text
# Prompt pattern that works
# CONTEXT : page object source + naming conventions
# TASK    : "Write pytest tests for <user story>"
# RULES   : "Only use methods that exist in the PO.
#            Include 2 negative cases. No sleeps."
#
# Then audit: does each test fail if the feature breaks?
````

## Exercise

Give an LLM your PageObject + one user story; audit the generated tests — count real bugs it would catch vs. asserting the mock.
