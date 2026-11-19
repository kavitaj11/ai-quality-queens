# 6. BDD with Cucumber

**Behaviour-Driven Development (BDD)** is a collaboration practice first: product owners, developers and testers agree on examples of behaviour in plain language *before* building. Cucumber then turns those examples into automated tests. Without the collaboration, Cucumber is just a verbose way to write tests.

## The feature file (Gherkin)

`src/test/resources/features/checkout.feature`:

```gherkin
Feature: Buying products

  Background:
    Given I am logged in as "bdd-user"

  @smoke
  Scenario: Buy a single product
    When I search for "bottle"
    And I add 3 of "Eco Bottle" to my cart
    Then my cart total is "$45.75"
    When I check out
    Then my order is confirmed with a total of "$45.75"

  @regression
  Scenario Outline: Cart total is quantity times price
    When I search for "<search>"
    And I add <quantity> of "<product>" to my cart
    Then my cart total is "<total>"

    Examples:
      | search    | product                     | quantity | total   |
      | mug       | Blue Mug                    | 1        | $12.50  |
      | lamp      | Smart Lamp                  | 2        | $118.00 |
      | headphone | Noise-Cancelling Headphones | 5        | $745.00 |
```

| Keyword | Meaning |
|---|---|
| `Feature` | The capability being described |
| `Background` | Steps run before every scenario in the file |
| `Scenario` | One concrete example |
| `Scenario Outline` + `Examples` | The same example with several rows of data |
| `Given / When / Then` | Context / action / expected outcome |
| `@smoke` | A tag for filtering |

**Write behaviour, not clicks.** "When I add 3 of Eco Bottle to my cart", not "When I click the select with id quantity and choose 3 and click the button". Business people should be able to read and correct your scenarios.

## Step definitions: thin glue

`bdd/ShoppingSteps.java` maps each step to page-object calls, with no raw Selenium:

```java
@When("I add {int} of {string} to my cart")
public void iAddToMyCart(int quantity, String productName) {
    cart = products.openProduct(productName).chooseQuantity(quantity).addToCart();
}

@Then("my cart total is {string}")
public void myCartTotalIs(String expected) {
    assertThat(cart.total()).isEqualByComparingTo(Money.parse(expected));
}
```

`{int}` and `{string}` are **Cucumber Expressions**: they capture and convert parameters. Regular expressions also work.

## Hooks

`bdd/Hooks.java` starts a browser before each scenario and, after a failure, attaches a screenshot to the Cucumber report:

```java
@After
public void stopBrowser(Scenario scenario) {
    if (scenario.isFailed()) {
        byte[] png = ((TakesScreenshot) DriverFactory.getDriver()).getScreenshotAs(OutputType.BYTES);
        scenario.attach(png, "image/png", scenario.getName());
    }
    DriverFactory.quit();
}
```

## Sharing state between steps

Cucumber creates a **new instance of each step class for every scenario**, so fields in `ShoppingSteps` (`products`, `cart`, `order`) are safe per-scenario state. When steps are split across several classes (LoginSteps, CartSteps…), share state with **dependency injection**: add `cucumber-picocontainer` and inject a `ScenarioContext` object into each step class's constructor. Never use static fields for scenario state; they break parallel runs exactly like the bug in Chapter 4.

## Running

`bdd/CucumberRunner.java` runs features through TestNG:

```java
@CucumberOptions(features = "src/test/resources/features", glue = "com.k11.shop.bdd",
                 plugin = {"pretty", "html:target/cucumber-report.html", "json:target/cucumber.json"})
public class CucumberRunner extends AbstractTestNGCucumberTests { }
```

```bash
mvn test                                              # included in testng.xml
mvn test -Dcucumber.filter.tags="@smoke"              # only @smoke scenarios
```

Open `target/cucumber-report.html` after a run. All 4 scenarios (1 scenario + 3 outline rows) pass.

## Review questions

**What is BDD? Is it the same as Cucumber?**
BDD is a collaboration practice: agree on concrete examples of behaviour before building. Cucumber is a tool that automates those examples. Using Cucumber without the conversations misses the point.

**Scenario vs Scenario Outline?**
A Scenario is one example. A Scenario Outline runs the same steps once per row in its Examples table.

**What is Background?**
Steps that run before every scenario in a feature, for shared context such as logging in. Keep it short.

**How do you share data between step definition classes?**
Dependency injection (PicoContainer, Spring or Guice) with a shared context object created per scenario. Not static fields.

**What makes a good Gherkin scenario?**
Declarative business language, one behaviour per scenario, independent of other scenarios, and readable by a non-technical stakeholder.

**How do you run only some scenarios?**
Tags: `-Dcucumber.filter.tags="@smoke and not @wip"`.

Next: [7. API setup, CI/CD, Grid and AI-assisted testing](07-api-setup-ci-grid-and-ai.md)
