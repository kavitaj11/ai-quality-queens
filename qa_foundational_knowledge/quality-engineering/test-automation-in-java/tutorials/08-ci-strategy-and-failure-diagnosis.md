# 08. CI strategy and failure diagnosis

**Advanced** · [Guide and prerequisites](../README.md) · [Worked example](../examples/ci/check.yml)

The sample workflow compiles and runs the Java boundary suite first, then runs the Cucumber examples. A nonzero exit fails the job. Maven reports are collected on failure when available. A failure before Maven starts will have only the job log. The sample is stored with the examples so you can inspect it before enabling it.

## Run the example

From the guide directory, after completing the prerequisites in the guide:

```sh
mkdir -p out
javac -d out examples/java/*.java
java -cp out guide.ShippingTest
mvn -f examples/cucumber/pom.xml test
```

## Interview question

**A UI quote fails only in CI. What do you investigate first?**

Reproduce the same input and inspect the first failed operation. If the Java rule passes but the page shows Calculating…, inspect the eventual DOM state and timing. A wrong final amount points toward application logic or test data. A missing element points toward navigation, selectors or setup. Keep the first failure evidence before retrying.

## Practice

Run the two local stages below. In a practice branch change the threshold comparison and identify which stage first catches the regression. To try the workflow in a fork, copy the sample into .github/workflows/check.yml at repository root.
