# 5. TestNG: Data-Driven Tests, Groups and Parallel Runs

## Annotations and their order

```
@BeforeSuite → @BeforeTest → @BeforeClass → @BeforeMethod → @Test → @AfterMethod → @AfterClass → @AfterTest → @AfterSuite
```

In a class hierarchy, the parent's `@BeforeMethod` runs before the child's. `SearchTests` relies on this: `BaseTest.startBrowser()` creates the driver, then `SearchTests.logInThroughApi()` uses it. `alwaysRun = true` on setup and teardown makes them run even when you filter by groups.

## Data-driven tests with @DataProvider

One test method, four rows of data, four test results (from `LoginTests`):

```java
@DataProvider(name = "invalidLogins")
public Object[][] invalidLogins() {
    return new Object[][] {
        {"alice", "wrong-password", "Invalid username or password."},
        {"", "Passw0rd!", "Username and password are required."},
        {"alice", "", "Username and password are required."},
        {"locked", "Passw0rd!", "This account is locked. Contact support."},
    };
}

@Test(dataProvider = "invalidLogins", groups = "regression")
public void invalidLoginShowsError(String username, String password, String expectedError) { ... }
```

The report names each row, for example `invalidLoginShowsError[locked, Passw0rd!, This account is locked. Contact support.](4)`, so you know exactly which data failed. Data can also come from CSV, Excel or JSON files read inside the provider.

## Groups and suites

Tests are tagged with groups, `@Test(groups = {"smoke", "regression"})`, and suite files choose what to run:

| Suite | Runs | Command | Result here |
|---|---|---|---|
| `testng.xml` | All TestNG tests + Cucumber | `mvn test` | 14 tests, all pass |
| `testng-smoke.xml` | `smoke` group only | `mvn test -Dsuite=testng-smoke.xml` | 3 tests, all pass |
| `testng-parallel.xml` | All TestNG tests, 3 threads | `mvn test -Dsuite=testng-parallel.xml` | 10 tests, all pass (3 runs) |

## Parallel execution

```xml
<suite name="K11 Shop - parallel" parallel="methods" thread-count="3" data-provider-thread-count="2">
```

| `parallel=` | What runs at the same time |
|---|---|
| `methods` | Test methods (most parallelism, needs the most care) |
| `classes` | Whole classes; methods within a class stay sequential |
| `tests` | `<test>` blocks in the suite file |

Prerequisites: a ThreadLocal driver, no shared mutable fields (Chapter 4's bug), and independent test data. The practice shop gives each login its own session and cart, so tests can't see each other's data.

## Reports and listeners

- Surefire writes JUnit-style XML to `target/surefire-reports/` that every CI system can read.
- TestNG writes `target/surefire-reports/index.html` and `emailable-report.html`.
- **Listeners** (`ITestListener`, `ISuiteListener`) hook into events; common uses are screenshots, logging and pushing results to a dashboard. This project uses an `@AfterMethod` with `ITestResult` for screenshots, which is simpler than a listener for a single concern.
- **Retry:** `IRetryAnalyzer` can re-run failed tests. Use it only for genuinely environmental failures, and report retried tests separately, or retries will hide real flakiness.

## Assertions: AssertJ

```java
assertThat(products.productNames()).containsExactlyInAnyOrder("Desk Lamp", "Smart Lamp");
assertThat(cart.total()).isEqualByComparingTo("40.25");
```

AssertJ gives readable chains and clear failure messages. For money, compare `BigDecimal` with `isEqualByComparingTo` (so `40.25` equals `40.250`), never `double`. TestNG's `SoftAssert` (or AssertJ's `SoftAssertions`) collects several failures in one test instead of stopping at the first.

## Review questions

**What is a DataProvider?**
A method returning `Object[][]` (or an iterator) that feeds rows of parameters to a test method; each row runs as its own test.

**How do you run only smoke tests?**
Tag tests with groups and include the group in a suite file or with `-Dgroups=smoke`.

**parallel="methods" vs "classes"?**
Methods runs individual test methods concurrently: fastest, but every method must be independent and thread-safe. Classes runs whole classes concurrently and keeps methods inside a class sequential.

**Hard vs soft assertions?**
A hard assertion stops the test at the first failure. A soft assertion records failures and reports them all at the end (`assertAll()`). Use soft assertions for checking many fields on one page.

**How do you rerun failed tests?**
`testng-failed.xml` is generated after a run, or use `IRetryAnalyzer`, but always investigate why they failed.

Next: [6. BDD with Cucumber](06-bdd-with-cucumber.md)
