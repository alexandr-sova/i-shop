package test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import pages.BaseFunc;
import pages.HomePage;
import pages.ItemPage;
import pages.OrderPage;

public class TestIshop {
    private BaseFunc baseFunc = new BaseFunc();
    private final static String HOMEPAGE = "http://automationpractice.com/index.php";
    private final static By DRESSES = By.xpath(".//*[@id='block_top_menu']/ul/li/a[.= 'Dresses']");
    public static final Logger LOGGER = LogManager.getLogger(TestIshop.class);
    HomePage homePage = new HomePage(baseFunc);
    ItemPage itemPage = new ItemPage(baseFunc);
    OrderPage orderPage = new OrderPage(baseFunc);


    @Before
    public void openHomePage() {
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

        homePage.clickOnMenuDresses();
        homePage.setColorFilter("Yellow");
        homePage.selectDressByPrice(25.00, 30.00);
        homePage.continueShopping();

        homePage.searchItems("t-shirt");
        homePage.goToSelectedItemPage();
        itemPage.addToCartAndShopping();
        homePage.goToCartPage();

        orderPage.verifyCalculateCartsValues();
        orderPage.increasItemValue();
        orderPage.verifyCalculateCartsValues();
        orderPage.decreasItemValue();
        orderPage.verifyCalculateCartsValues();

//        String www = "";
    }

    @Test
    public void getColor() {
//        homePage.getMenuDresses();
//        homePage.clickOnMenuDresses();
//        homePage.setColorFilter("Yellow");
//        homePage.selectDressByPrice(25.00, 30.00);
//        homePage.continueShopping();
        homePage.searchItems("t-shirt");
        homePage.goToSelectedItemPage();
        itemPage.addToCartAndShopping();
        homePage.goToCartPage();


        String www = "";

    }

    @After
    public void quitBrowser() {

        baseFunc.closeBrowser();
    }
}

