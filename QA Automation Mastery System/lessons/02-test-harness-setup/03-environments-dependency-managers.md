# Environments & dependency managers

[02 · Module topics](../../modules/02-test-harness-setup.md) · [Curriculum index](../../README.md)

**Category** · TOOLCHAIN

venv + pip for Python, npm for JavaScript/TypeScript, Maven/Gradle for Java. Pinned dependencies are what make your suite reproducible on CI.

## Worked reference

````text
# Python
python -m venv .venv && source .venv/bin/activate
pip install pytest selenium playwright
pip freeze > requirements.txt
````

## Exercise

Set up all four: a Python venv with pytest, npm projects (JS and TS) with @playwright/test, and a Maven pom with selenium-java.
