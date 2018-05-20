package pages;

import model.Items;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;

public class OrderPage {
    BaseFunc baseFunc;
    private final static By CARTTABLE = By.xpath(".//*[@id='cart_summary']/tbody/tr");
    private final static By TOTALPRODUCT = By.id("total_product");
    private final static By SHIPPING = By.id("total_shipping");
    private final static By TOTALNOTAX = By.id("total_price_without_tax");
    private final static By TAX = By.id("total_tax");
    private final static By TOTALPRICE = By.id("total_price");
    private final static By UPVALUE = By.id("cart_quantity_up_5_19_0_0");
    private final static By DOWNVALUE = By.id("cart_quantity_down_5_19_0_0");
    private final static By QTY = By.xpath(".//input[@class='cart_quantity_input form-control grey']");
    private final static Logger LOGGER = LogManager.getLogger(OrderPage.class);

    public OrderPage(BaseFunc baseFunc) {
        this.baseFunc = baseFunc;
    }

    public List<WebElement> getAllTableRows() {

        return baseFunc.getElements(CARTTABLE);
    }

    public List<Items> getItemsList() {

        List<Items> resultList = new ArrayList<Items>();
        Integer rowsCount = baseFunc.getElements(CARTTABLE).size();

        for(int i = 0; i < rowsCount; i++) {

            Items items = new Items();
            WebElement element = getAllTableRows().get(i);

            items.setName(element.findElements(By.tagName("td")).get(1).getText());
            items.setPrice(Double.parseDouble(element.findElements(By.tagName("td")).get(3).getText().split(" ", 2)[0].substring(1)));
            items.setQty(Integer.parseInt(element.findElements(By.tagName("td")).get(4).findElements(By.tagName("input")).get(1).getAttribute("value")));
            items.setTotal(Double.parseDouble(element.findElements(By.tagName("td")).get(5).getText().split(" ", 1)[0].substring(1)));

            resultList.add(items);
        }

        return resultList;
    }

    public void verifyCalculateCartsValues() {

        List<Items> tableItems = getItemsList();
        LOGGER.info("\nItems List has been successfully created for the calculations verifying.\n");

        Double totalProduct = 0.0;
        Double totalNoTax = 0.0;
        Double totalPrice = 0.0;
        Double sourceTotalNotax = Double.parseDouble(baseFunc.getElement(TOTALNOTAX).getText().substring(1));
        Double sourceShipping = Double.parseDouble(baseFunc.getElement(SHIPPING).getText().substring(1));
        Double sourceTax = Double.parseDouble(baseFunc.getElement(TAX).getText().substring(1));
        Double sourceTotalProduct = Double.parseDouble(baseFunc.getElement(TOTALPRODUCT).getText().substring(1));
        Double sourceTotalPrice = Double.parseDouble(baseFunc.getElement(TOTALPRICE).getText().substring(1));

        for (int i = 0; i < tableItems.size(); i++) {

            WebElement element = getAllTableRows().get(i);
            Double sourceTotal = Double.parseDouble(element.findElements(By.tagName("td")).get(5).getText().split(" ", 1)[0].substring(1));

            Double total = getItemsList().get(i).getPrice()*getItemsList().get(i).getQty();
            assertEquals("Calculated value doesn't match with web Total(item) value for " + tableItems.get(i).getName(), total, sourceTotal);
            LOGGER.info("Calculated Total(item): " + total + " matched with the original value.");

            totalProduct = totalProduct + total;
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

    public void increasItemValue() {

        Integer qtyOriginal = Integer.parseInt(baseFunc.getElement(QTY).getAttribute("value"));
        baseFunc.getElement(UPVALUE).click();
        baseFunc.getWait().until(ExpectedConditions.attributeToBe(baseFunc.getElement(QTY),"value", Integer.toString(qtyOriginal+1)));
        baseFunc.refreshPage("Order");
        LOGGER.info("Item has been increased by 1.\n");
    }

    public void decreasItemValue() {

        Integer qtyOriginal = Integer.parseInt(baseFunc.getElements(QTY).get(1).getAttribute("value"));

        if (qtyOriginal > 1) {
            baseFunc.getElement(DOWNVALUE).click();
            baseFunc.getWait().until(ExpectedConditions.attributeToBe(baseFunc.getElements(QTY).get(1),
                    "value", Integer.toString(qtyOriginal - 1)));
            baseFunc.refreshPage("Order");
            LOGGER.info("Item has been decreased by 1.\n");
        }
    }

}

