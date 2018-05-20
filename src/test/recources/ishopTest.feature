Feature: This test are created for i-shop testing

  Scenario: Adding items to a cart and checking TOTAL price
    When we select Dress item in menu
    Then we select Yellow color
    And we select item with price more then 25.00USD less then 30.00USD
    And we continue shopping
    And we search item t-shirt in the search box
    And we go to searched item page
    And we add item to cart
    And we open Cart
    And we check TOTAL price
    And we increase the quantity for an item
    And we check TOTAL price
    And we decrease the quantity for an item
    And we check TOTAL price



