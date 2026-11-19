@api @db
Feature: Orders API
  The orders API places orders from a shopper's cart and returns them as JSON.
  Responses must match the published schema and agree with the database.

  @smoke
  Scenario: API checkout returns a valid order that matches the database
    Given I am signed in to the API as "api-buyer"
    And my cart contains:
      | product    | quantity |
      | Blue Mug   | 2        |
      | Eco Bottle | 1        |
    When I check out through the API
    Then the API responds with status 201
    And the response matches the "order" schema
    And the order total is "40.25"
    And every order line total is quantity times unit price
    And the database total equals the sum of the order lines
    And the order has exactly 1 "CREATED" audit entry

  @regression
  Scenario: Checking out an empty cart is rejected
    Given I am signed in to the API as "empty-cart-user"
    When I check out through the API
    Then the API responds with status 409 and an error containing "cart is empty"

  @regression @security
  Scenario: A shopper cannot read another shopper's order
    Given I am signed in to the API as "order-owner"
    And my cart contains:
      | product   | quantity |
      | Desk Lamp | 1        |
    And I check out through the API
    When another signed-in user asks for that order
    Then the API responds with status 404 and an error containing "order not found"

  @regression @security
  Scenario: Orders cannot be read without signing in
    When an anonymous client asks for order "ORD-00000000"
    Then the API responds with status 401 and an error containing "not logged in"
