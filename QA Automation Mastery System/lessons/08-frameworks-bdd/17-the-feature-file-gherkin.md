# The .feature file (Gherkin)

[08 · Module topics](../../modules/08-frameworks-bdd.md) · [Curriculum index](../../README.md)

**Category** · BDD WITH CUCUMBER

BDD starts with the feature file — plain-language scenarios that read as living documentation and are executable. Gherkin keywords: Feature, Scenario, Given (initial state), When (action), Then (expected outcome), And/But. Scenario Outline + Examples runs one scenario over a data table; Background factors out shared Givens; tags (@smoke) select what runs. The glue code (step definitions) comes next.

## Worked reference

````text
# features/checkout.feature

Feature: Checkout
  As a shopper
  I want to buy items in my cart
  So that I receive my order

  Background:                      # runs before EVERY scenario
    Given I am logged in as "qa@test.dev"
    And my cart is empty

  @smoke
  Scenario: Guest completes a purchase
    Given I add "Nimbus 2000" to the cart
    When I check out with my saved card
    Then the order is confirmed
    And the cart total is "$499.00"

  Scenario: Empty cart cannot check out
    When I try to check out
    Then I see "Your cart is empty"

  # Data-driven: runs once per Examples row
  Scenario Outline: Discount codes
    Given I add "Nimbus 2000" to the cart
    When I apply the code "<code>"
    Then the total is "<total>"

    Examples:
      | code     | total   |
      | WELCOME10| $449.10 |
      | HALFOFF  | $249.50 |
      | EXPIRED  | $499.00 |

# Each Given/When/Then maps to a step definition (next topic).
# Run:  behave            (Python)
#       npx cucumber-js   (JS/TS)
#       mvn test          (Cucumber-JVM)
````

## Exercise

Write a checkout.feature with a Background, one plain Scenario, and one Scenario Outline (3 Examples rows). Tag the smoke path.
