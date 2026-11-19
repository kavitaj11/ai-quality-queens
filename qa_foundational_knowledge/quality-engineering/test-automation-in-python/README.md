# Test automation in Python

[Data-driven tests with pytest](tutorials/01-data-driven-tests-with-pytest.md) shows how to parameterize boundary cases and use a session-scoped fixture to compile a Java command-line adapter. Each test runs the adapter and checks its output.

Use Python 3 with pip and a JDK 17+ with both `java` and `javac` on your PATH. Run the tutorial commands from this directory in a checkout of the repository. The [pytest example](examples/pytest/test_shipping.py) exercises the [shipping rule](../test-automation-in-java/examples/java/Shipping.java) from the Java guide.
