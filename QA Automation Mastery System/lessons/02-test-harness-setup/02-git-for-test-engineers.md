# Git for test engineers

[02 · Module topics](../../modules/02-test-harness-setup.md) · [Curriculum index](../../README.md)

**Category** · TOOLCHAIN

Branch-per-feature, meaningful commits, and pull-request review discipline. Your test code lives next to app code and follows the same standards.

## Worked reference

````text
# Daily flow (Python suite)
# git checkout -b test/login-suite
# git add tests/test_login.py
# git commit -m "test: add login boundary cases"
# git push -u origin test/login-suite
````

## Exercise

Create a repo, branch, commit a test file, open a PR against main, and squash-merge it.
