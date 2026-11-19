# Test Automation in Java

**Start with [01. Java basics for automation testers](tutorials/01-java-basics-for-automation-testers.md).** Compile a Java class, call a method, and turn a shipping requirement into a table of executable checks. The first lab requires only JDK 17+.

## Run the first Java test

From this guide directory in a POSIX shell:

```sh
mkdir -p out
javac -d out examples/java/Shipping.java examples/java/ShippingTest.java
java -cp out guide.ShippingTest
```

Look for `All shipping checks passed`. Open [Shipping.java](examples/java/Shipping.java) and [ShippingTest.java](examples/java/ShippingTest.java) side by side. Trace what happens when the subtotal moves from 4,999 to 5,000 cents.

## Java learning path

| Lesson | What you will practice |
|---|---|
| [01. Java basics for automation testers](tutorials/01-java-basics-for-automation-testers.md) | Read method signatures, compare values, iterate over cases, and check exceptions. |
| [02. Test levels and risk](tutorials/02-test-levels-and-risk.md) | Decide which shipping risks a fast Java test can cover. |
| [03. Boundary data and assertions](tutorials/03-boundary-data-and-assertions.md) | Choose inputs around a threshold and diagnose an off-by-one failure. |
| [04. BDD with Cucumber-JVM](tutorials/04-bdd-with-cucumber-jvm.md) | Connect a [Gherkin scenario](examples/cucumber/src/test/resources/guide/shipping.feature) to Java step definitions. |
| [05. Browser synchronization with Selenium](tutorials/05-browser-synchronization-with-selenium.md) | Wait for a quote to become observable in the [Java browser check](examples/selenium/ShippingBrowser.java). |
| [06. Locators and isolation with Playwright Java](tutorials/06-locators-and-isolation-with-playwright-java.md) | Locate controls and isolate browser state in the [Playwright example](examples/playwright/ShippingBrowser.java). |

## Set up the framework labs

Use Maven for Cucumber-JVM. The standalone Selenium and Playwright Java examples use JBang to resolve dependencies. Selenium requires Chrome and a compatible driver, available locally or through Selenium Manager. Playwright requires its matching Chromium browser and system dependencies. Allow internet access for first-time downloads.

For the browser labs, start the shipping calculator with Python 3 in a separate terminal and leave it running. Open <http://127.0.0.1:8000> to confirm the calculator loads.

```sh
python -m http.server 8000 --bind 127.0.0.1 --directory examples/web
```

## Extend the same scenario

- [07. Validate persisted outcomes with SQL](tutorials/07-validate-persisted-outcomes-with-sql.md) uses the SQLite CLI and a [shipping query](examples/sql/shipping.sql).
- [08. CI strategy and failure diagnosis](tutorials/08-ci-strategy-and-failure-diagnosis.md) connects the checks to a [sample CI workflow](examples/ci/check.yml).
- For the Cypress version of this scenario, see [Test Automation in JavaScript](../test-automation-in-javascript/README.md).
