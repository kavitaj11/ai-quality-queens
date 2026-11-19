# 03. Boundary data and assertions

**Beginner** · [Guide and prerequisites](../README.md) · [Worked example](../examples/java/ShippingTest.java)

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
