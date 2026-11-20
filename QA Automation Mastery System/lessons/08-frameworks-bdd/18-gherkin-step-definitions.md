# Gherkin & step definitions

[08 · Module topics](../../modules/08-frameworks-bdd.md) · [Curriculum index](../../README.md)

**Category** · BDD WITH CUCUMBER

Given/When/Then features as living documentation, glued to code via step definitions. Cucumber-JVM for Java, cucumber-js for JS/TS, behave or pytest-bdd for Python. Know when BDD adds value (shared understanding) and when it's ceremony.

## Worked reference

````text
# steps/checkout_steps.py (behave)
from behave import when, then

@when('I add "{product}" to the cart')
def step_add(ctx, product):
    ctx.shop.add(product)

@then('the cart total is "{total}"')
def step_total(ctx, total):
    assert ctx.cart.total() == total
````

## Exercise

Write a checkout.feature with 3 scenarios and implement its steps against your POM layer.
