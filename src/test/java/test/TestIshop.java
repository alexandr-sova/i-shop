package test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import pages.*;

public class TestIshop {
    private BaseFunc baseFunc = new BaseFunc();
    private final static String HOMEPAGE = "http://automationpractice.com/index.php";
//    private final static String HOMEPAGE = "http://jqueryui.com/slider/";


    private final static By DRESSES = By.xpath(".//*[@id='block_top_menu']/ul/li/a[.= 'Dresses']");
    public static final Logger LOGGER = LogManager.getLogger(TestIshop.class);

    DressesPage dressesPage = new DressesPage(baseFunc);
    HomePage homePage = new HomePage(baseFunc);
    ItemPage itemPage = new ItemPage(baseFunc);
    OrderPage orderPage = new OrderPage(baseFunc);

    @Before
    public void openHomePage() {
        baseFunc.goToUrl(HOMEPAGE);
    }

    @Test
    public void checkElementsOnHomePage() {

//        dressesPage.getMenuDresses();
//        dressesPage.getSearch();
//        dressesPage.getCart();
    }

    @Test
    public void getSlider() throws Exception {
//        homePage.clickOnMenu("Dresses");
//        dressesPage.clickOnColor("Yellow");
//        dressesPage.setPriceFilter(25.00, 30.00);
//        dressesPage.addItemToCart();
        String www = "";
    }



    @After
    public void quitBrowser() {

//        baseFunc.closeBrowser();
    }
}

