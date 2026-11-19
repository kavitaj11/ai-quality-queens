# 6. Cucumber BDD and Data-Driven Tests

## Feature files (Gherkin)

Three features in `src/test/resources/features/`, tagged by layer: `@ui`, `@api`, `@db`, `@smoke`, `@regression`, `@security`.

A **DataTable** builds the cart (from `orders_api.feature`):

```gherkin
@smoke
Scenario: API checkout returns a valid order that matches the database
  Given I am signed in to the API as "api-buyer"
  And my cart contains:
    | product    | quantity |
    | Blue Mug   | 2        |
    | Eco Bottle | 1        |
  When I check out through the API
  Then the API responds with status 201
  And the response matches the "order" schema
  And the order total is "40.25"
  And every order line total is quantity times unit price
  And the database total equals the sum of the order lines
  And the order has exactly 1 "CREATED" audit entry
```

A **Scenario Outline** makes it data-driven (from `login.feature`):

```gherkin
Scenario Outline: Invalid logins are rejected with a clear message
  Given I am on the login page
  When I log in as "<username>" with password "<password>"
  Then I see the login error "<message>"

  Examples:
    | username | password       | message                                  |
    | alice    | wrong-password | Invalid username or password.            |
    |          | Passw0rd!      | Username and password are required.      |
    | locked   | Passw0rd!      | This account is locked. Contact support. |
```

## Step definitions: thin glue

```java
@Given("my cart contains:")
public void myCartContains(DataTable table) {
    for (Map<String, String> row : table.asMaps()) {
        String name = row.get("product");
        Product product = ctx.userApi.searchProducts(name).items().stream()
                .filter(p -> p.name().equals(name))
                .findFirst()
                .orElseThrow(() -> new AssertionError("No product named '" + name + "'"));
        ctx.userApi.addToCart(product.id(), Integer.parseInt(row.get("quantity")));
    }
}
```

Steps call service objects, page objects and `ShopDb`, never raw locators or URLs. `{string}` and `{int}` are Cucumber Expressions that capture and convert parameters.

## Sharing state: PicoContainer dependency injection

Four step classes (`LoginSteps`, `ShoppingSteps`, `OrderApiSteps`, `DatabaseSteps`) and `Hooks` all declare a `TestContext` constructor parameter:

```java
public class DatabaseSteps {
    private final TestContext ctx;
    public DatabaseSteps(TestContext ctx) { this.ctx = ctx; }
    ...
}
```

`cucumber-picocontainer` creates **one** `TestContext` per scenario and injects that same instance everywhere. That's how a UI step stores `ctx.lastOrderId` and a database step reads it. No static fields, so scenarios can't leak into each other and parallel runs stay safe.

## Hooks

`support/Hooks.java`:

```java
@Before
public void startSession(Scenario scenario) {
    ctx.start(scenario.getName());                 // new BrowserContext + Page + tracing
}

@After
public void endSession(Scenario scenario) {
    byte[] screenshot = ctx.finish(scenario.isFailed());   // trace.zip + screenshot only on failure
    if (screenshot != null) {
        scenario.attach(screenshot, "image/png", scenario.getName());
    }
}
```

## Running Cucumber on JUnit 5

`RunCucumberTest.java`:

```java
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.k11.pwj")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME,
        value = "pretty, html:target/cucumber-report.html, json:target/cucumber.json")
public class RunCucumberTest { }
```

```bash
mvn test -Dcucumber.filter.tags="@smoke"                  # verified: 3 scenarios run, 9 skipped
mvn test -Dcucumber.filter.tags="@api and not @security"
```

Open `target/cucumber-report.html` after a run.

## Data-driven JUnit tests

Not everything needs Gherkin. `ProductApiTest` reads its cases from `src/test/resources/testdata/product-search.csv`:

```
term,expectedCount,expectedNames
lamp,2,Desk Lamp;Smart Lamp
OFFICE,3,Desk Lamp;Smart Lamp;Ergonomic Chair
unicorn,0,
```

```java
@ParameterizedTest(name = "search \"{0}\" returns {1} product(s)")
@CsvFileSource(resources = "/testdata/product-search.csv", numLinesToSkip = 1)
void searchReturnsTheExpectedProducts(String term, int expectedCount, String expectedNames, ShopSession session) {
    ProductSearch result = session.newApiClient().searchProducts(term);
    ...
}
```

The CSV values fill the first parameters; `PlaywrightExtension` supplies the `ShopSession` after them. Adding a case means adding a row.

**When to use which:** Gherkin for behaviour the business reads and agrees on; parameterized JUnit for technical, data-heavy checks that no one outside QA needs to read.

## Review questions

**Explain Gherkin, step definitions and hooks.**
Gherkin is the plain-language feature format (Feature, Background, Scenario, Scenario Outline, Given/When/Then). Step definitions are Java methods matched to steps via Cucumber Expressions. Hooks (`@Before`, `@After`) run around each scenario for setup, teardown and failure evidence.

**How do you share state between step classes?**
Dependency injection: cucumber-picocontainer injects one context object per scenario into every step class's constructor. Never static fields.

**DataTable vs Scenario Outline?**
A DataTable passes structured data into **one** step; a Scenario Outline runs the **whole scenario** once per Examples row.

**How do you run Cucumber with JUnit 5?**
`cucumber-junit-platform-engine` plus a `@Suite` class selecting the features and glue; filter with `cucumber.filter.tags`.

Next: [7. SQL and database testing](07-sql-and-database-testing.md)
