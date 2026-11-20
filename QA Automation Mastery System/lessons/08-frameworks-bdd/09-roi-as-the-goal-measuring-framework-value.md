# ROI as the goal — measuring framework value

[08 · Module topics](../../modules/08-frameworks-bdd.md) · [Curriculum index](../../README.md)

**Category** · FOUNDATION

A framework is judged on return, not elegance: hours saved (manual runs avoided × frequency), defects caught pre-production (cost of a prod bug vs a caught one), and time-to-signal (how fast a PR gets a verdict) — against build + maintenance cost. Track these numbers and revisit them; a framework that costs more to maintain than it saves has negative ROI regardless of how well-engineered it is.

## Worked reference

````text
ROI AS THE GOAL — the framework's scorecard

  THE FORMULA
  ROI = (VALUE DELIVERED) − (COST TO BUILD + MAINTAIN)

  VALUE DELIVERED (what to actually measure)
  hours saved       manual run time × frequency ×
                    avoided runs
  defects caught    bugs found pre-prod × (cost of a
  pre-production    prod incident − cost of a caught one)
  time-to-signal    lead time from commit to pass/fail
                    verdict (feeds DORA lead-time)
  deploy confidence enables faster release cadence —
                    hard to measure directly, shows up
                    in deploy frequency

  COST (the side people forget to track)
  build time · maintenance hours (locator fixes,
  flake chasing) · infra spend (cloud runners,
  device farms) · onboarding time for new engineers

  THE UNCOMFORTABLE QUESTION TO ASK QUARTERLY
  is maintenance cost trending UP faster than value
  delivered? That's a framework in ROI decline —
  usually from unchecked flake, over-automation of
  low-value cases, or missing the 'what NOT to
  automate' discipline (lesson 2)

  ROI, NOT ELEGANCE, IS THE NORTH STAR
  a beautifully layered framework nobody trusts
  (too flaky) has LOW roi; a scrappier one that
  reliably blocks bad merges has HIGH roi. Optimize
  for the number, not the architecture diagram.
````

## Exercise

Estimate your framework's ROI: hours/month saved vs manual, hours/month spent on maintenance + flake-chasing. Which number is bigger, and by how much?
