package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import model.Menu;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.Assert.assertFalse;

public class HomePage {
    BaseFunc baseFunc;

    private final static By MENU = By.xpath(".//*[@id='block_top_menu']/ul");
    private final static By MENU_DRESSES = By.xpath(".//*[@id='block_top_menu']/ul/li/a[.= 'Dresses']");
    private final static By MENU_DRESSES_SUMMER = By.xpath(".//ul[contains(@class, 'submenu-container')]/*/a[.='Summer Dresses']");
    private final static By SEARCH = By.id("search_query_top");
    private final static By CART = By.xpath(".//b[.= 'Cart']");
    private final static Logger LOGGER = LogManager.getLogger(HomePage.class);

    public HomePage(BaseFunc baseFunc) {
        this.baseFunc = baseFunc;
        getMenu();
        getSearch();
        getCart();
    }

    private void getMenu() {

        assertFalse("The menu item 'Dersses' doesn't exist", baseFunc.getElements(MENU).isEmpty());
    }

    private void getSearch() {

        assertFalse("The Search bar doesn't exist", !baseFunc.getElement(SEARCH).isDisplayed());
    }

    private WebElement getCart() {

        assertFalse("The Cart doesn't exist", !baseFunc.getElement(CART).isDisplayed());
        return baseFunc.getElement(CART);
    }

    public void clickOnMenu(Menu menu, Menu subMenu) {

       switch (menu) {
           case DRESSES:
               baseFunc.actions()
                       .moveToElement(baseFunc.getElement(MENU_DRESSES))
                       .build()
                       .perform();
               baseFunc.getWait().until(ExpectedConditions.elementToBeClickable(MENU_DRESSES_SUMMER));
               switch (subMenu){
                   case SUMMER_DRESSES:
                       baseFunc.getElement(MENU_DRESSES_SUMMER).click();
                       break;
                   default:
                       throw new AssertionError("Unknown Sub Menu Item" + subMenu);
               }
               break;
           default:
               throw new AssertionError("Unknown Menu Item" + subMenu);
            }
        Assert.assertEquals("There isn't "+ Menu.SUMMER_DRESSES.item() +" page!",
                Menu.SUMMER_DRESSES.item() + baseFunc.MY_STORE,
                baseFunc.requestTitle());
        LOGGER.info(Menu.SUMMER_DRESSES.item() +" page has been opened.\n");
    }

}

