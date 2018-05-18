package test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pages.BaseFunc;
import pages.web.HomePage;
import pages.web.ItemPage;
import pages.web.OrderPage;

public class TestIshop {
    private BaseFunc baseFunc = new BaseFunc();
    private final static String HOMEPAGE = "http://automationpractice.com/index.php";
    public static final Logger LOGGER = LogManager.getLogger(TestIshop.class);
    HomePage homePage = new HomePage(baseFunc);
    ItemPage itemPage = new ItemPage(baseFunc);
    OrderPage orderPage = new OrderPage(baseFunc);


    @Before
    public void openHomepage() {
        baseFunc.goToUrl(HOMEPAGE);
    }

    @Test
    public void checkElementsOnHomePage() {

        homePage.getMenuDresses();
        homePage.getSearch();
        homePage.getCart();
    }

    @Test
    public void getItemsToCart() {

        homePage.selectYellowDresses();
        homePage.selectDressByPrice(25.00, 30.00);
        homePage.continueShopping();

        homePage.searchItems("t-shirt");
        homePage.selectItem();
        homePage.goToSeectedItemPage();
        itemPage.addToCartAndShopping();
        homePage.goToCartPage();

        orderPage.verifyCalculateCartsValues();
        orderPage.increasItemValue();
        orderPage.verifyCalculateCartsValues();
        orderPage.decreaseItemVlue();
        orderPage.verifyCalculateCartsValues();

        String www = "";
    }

    @After
    public void quitBrowser() {

        baseFunc.closeBrowser();
    }
}

