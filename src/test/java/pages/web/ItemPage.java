package pages.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.BaseFunc;

public class ItemPage {
    BaseFunc baseFunc;
    private final static By ADDTOCART = By.xpath(".//*[@id='add_to_cart']/button");
    private final static By SHOPPING = By.xpath(".//div[@class = 'button-container']/span[@class = 'continue btn btn-default button exclusive-medium']");
    private final static Logger LOGGER = LogManager.getLogger(ItemPage.class);


    public ItemPage(BaseFunc baseFunc) {
        this.baseFunc = baseFunc;
    }

    public void addToCartAndShopping() {
        baseFunc.getElement(ADDTOCART).click();
        baseFunc.getWait().until(ExpectedConditions.visibilityOfElementLocated(By.id("layer_cart")));
        baseFunc.getElement(SHOPPING).click();
        LOGGER.info("Items has been successfully added to the Basket.");

    }

}
