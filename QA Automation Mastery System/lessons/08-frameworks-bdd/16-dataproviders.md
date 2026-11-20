# DataProviders

[08 · Module topics](../../modules/08-frameworks-bdd.md) · [Curriculum index](../../README.md)

**Category** · TESTNG (JAVA)

TestNG's answer to parametrize: a method returning Object[][] feeds the test. Combine with external sources (CSV, Excel, DB) for true data-driven suites.

## Worked reference

````text
DATAPROVIDERS — overview

  THE SHAPE
  @DataProvider(name="credentials")
  method returns Object[][] — each inner array is
  ONE test invocation's arguments, in order

  @Test(dataProvider = "credentials")
  test method params MATCH the Object[] positions
  (String user, String pass, boolean expected)

  VS pytest.mark.parametrize
  same concept, different mechanics: TestNG needs a
  separate provider METHOD; pytest inlines the data
  list directly in the decorator — TestNG's indirection
  pays off when data comes from CSV/Excel/DB

  EXTERNAL DATA SOURCES (the real power)
  CSV/Excel   read file → build Object[][] at runtime
              — QA/business can edit data without
              touching Java
  database    query a fixtures table — keeps test
              data centrally versioned

  PARALLEL DATAPROVIDER
  @DataProvider(parallel = true) runs each data row
  on its own thread — combine with parallel="methods"
  in testng.xml for full matrix parallelism

  WHY IT MATTERS
  one test method, N real-world credential/data
  combinations — exactly the data-driven testing
  principle, TestNG's idiom for it
````

## Exercise

Drive a login test from a DataProvider backed by a CSV of 10 credential pairs.
