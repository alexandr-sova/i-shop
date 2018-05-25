Feature: This test are created for i-shop testing

  Scenario: Adding items to a cart and checking TOTAL prices
    Given open page http://automationpractice.com
     When we click on "DRESSES" -> "SUMMER_DRESSES" in menu
     Then we select items in Yellow color
      And filtered items in range more 25.00USD less 30.00USD
      And add 1 st/d filtered item to cart
      And continue shopping
     When we search t-shirt in the search box
     Then we go to 1 st/d searched item page
      And add item to cart
      And go to Checkout
      And we check TOTAL price
     When we increase the quantity for the 1 st/d item
     Then we check TOTAL price
     When we decrease the quantity for the 1 st/d item
     Then we check TOTAL price

