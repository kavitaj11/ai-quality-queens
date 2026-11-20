# 04. BDD with Cucumber-JVM

**Intermediate** · [Guide and prerequisites](../README.md) · [Worked example](../examples/cucumber/src/test/resources/guide/shipping.feature)

**QA Automation Mastery** · [Gherkin & step definitions](../../../../QA%20Automation%20Mastery%20System/lessons/08-frameworks-bdd/18-gherkin-step-definitions.md)

## How behavior-driven development reaches Java

BDD starts with a rule that product and engineering can explain in shared language. Gherkin captures the relevant context, event and observable outcome. Cucumber matches each step to Java glue code; that code creates the fixture, performs the action or checks the result. Keep scenario state scoped to a single scenario so execution order cannot change its meaning.

For a document approval workflow, a reviewer approving a draft and an unauthorised reader attempting approval are distinct behaviors with different outcomes. Express each rule clearly. Use a Scenario Outline when only the input data changes and the same rule still applies. Keep selectors and service paths in the glue or its helpers so a transport change does not force a rewrite of the specification.

## Apply the concepts in the lab

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
