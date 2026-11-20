# Dependency & build management

[06 · Module topics](../../modules/06-selenium.md) · [Curriculum index](../../README.md)

**Category** · STRUCTURE & SCALE

Every framework needs its libraries declared, versioned, and reproducibly installed — that's the build/dependency manager. Java uses Maven (pom.xml) or Gradle; Python uses pip + requirements.txt or Poetry; JS uses npm/pnpm (package.json + lockfile). Pin versions and commit the lockfile so CI installs the exact same tree the author tested.

## Worked reference

````text
# requirements.txt — pin exact versions (pip)
selenium==4.25.0
pytest==8.3.3
pytest-xdist==3.6.1          # parallel
pytest-rerunfailures==14.0   # retries
allure-pytest==2.13.5        # reporting

#   python -m venv .venv && source .venv/bin/activate
#   pip install -r requirements.txt

# ── Poetry (modern; lockfile = reproducible tree) ──
# pyproject.toml
#   [tool.poetry.group.dev.dependencies]
#   selenium = "4.25.0"
#   pytest   = "8.3.3"
#   poetry install     # installs from poetry.lock, exact versions

# ── JS/TS: package.json + package-lock.json ──
#   "devDependencies": { "selenium-webdriver": "4.25.0",
#                        "mocha": "10.7.3" }
#   npm ci     # installs the EXACT locked tree (CI-safe)

# RULE: commit the lockfile so CI == your machine.
````

## Exercise

Declare Selenium + your runner + a reporter in the right manifest for your language, pin versions, and reproduce the install from the lockfile in CI.
