# Usability testing

[11 · Module topics](../../modules/11-non-functional-testing.md) · [Curriculum index](../../README.md)

**Category** · OTHER NON-FUNCTIONAL TYPES

Usability testing measures whether real users can accomplish goals efficiently and without confusion. It's mostly qualitative and human-run (task-based sessions, think-aloud, surveys like SUS), but QA supports it: instrument flows, measure task success/time/error rates, and catch UX regressions with analytics and session replay. Not the same as accessibility.

## Worked reference

````text
USABILITY TESTING

WHAT IT ANSWERS
  can real users achieve their goals easily, quickly, without
  confusion or errors? (subjective + behavioral, not pass/fail)

METHODS (mostly human)
  moderated task sessions · think-aloud protocol · unmoderated
  remote tests · A/B tests · surveys: SUS (System Usability
  Scale), NPS · card sorting · first-click / tree testing

METRICS
  task SUCCESS rate · time-on-task · error rate · SUS score ·
  drop-off / funnel completion · rage-clicks

WHERE QA HELPS
  instrument flows (analytics events) · measure funnels ·
  session replay (FullStory/Hotjar) to see friction ·
  guard against UX regressions in the automated E2E suite
````

## Exercise

Define 3 key user tasks, then specify what you'd measure for each (success rate, time-on-task, error count) and how you'd capture it.
