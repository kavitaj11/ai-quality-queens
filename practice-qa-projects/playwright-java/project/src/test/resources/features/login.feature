@ui
Feature: Logging in
  As a shopper
  I want to log in securely
  So that only I can see my cart and orders

  @smoke
  Scenario: A registered shopper logs in
    Given I am on the login page
    When I log in as "alice" with the valid password
    Then I see the products page with the greeting "Hi, alice"

  @regression
  Scenario Outline: Invalid logins are rejected with a clear message
    Given I am on the login page
    When I log in as "<username>" with password "<password>"
    Then I see the login error "<message>"

    Examples:
      | username | password       | message                                  |
      | alice    | wrong-password | Invalid username or password.            |
      |          | Passw0rd!      | Username and password are required.      |
      | locked   | Passw0rd!      | This account is locked. Contact support. |
