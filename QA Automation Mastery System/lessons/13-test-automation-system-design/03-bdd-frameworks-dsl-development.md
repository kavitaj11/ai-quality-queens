# BDD frameworks & DSL development

[13 · Module topics](../../modules/13-test-automation-system-design.md) · [Curriculum index](../../README.md)

**Category** · FRAMEWORK BLUEPRINTS

BDD tooling (Cucumber, SpecFlow, behave, pytest-bdd) is really DSL engineering: you design a vocabulary of steps that business people can read and step definitions implement. The craft is in the DSL: declarative steps ('Given a gold-tier customer') not imperative clicks ('When I click #btn-2'), parameterized and composable, with a thin binding layer delegating to the same flows/pages your other tests use. A good DSL is a domain glossary that runs.

## Worked reference

````text
# pytest-bdd: thin bindings over the shared engine
# features/loyalty.feature
#   Given a "gold"-tier customer with an empty cart
#   When they purchase a discounted item
#   Then loyalty points apply at 2x

from pytest_bdd import scenario, given, when, then, parsers
from flows import CustomerFlow, CheckoutFlow  # shared engine

@scenario("features/loyalty.feature",
          "Gold tier gets double points")
def test_gold_double_points(): pass

@given(parsers.parse('a "{tier}"-tier customer with an empty cart'),
       target_fixture="customer")
def customer(tier, api):
    return CustomerFlow(api).create(tier=tier)  # API, not UI

@when("they purchase a discounted item")
def purchase(customer, page):
    CheckoutFlow(page).buy(customer, item="discounted")

@then(parsers.parse("loyalty points apply at {mult:d}x"))
def points_multiplied(customer, mult, api):
    assert api.points(customer.id) == customer.base_points * mult
````

## Exercise

Take one E2E test you own and rewrite it twice in Gherkin: once imperative (UI steps), once declarative (domain language). Show both to a PM — which one do they correct? That's the DSL working.
