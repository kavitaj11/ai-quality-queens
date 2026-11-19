# 01. Java basics for automation testers

[Java automation guide](../README.md) · [Shipping rule](../examples/java/Shipping.java) · [Executable test](../examples/java/ShippingTest.java)

Your first assignment is to check a checkout rule. Shipping costs 499 cents below a subtotal of 5,000 cents, becomes free at 5,000 cents, and rejects negative subtotals. By the end of this lesson you can run the check, read its Java code, and explain a boundary failure.

## 1. Compile and run your first check

Install JDK 17 or later. Open a terminal in the directory containing the guide README and run:

```sh
javac -version
mkdir -p out
javac -d out examples/java/Shipping.java examples/java/ShippingTest.java
java -cp out guide.ShippingTest
```

Expected output: `All shipping checks passed`. The compiler writes class files under `out/guide` because both sources declare `package guide;`. The launcher searches `out` for the qualified class name and starts its `main` method.

## 2. Read a Java method as a test contract

```java
public static int fee(int subtotalCents) {
    if (subtotalCents < 0) throw new IllegalArgumentException("Negative subtotal");
    return subtotalCents >= 5000 ? 0 : 499;
}
```

The class `Shipping` groups this behavior under a name. The method is `public`, so other classes can call it. It is `static`, so a test calls `Shipping.fee(5000)` directly. The parameter and return value both use `int` because the contract expresses amounts in whole cents. The comparison produces a `boolean`. When it is true the method returns 0, otherwise it returns 499. A negative input exits through an exception before either fee is returned.

## 3. Turn examples into data-driven assertions

```java
int[][] cases = {{4999, 499}, {5000, 0}, {5001, 0}};
for (int[] row : cases) {
    int subtotal = row[0];
    int expected = row[1];
    int actual = Shipping.fee(subtotal);
    if (actual != expected) {
        throw new AssertionError("Subtotal " + subtotal
                + ": expected " + expected + ", got " + actual);
    }
}
```

An array stores values by position, starting at zero. Each inner array holds one input and its independently chosen expected result. The enhanced `for` loop runs the same check for every row. An uncaught `AssertionError` stops the program and gives CI a failing exit status. Printing a mismatch alone would let the process finish successfully.

| When writing automation | Java choice | Example |
|---|---|---|
| Compare numeric fees or status codes | Compare primitive values with `==` or `!=` | `actual == 499` |
| Compare a UI label or API status string | Use `String.equals` for text content | `"PAID".equals(status)` also handles a null status |
| Keep several expected labels in order | Use a typed list and import `java.util.List` | `List<String> labels = List.of("Subtotal", "Shipping");` |
| Look up an expected value by name | Use a typed map and import `java.util.Map` | `Map<String, Integer> fees = Map.of("standard", 499);` |

## 4. Check the exception path

Read the last block in [ShippingTest.java](../examples/java/ShippingTest.java). The `try` block calls the rule with −1. If the call returns normally, the next line throws an assertion error. The `catch` accepts only `IllegalArgumentException`. An unexpected exception or the assertion error therefore fails the run. This matters because “something went wrong” is too weak a contract for invalid input.

## Practice lab

1. In a local practice copy, change `>= 5000` to `> 5000`. Predict the first failing row, then compile and run again.
2. Read the failure from the test’s point of view. At 5,000 cents the expected fee is 0 and the observed fee is 499. Why do both neighboring rows still satisfy their expectations?
3. Restore the rule. Add `{1, 499}` to the test table, then compile and rerun. The success message should return.

Continue with [test levels and risk](02-test-levels-and-risk.md) to decide which of these checks belongs below the browser.
