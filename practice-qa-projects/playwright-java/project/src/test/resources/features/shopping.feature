@ui @db
Feature: Buying products in the web shop
  As a shopper
  I want to search for products and buy them
  So that I receive what I ordered and pay the right amount

  Background:
    Given I am logged in as "pwj-shopper"

  @smoke
  Scenario: A purchase is confirmed on screen and stored correctly in the database
    When I search for "lamp"
    And I add 2 of "Desk Lamp" to my cart
    Then my cart total is "$69.98"
    When I check out
    Then my order is confirmed with a total of "$69.98"
    And the database holds the order with status "CONFIRMED" and total "69.98"
    And the order has exactly 1 "CREATED" audit entry

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
