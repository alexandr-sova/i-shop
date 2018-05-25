package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ItemPage {
    BaseFunc baseFunc;
    private final static By ADD_TO_CART = By.xpath(".//*[@id='add_to_cart']/button");
    private final static By QTY = By.xpath(".//span[@class = 'ajax_cart_quantity']");
    private final static By IN_CART = By.id("layer_cart");
    private final static Logger LOGGER = LogManager.getLogger(ItemPage.class);

    public ItemPage(BaseFunc baseFunc) {
        this.baseFunc = baseFunc;
    }

    public void addToCartAndShopping() {

        Integer qtyBefore = Integer.parseInt(baseFunc.getElement(QTY).getText());
        baseFunc.getWait().until(ExpectedConditions.visibilityOfElementLocated(ADD_TO_CART));
        baseFunc.getElement(ADD_TO_CART).click();
        baseFunc.getWait().until(ExpectedConditions.visibilityOfElementLocated(IN_CART));
        Integer qtyAfter = Integer.parseInt(baseFunc.getElement(QTY).getText());
        Assert.assertTrue("Item hasn't added to the Cart.", !qtyAfter.equals(qtyBefore));
        LOGGER.info("Items has been successfully added to the Basket.\n\n");
    }
}
