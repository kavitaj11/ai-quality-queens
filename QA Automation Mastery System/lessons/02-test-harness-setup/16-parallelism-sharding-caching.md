# Parallelism, sharding & caching

[02 · Module topics](../../modules/02-test-harness-setup.md) · [Curriculum index](../../README.md)

**Category** · CI/CD FOR TEST SUITES

Suites that take 40 minutes get skipped. Parallelize inside a machine (workers/threads), shard across machines, and cache dependencies. Hard requirement: tests must be independent — unique users, no fixed ports, no shared state.

## Worked reference

````text
# pytest-xdist: use every core
#   pip install pytest-xdist
#   pytest -n auto

# Independence rules that make -n auto safe:
#   unique emails (uuid), no fixed ports,
#   no test ordering, per-test data via fixtures

# Cache pip deps in CI:
#   - uses: actions/cache@v4
#     with:
#       path: ~/.cache/pip
#       key: pip-${{ hashFiles('requirements.txt') }}
````

## Exercise

Take a serial suite and run it with 4 workers. Fix every collision you find (shared users, fixed emails, order dependencies).
