# Annotations & lifecycle

[08 · Module topics](../../modules/08-frameworks-bdd.md) · [Curriculum index](../../README.md)

**Category** · TESTNG (JAVA)

TestNG's execution model is driven by annotations. Lifecycle runs outer→in then in→out: @BeforeSuite → @BeforeTest → @BeforeClass → @BeforeMethod → @Test → @AfterMethod → … → @AfterSuite. @Test carries the metadata that shapes a run: groups, priority, dependsOnMethods, enabled, timeOut, and expectedExceptions. Know this cold for Java-stack interviews.

## Worked reference

````text
TESTNG ANNOTATIONS & LIFECYCLE — overview

  THE LIFECYCLE LADDER (broad → narrow → unwinds)
  @BeforeSuite   once per testng.xml run
  @BeforeTest      once per <test> block
  @BeforeClass       once per class
  @BeforeMethod        before EVERY @Test method
  @Test                  the test itself
  @AfterMethod         after every @Test (alwaysRun!)
  @AfterClass        once per class
  @AfterTest       once per <test> block
  @AfterSuite    once per run

  @Test METADATA — the run-shaping attributes
  groups              tag for selection (smoke,
                      regression) — the Java
                      equivalent of pytest markers
  priority            lower runs first (default 0)
  dependsOnMethods    skip (not fail) if the
                      dependency failed
  timeOut             fail if it exceeds N ms
  expectedExceptions  test passes only if this
                      exception is thrown
  enabled = false     temporarily switch off

  alwaysRun = true — the safety-net flag
  put it on @AfterMethod/@AfterClass so cleanup
  (browser.quit(), cookie reset) runs even when a
  @Test or an earlier @Before fails — skip it and
  failures leak state into the next test

  WHY MEMORIZE THIS
  interview canon at Java shops; also the exact
  mental model TestNG's testng.xml and
  DataProviders (next lessons) build on
````

## Exercise

Write a BaseTest with the full before/after ladder, then a test class using groups, priority, and dependsOnMethods across three @Test methods.
