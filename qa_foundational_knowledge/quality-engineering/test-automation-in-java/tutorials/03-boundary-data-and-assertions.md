# 03. Boundary data and assertions

**Beginner** · [Guide and prerequisites](../README.md) · [Worked example](../examples/java/ShippingTest.java)

**QA Automation Mastery** · [Test design techniques](../../../../QA%20Automation%20Mastery%20System/lessons/01-foundations/03-test-design-techniques.md)

## Derive inputs from the contract

An equivalence partition groups inputs for which the specification requires the same behavior. A boundary separates two such groups. Start by stating the unit, the valid range and whether each endpoint is included. For discrete inputs, exercise the endpoint and its nearest representable neighbors. Decide separately how to cover missing values or invalid formats when the interface accepts them.

Suppose a file importer accepts up to 100 records. For 99, 100 and 101 records the expected decisions are accept, accept and reject. If the contract also promises an all-or-nothing import, check the stored record count after rejection. A correct error message can coexist with a partial write. The extra assertion follows from a second promise in the contract.

## Apply the concepts in the lab

Partition the input into invalid, paid and free shipping. Pick values on both sides of the threshold. Store each input alongside an independently specified expected fee. Recomputing the expected value with the production formula would reproduce the same mistake in the test.

## Run the example

From the guide directory, after completing the prerequisites in the guide:

```sh
mkdir -p out
javac -d out examples/java/*.java
java -cp out guide.ShippingTest
```

## Interview question

**How do you prove a test can catch a regression?**

Make one controlled change in a practice copy. Changing >= to > should fail exactly at 5000. Check that the failure reports the input, expected fee and observed fee. Restore the original operator and rerun.

## Practice

Predict the effect of deleting the negative-input guard. Run the suite after that edit and explain why the exception check fails even when all four rows pass.
