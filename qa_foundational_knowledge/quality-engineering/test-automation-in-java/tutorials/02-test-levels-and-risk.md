# 02. Test levels and risk

**Beginner** · [Guide and prerequisites](../README.md) · [Worked example](../examples/java/Shipping.java)

## Match the test level to the failure mechanism

The level of a test describes the boundary it exercises. A unit check can isolate a decision inside one method. A component or integration check can reveal disagreement between collaborators. An end-to-end check follows a user-visible outcome across the assembled system. Moving outward introduces more dependencies and more possible explanations for a failure.

Consider a calendar reminder that fires an hour late. If the risk is a time-zone conversion error, exercise the conversion with a fixed instant and zone. If the scheduler saves the correct instant but a worker reads it in local time, exercise that handoff. If the notification reaches the service but never appears on the device, observe delivery there. Choose the smallest boundary that contains the suspected failure, then identify what still needs evidence at a wider boundary.

## Apply the concepts in the lab

A shop charges 499 cents for shipping below a 5,000-cent subtotal and offers free shipping at or above it. Start with the customer risk: a one-cent boundary error could charge every qualifying shopper. The Java method exposes the rule directly, so you can check many inputs cheaply. The browser example covers a different risk: a correct rule can still produce a wrong message on screen.

## Run the example

From the guide directory, after completing the prerequisites in the guide:

```sh
mkdir -p out
javac -d out examples/java/*.java
java -cp out guide.ShippingTest
```

## Interview question

**Which checks belong at each level?**

Use unit checks for every price boundary. Keep one browser journey for entering a subtotal and reading its quote. If the real product retrieves prices from a service, add an integration check for its amount and currency fields. Duplicating every unit case through a browser would slow diagnosis.

## Practice

Read the two return values and predict the fees for 4999 and 5000 cents. Run the Java suite below. Explain what a green result tells you about the rule and what still needs a browser.
