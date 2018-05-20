package stepDefs;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java.Before;
import cucumber.api.java.After;
import pages.BaseFunc;
import pages.HomePage;
import pages.ItemPage;
import pages.OrderPage;

public class IshopTestStepDef {

    private final static String HOMEPAGE = "http://automationpractice.com/index.php";
    private BaseFunc baseFunc = new BaseFunc();
    private HomePage homePage = new HomePage(baseFunc);
    private ItemPage itemPage = new ItemPage(baseFunc);
    private OrderPage orderPage = new OrderPage(baseFunc);

    @When("we select Dress item in menu")
    public void menuDress () {
        homePage.clickOnMenuDresses();
    }

    @Then("we click on (.*)")
    public void yellowDresses(String color) {
        homePage.setColorFilter(color);
    }

    @Then("we select item with price more then (.*)USD less then (.*)USD")
    public void getDressByPrice(Double lowPrice, Double highPrice) {
        homePage.selectDressByPrice(lowPrice, highPrice);
    }

    @Then("we continue shopping")
    public void goShopping() {
        homePage.continueShopping();
    }

    @Then("we search (.*) in the search box")
    public void getIitemByName(String itemName) {
        homePage.searchItems(itemName);
    }
    @Then("we go to searched item page")
    public void getItem() {
        homePage.goToSelectedItemPage();
    }

    @Then("we add item to cart")
    public void addToCart() {
        itemPage.addToCartAndShopping();
    }

    @Then("we open Cart")
    public  void openCart() {
        homePage.goToCartPage();
    }

    @Then("we check TOTAL price")
    public void checkTotalPrice() {
        orderPage.verifyCalculateCartsValues();
    }

    @Then("we increase the quantity for an item")
    public void increaseValue() {
        orderPage.increasItemValue();
    }

    @Then("we decrease the quantity for an item")
    public void decreaseValue() {
        orderPage.decreasItemValue();
    }

    @Before
    public void startShopping() {
        baseFunc.goToUrl(HOMEPAGE);
    }

    @After
    public void finishShopping() {
        baseFunc.closeBrowser();
    }
}
