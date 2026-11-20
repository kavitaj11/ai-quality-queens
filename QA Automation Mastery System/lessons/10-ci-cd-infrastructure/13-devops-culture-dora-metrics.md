# DevOps culture & DORA metrics

[10 · Module topics](../../modules/10-ci-cd-infrastructure.md) · [Curriculum index](../../README.md)

**Category** · DEVOPS FOR QA

DevOps collapsed the wall between dev and ops; quality engineering lives in that collapse: QA shifts from gatekeeper at the end to enabler of safe speed throughout. The DORA four — deployment frequency, lead time, change failure rate, MTTR — are the metrics that prove it: good automation RAISES deploy frequency while HOLDING failure rate down. If your quality process lowers deployment frequency without lowering failure rate, it's friction, not quality.

## Worked reference

````text
DEVOPS & QA — from gatekeeper to enabler

  OLD WALL                 DEVOPS
  dev → throw → QA →       one team owns build,
  throw → ops              test, deploy, operate
  QA = final gate          quality built into the
  (bottleneck, blamed)     pipeline; QA engineers
                           the SYSTEM of safety

  DORA METRICS — the four that matter
  DEPLOY FREQUENCY   how often you ship
  LEAD TIME          commit → production
  CHANGE FAILURE %   deploys causing incidents
  MTTR               time to recover

  elite teams: deploy on demand · lead time < 1 day
  · CFR < 5% · MTTR < 1 hr — SPEED AND SAFETY
  CORRELATE; teams fast at shipping are also safer

  WHERE AUTOMATION MOVES THE NEEDLE
  fast reliable PR gates      → lead time ↓
  trusted suite (no manual    → deploy frequency ↑
  regression pass)
  contract + component depth  → change failure ↓
  synthetic monitors + smoke  → MTTR ↓ (detect fast,
  on deploy                     verify the fix fast)

  THE LITMUS TEST
  a quality practice that slows deploys WITHOUT
  reducing failures is friction wearing a quality
  costume: 3-day manual regression cycles, sign-off
  meetings, suites so flaky nobody reruns them.
  Measure, then delete the friction.

  QA'S DEVOPS JOB DESCRIPTION
  own the pipeline's quality gates · make tests a
  platform others build on · treat test infra with
  ops discipline (monitoring, on-call for the
  suite) · report in DORA terms leadership reads
````

## Exercise

Estimate your team's four DORA metrics. Which one would better test automation move first — and which quality practice of yours is actually friction?
