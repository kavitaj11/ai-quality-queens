# Suite XML (testng.xml)

[08 · Module topics](../../modules/08-frameworks-bdd.md) · [Curriculum index](../../README.md)

**Category** · TESTNG (JAVA)

testng.xml is the run controller — it decides WHICH tests run, in what order, with what parallelism and parameters, without touching Java code. You group tests, include/exclude groups, pass <parameter> values (read via @Parameters), set parallel + thread-count, and list listeners. CI runs a specific XML (e.g. smoke.xml on PRs, regression.xml nightly).

## Worked reference

````text
xml
SUITE XML (testng.xml) — overview

  WHAT IT CONTROLS (all WITHOUT touching Java)
  WHICH tests    <groups><include name="smoke"/>
                 <exclude name="slow"/></groups>
  WHAT ORDER     <test> block order, preserve-order
  PARALLELISM    parallel="tests|classes|methods"
                 thread-count="4"
  PARAMETERS     <parameter name="browser" value="chrome"/>
                 read in Java via @Parameters("browser")
  LISTENERS      retry analyzers, custom reporters

  WHY THIS MATTERS FOR CI
  ONE Java codebase, MULTIPLE XML files:
    smoke.xml       PR gate — smoke group, fast
    regression.xml  nightly — full groups
    cross-browser.xml  same tests, browser param
                       swapped per <test> block
  mvn test -DsuiteXmlFile=smoke.xml — no code change
  needed to run a different tier (same idea as
  pytest -m smoke)

  THE MENTAL MODEL
  testng.xml is CONFIGURATION, @Test annotations are
  BEHAVIOR — keeping them separate is what lets one
  suite serve every CI tier via a config swap
````

## Exercise

Write a testng.xml with two <test> blocks (chrome + firefox via a browser parameter), include the smoke group and exclude slow, and run it in parallel with 4 threads.
