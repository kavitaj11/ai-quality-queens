# Test strategy vs test plan

[00 · Module topics](../../modules/00-sdlc-test-engineering.md) · [Curriculum index](../../README.md)

**Category** · TEST ENGINEERING & PROCESS

Two documents interviewers love to separate. A test STRATEGY is the high-level, long-lived approach for an org/product (scope, levels, tools, environments, risk approach). A test PLAN is project/release-specific (what/when/who, entry-exit criteria, schedule, deliverables). Strategy is the philosophy; plan is the execution for this release.

## Worked reference

````text
TEST STRATEGY  vs  TEST PLAN

TEST STRATEGY  (org / product level · long-lived · stable)
  • overall approach & objectives; quality goals
  • test LEVELS (unit/integration/system/acceptance) & TYPES
  • automation approach, tools, frameworks, environments
  • risk-based priorities; entry/exit philosophy
  • roles, standards, metrics -- the "how we test, always"

TEST PLAN  (project / release level · specific · changes often)
  • SCOPE: what is in / out of THIS release
  • test items & features to test
  • ENTRY criteria (build deployed, data ready) and
    EXIT criteria (X% pass, 0 criticals, coverage met)
  • schedule, estimates, environments, test data
  • deliverables (cases, reports), roles & responsibilities
  • risks & mitigations, suspension/resumption rules

IEEE-829 PLAN OUTLINE (the checklist)
  scope · items · features in/out · approach · pass-fail
  criteria · suspension · deliverables · schedule · risks · approvals

RULE OF THUMB
  strategy answers: how does this company test?
  plan answers: how do we test release 4.2, by when, and who?
````

## Exercise

Draft a one-page test plan for a checkout feature: scope, in/out, test types, entry/exit criteria, risks, and who owns what.
