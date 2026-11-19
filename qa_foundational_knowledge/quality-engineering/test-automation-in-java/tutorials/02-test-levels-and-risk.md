# 02. Test levels and risk

**Beginner** · [Guide and prerequisites](../README.md) · [Worked example](../examples/java/Shipping.java)

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
