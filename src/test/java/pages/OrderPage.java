package pages;

import model.Items;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;

public class OrderPage {
    BaseFunc baseFunc;
    private final static By CART_TABLE = By.xpath(".//*[@id='cart_summary']/tbody/tr");
    private final static By TABLE_COLUMN = By.tagName("td");
    private final static By TABLE_ITEM_QTY = By.tagName("input");
    private final static By TOTAL_PRODUCT = By.id("total_product");
    private final static By SHIPPING = By.id("total_shipping");
    private final static By TOTAL_NO_TAX = By.id("total_price_without_tax");
    private final static By TAX = By.id("total_tax");
    private final static By TOTAL_PRICE = By.id("total_price");
    private final static By UP_VALUE = By.xpath(".//a[@title = 'Add']");
    private final static By DOWN_VALUE = By.xpath(".//a[@title = 'Subtract']");
    private final static By QTY = By.xpath(".//input[@class='cart_quantity_input form-control grey']");
    private final static Logger LOGGER = LogManager.getLogger(OrderPage.class);

    public OrderPage(BaseFunc baseFunc) {
        this.baseFunc = baseFunc;
    }

    private List<WebElement> getAllTableRows() {

        return baseFunc.getElements(CART_TABLE);
    }

    private List<Items> getItemsList() {

        List<Items>resultList = new ArrayList<Items>();
        Integer rowsCount = baseFunc.getElements(CART_TABLE).size();

        Assert.assertFalse("List is empty!", rowsCount == 0);

        for(int i = 0; i < rowsCount; i++) {

            Items items = new Items();
            WebElement element = getAllTableRows().get(i);

            items.setName(element.findElements(TABLE_COLUMN).get(1).getText());
            items.setPrice(Double.parseDouble(element.findElements(TABLE_COLUMN).get(3).getText().split(" ", 2)[0].substring(1)));
            items.setQty(Integer.parseInt(element.findElements(TABLE_COLUMN).get(4).findElements(TABLE_ITEM_QTY).get(1).getAttribute("value")));
            items.setId(element.findElements(TABLE_COLUMN).get(4).findElements(UP_VALUE).get(0).getAttribute("id").split("up_", 2)[1]);
            resultList.add(items);
        }
        return resultList;
    }

    public void verifyCalculateCartsValues() {

        List<Items> tableItems = getItemsList();

        LOGGER.info("Items List has been successfully created for the calculations verifying.\n");

        Double totalProduct = 0.0;
        Double totalNoTax = 0.0;
        Double totalPrice = 0.0;
        Double sourceTotalNotax = Double.parseDouble(baseFunc.getElement(TOTAL_NO_TAX).getText().substring(1));
        Double sourceShipping = Double.parseDouble(baseFunc.getElement(SHIPPING).getText().substring(1));
        Double sourceTax = Double.parseDouble(baseFunc.getElement(TAX).getText().substring(1));
        Double sourceTotalProduct = Double.parseDouble(baseFunc.getElement(TOTAL_PRODUCT).getText().substring(1));
        Double sourceTotalPrice = Double.parseDouble(baseFunc.getElement(TOTAL_PRICE).getText().substring(1));

        for (int i = 0; i < tableItems.size(); i++) {

            WebElement element = getAllTableRows().get(i);
            Double sourceTotal = Double.parseDouble(element.findElements(By.tagName("td")).get(5).getText().split(" ", 1)[0].substring(1));

            Double total = getItemsList().get(i).getPrice()*getItemsList().get(i).getQty();
            assertEquals("Calculated value doesn't match with web Total(item) value for " + tableItems.get(i).getName(), total, sourceTotal);
            LOGGER.info("Calculated Total(item): " + total + " matched with the original value.");
            totalProduct += total;
        }
        assertEquals("Calculated value doesn't match with web Total Products value for ", totalProduct, sourceTotalProduct);
        LOGGER.info("Calculated Total Product: " + totalProduct + " matched with the original value.");

        totalNoTax = totalNoTax + totalProduct + sourceShipping;
        assertEquals("Calculated value doesn't match with web Total value for ", totalNoTax, sourceTotalNotax);
        LOGGER.info("Calculated Total: " + totalNoTax + " matched with the original value.");

        totalPrice = totalPrice + totalNoTax + sourceTax;
        assertEquals("Calculated value doesn't match with web TOTAL(order) value for ", totalPrice, sourceTotalPrice);
        LOGGER.info("Calculated TOTAL: " + totalPrice + " matched with the original value.\n");
    }

    public void increaseItemValue(Integer itemNumber) {

        Integer itemsCount = baseFunc.getElements(QTY).size();
        Assert.assertTrue("Items Nr." + itemNumber + " doesn't exist.", itemNumber < itemsCount);
        Integer qtyOriginal = Integer.parseInt(baseFunc.getElements(QTY).get(itemNumber).getAttribute("value"));
        String regEx = "up_";
        changeItemQty(itemNumber, UP_VALUE, regEx);

        baseFunc.getWait().until(ExpectedConditions.attributeToBe(baseFunc.getElements(QTY).get(itemNumber),"value" ,Integer.toString(qtyOriginal+1)));
        baseFunc.refreshPage("Order");
        LOGGER.info("Item Nr." + itemNumber + " has been increased by 1.\n\n");
    }

    public void decreaseItemValue(Integer itemNumber) {

        Integer qtyOriginal = Integer.parseInt(baseFunc.getElements(QTY).get(itemNumber).getAttribute("value"));
        Assert.assertTrue("The Qty is: "+ qtyOriginal + " and can't be decreased.", qtyOriginal > 1);
        String regEx = "down_";
        changeItemQty(itemNumber,DOWN_VALUE, regEx );

            baseFunc.getWait().until(ExpectedConditions.attributeToBe(baseFunc.getElements(QTY).get(itemNumber),"value", Integer.toString(qtyOriginal - 1)));
            baseFunc.refreshPage("Order");
            LOGGER.info("Item Nr." + itemNumber + " has been decreased by 1.\n\n");
    }

    private void changeItemQty(Integer itemNumber, By chnangType, String RegEx) {

        String id = getItemsList().get(itemNumber).getId();

        Integer rowCount = baseFunc.getElements(chnangType).size();
        int i;
        for (i = 0; i < rowCount; i++) {

            String elementId = baseFunc.getElements(chnangType).get(i).getAttribute("id").split(RegEx, 2)[1];

            if (elementId.equals(id)){
                break;
            }
        }
        baseFunc.getElements(chnangType).get(i).click();
    }
}

