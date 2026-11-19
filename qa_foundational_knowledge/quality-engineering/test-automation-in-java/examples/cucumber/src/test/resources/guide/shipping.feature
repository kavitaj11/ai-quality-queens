Feature: Shipping charges
  Scenario Outline: Quote shipping at the free-shipping boundary
    Given a subtotal of <subtotal> cents
    When shipping is calculated
    Then the fee is <fee> cents

    Examples:
      | subtotal | fee |
      | 4999     | 499 |
      | 5000     | 0   |
