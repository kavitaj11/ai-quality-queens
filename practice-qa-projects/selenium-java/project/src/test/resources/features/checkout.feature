Feature: Buying products
  As a shopper
  I want to search for products and buy them
  So that I receive what I ordered and pay the right amount

  Background:
    Given I am logged in as "bdd-user"

  @smoke
  Scenario: Buy a single product
    When I search for "bottle"
    And I add 3 of "Eco Bottle" to my cart
    Then my cart total is "$45.75"
    When I check out
    Then my order is confirmed with a total of "$45.75"

  @regression
  Scenario Outline: Cart total is quantity times price
    When I search for "<search>"
    And I add <quantity> of "<product>" to my cart
    Then my cart total is "<total>"

    Examples:
      | search    | product                     | quantity | total   |
      | mug       | Blue Mug                    | 1        | $12.50  |
      | lamp      | Smart Lamp                  | 2        | $118.00 |
      | headphone | Noise-Cancelling Headphones | 5        | $745.00 |
