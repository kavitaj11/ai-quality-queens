# 04. BDD with Cucumber-JVM

**Intermediate** · [Guide and prerequisites](../README.md) · [Worked example](../examples/cucumber/src/test/resources/guide/shipping.feature)

A scenario outline gives product and engineering a shared table of concrete decisions. Cucumber expands each example into a scenario. The Given step records the subtotal, When calls the same Java method as the unit suite, and Then compares the observed fee with the business expectation. Maven compiles the shared source and the JUnit runner discovers the feature.

## Run the example

From the guide directory, after completing the prerequisites in the guide:

```sh
mvn -f examples/cucumber/pom.xml test
```

## Interview question

**How do you keep a BDD suite maintainable?**

Use business terms in the feature and keep Java calls in step definitions. Store scenario state on the step instance. A static subtotal can leak between scenarios and become a race when the runner executes in parallel.

## Practice

Add a 5001 / 0 example. The existing step definitions should handle the extra scenario. Change its fee to 499 and read the failing Then step.
