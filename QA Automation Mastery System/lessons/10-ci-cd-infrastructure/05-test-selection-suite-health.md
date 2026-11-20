# Test selection & suite health

[10 · Module topics](../../modules/10-ci-cd-infrastructure.md) · [Curriculum index](../../README.md)

**Category** · QUALITY AT SCALE

Impact-based selection, smoke/regression tiers, runtime budgets, and deleting dead tests. A 4-hour suite is a process failure, not a hardware problem.

## Worked reference

````text
# Tiering policy
# PR gate   : smoke, < 10 min, zero known-flaky
# Merge     : regression subset, < 30 min
# Nightly   : full matrix, all browsers
# Weekly    : perf + a11y + long-haul journeys

# pytest -m smoke --timeout=60 -n auto
````

## Exercise

Set a 10-minute PR budget. Move everything that doesn't fit to nightly and justify each survivor.
