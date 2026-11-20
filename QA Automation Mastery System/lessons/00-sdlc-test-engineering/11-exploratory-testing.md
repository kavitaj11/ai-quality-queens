# Exploratory testing

[00 · Module topics](../../modules/00-sdlc-test-engineering.md) · [Curriculum index](../../README.md)

**Category** · QE PROCESS QUALITY

Structured discovery — not unscripted clicking. Session-Based Test Management gives it rigor: a CHARTER (mission for this session), a timebox (60-120 min), notes as you go, and a debrief. Heuristics like SFDIPOT (Structure, Function, Data, Interfaces, Platform, Operations, Time) and tours (money tour, garbage-collector tour) steer attention. Best bug-per-hour ratio in QA when done well.

## Worked reference

````text
EXPLORATORY TESTING — structured discovery

SESSION-BASED TEST MANAGEMENT (SBTM)
  CHARTER   one-sentence mission:
            'explore <area> with <resources>
             to discover <kind of information>'
  TIMEBOX   60-120 min, uninterrupted
  NOTES     what you tried, what surprised you,
            bugs, questions, new charter ideas
  DEBRIEF   findings → bugs filed, charters queued,
            cases worth automating

HEURISTICS — where to point your attention
  SFDIPOT   Structure · Function · Data · Interfaces ·
            Platform · Operations · Time
  TOURS     money tour (revenue paths) · garbage
            collector (every screen, shallow) ·
            saboteur (starve it: kill network, fill
            disk) · back-alley (least-used features)
  DATA ATTACKS  empty · huge · unicode · negative ·
            duplicates · concurrent edits

EXPLORATORY vs SCRIPTED
  scripted verifies what you EXPECTED to be true;
  exploratory discovers what you didn't know to ask.
  New feature? explore FIRST, then script what matters.

FEEDS THE AUTOMATION PIPELINE
  every exploratory find is a candidate regression
  test — the discovery engine for your scripted suite.
````

## Exercise

Run a 60-minute session with the charter 'explore checkout coupon handling for money-losing bugs'; produce session notes, 3 findings, and the follow-up charters you'd run next.
