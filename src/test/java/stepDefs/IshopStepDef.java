package stepDefs;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java.After;
import model.Menu;
import pages.*;

public class IshopStepDef {

    private BaseFunc baseFunc = new BaseFunc();
    private HomePage homePage;
    private DressesPage dressesPage;
    private ItemPage itemPage;
    private OrderPage orderPage;

    @Given("open page (.*)")
    public void startShopping(String url) {
        baseFunc.goToUrl(url);
        homePage = new HomePage(baseFunc);
    }

    @When("we click on \"([^\"]*)\" -> \"([^\"]*)\" in menu")
    public void clickOnMenuItem(Menu menuItem, Menu subMenu) throws Throwable{
        homePage.clickOnMenu(menuItem, subMenu);
        switch (subMenu) {
            case SUMMER_DRESSES:
                dressesPage = new DressesPage(baseFunc);
                break;
            default:
                throw new AssertionError("Unknown Sub Menu Item" + subMenu);
        }
    }

    @Then("we select items in (.*) color")
    public void selectDresses(String color) {
        dressesPage.clickOnColor(color);
    }

    @Then("filtered items in range more (.*)USD less (.*)USD")
    public void getDressByPrice(Double lowPrice, Double highPrice) throws Exception {
        dressesPage.setPriceFilter(lowPrice, highPrice);
    }

    @Then("add (.*) st/d filtered item to cart")
    public void addFilteredItemToCart(int item) throws InterruptedException {
        dressesPage.addItemToCart(item - 1);
    }

    @Then("continue shopping")
    public void goShopping() {
        dressesPage.continueShopping();
    }

    @When("we search (.*) in the search box")
    public void getIitemByName(String itemName) {
        dressesPage.searchItems(itemName);
    }

    @Then("we go to (.*) st/d searched item page")
    public void getItem(int item) {
        dressesPage.selectItem(item-1);
        itemPage = dressesPage.goToSelectedItemPage(item-1);
    }

    @Then("add item to cart")
    public void addToCart() {
        itemPage.addToCartAndShopping();
    }

    @Then("go to Checkout")
    public  void goToCheckout() {
        orderPage = dressesPage.proceedToChekout();

    }

    @Then("we check TOTAL price")
    public void checkTotalPrice() {
        orderPage.verifyCalculateCartsValues();
    }

    @When("we increase the quantity for the (.*) st/d item")
    public void increaseValue(Integer itemNumber) {
        orderPage.increaseItemValue(itemNumber);
        orderPage = new OrderPage(baseFunc);
    }

    @When("we decrease the quantity for the (.*) st/d item")
    public void decreaseValue(Integer itemNumber) {
        orderPage.decreaseItemValue(itemNumber);
        orderPage = new OrderPage(baseFunc);
    }

    @After
    public void finishShopping() {
        baseFunc.closeBrowser();
    }
}

