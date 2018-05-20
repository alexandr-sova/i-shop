package pages;

import model.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;

public class HomePage {
    BaseFunc baseFunc;
    private final static By MENUDRESSES = By.xpath(".//*[@id='block_top_menu']/ul/li/a[.= 'Dresses']");
    private final static By COLORS = By.xpath(".//ul[@id='ul_layered_id_attribute_group_3']/li");
    private final static By ITEM = By.xpath(".//div[@class = 'product-container']");
    private final static By PRICE = By.xpath(".//span[@class = 'price product-price']");
    private final static By ADDTOCART = By.xpath(".//*[@id='center_column']/ul/li[1]/div/div[2]/div[2]/a[1]");
    private final static By SHOPPING = By.xpath(".//div[@class = 'button-container']/span[@class = 'continue btn btn-default button exclusive-medium']");
    private final static By SEARCH = By.id("search_query_top");
    private final static By CART = By.xpath(".//b[.= 'Cart']");
    private final static By SEARCHRESULT = By.xpath(".//*[@id='center_column']/h1");
    private final static By SEARCHEDITEM = By.xpath(".//span[@class='heading-counter']");
    private final static Logger LOGGER = LogManager.getLogger(HomePage.class);

    public HomePage(BaseFunc baseFunc) {
        this.baseFunc = baseFunc;
    }

    private List<Color> setColor() {

        List<Color> resultList = new ArrayList<Color>();

        Integer rowcount = baseFunc.getElements(COLORS).size();

        for (int i = 0; i < rowcount; i++) {

            Color color = new Color();

            WebElement element = baseFunc.getElements(COLORS).get(i);

            color.setName(element.getText().split(" ", 2)[0]);
            color.setLink(element.findElement(By.xpath(".//label/a")).getAttribute("href"));

            resultList.add(color);
        }
        return resultList;
    }

    public ItemPage goToSelectedItemPage() {

        selectItem().click();
        return new ItemPage(baseFunc);
    }

    public OrderPage goToCartPage() {

        getCart().click();
        return new OrderPage(baseFunc);
    }

    public void getMenuDresses() {

        assertFalse("The menu item 'Dersses' doesn't exist", baseFunc.getElements(MENUDRESSES).isEmpty());
    }

    public void getSearch() {

        assertFalse("The Search bar doesn't exist", !baseFunc.getElement(SEARCH).isDisplayed());
    }

    public WebElement getCart() {

        assertFalse("The Cart doesn't exist", !baseFunc.getElement(CART).isDisplayed());
        return baseFunc.getElement(CART);
    }

    public void clickOnMenuDresses() {

        baseFunc.getElement(MENUDRESSES).click();
        Assert.assertEquals("There isn't Dresses page!",
                            "Dresses - My Store", baseFunc.requestTitle());
        LOGGER.info("The Dresses page has been opened.\n");
    }

    private String getColor(String color) {

        List<Color> colorList = setColor();

        int i;
        for (i = 0; i < colorList.size(); i++) {
            String colorInList = colorList.get(i).getName();
            if (colorInList.matches(color)) {
                break;
            }
        }
        if (i == colorList.size()) {
            LOGGER.info("The " + color + " color doesn't exist it the list.\n");
        }
        return colorList.get(i).getLink();
    }

    public void setColorFilter(String color) {

        setColor();
        LOGGER.info("The " + color + " color has been selected.\n");
        baseFunc.goToUrl(getColor(color));
        LOGGER.info("Item(s) in " + color + " color has(have) been filtered.\n");
        baseFunc.getWait().until(ExpectedConditions.titleIs("Dresses > Color "+ color  + baseFunc.myStore));
    }

    public void selectDressByPrice(Double lowPrice, Double highPrice) {

        Integer rowcount = baseFunc.getElements(ITEM).size();

        int i;
        for (i = 0; i < rowcount; i++) {

            WebElement element = baseFunc.getElements(ITEM).get(i);

            if (Double.parseDouble(element.findElements(PRICE).get(1).getText().substring(1)) >= lowPrice &&
                Double.parseDouble(element.findElements(PRICE).get(1).getText().substring(1)) <= highPrice)
            {
                break;
            }
        }
        if (i == rowcount) {
            LOGGER.info("There isn't any item it the list.\n");
        }
        else {
            LOGGER.info("One Dress in price range between $"+ lowPrice + "USD and $" + highPrice + "USD have been selected.");
        }
        baseFunc.addToBascket().moveToElement(baseFunc.getElements(ITEM).get(i)).build().perform();
        baseFunc.getElement(ADDTOCART).click();
        baseFunc.getWait().until(ExpectedConditions.visibilityOfElementLocated(By.id("layer_cart")));
        LOGGER.info("One Dress in price range between $"+ lowPrice + "USD and $" + highPrice + "USD have been added to the Basket.\n");
    }

    public void continueShopping() {

        baseFunc.getElement(SHOPPING).click();
    }

    public void searchItems(String itemName) {

        baseFunc.getElement(SEARCH).clear();
        baseFunc.getElement(SEARCH).sendKeys(itemName);
        baseFunc.getElement(SEARCH).submit();
        baseFunc.getWait().until(ExpectedConditions.titleIs("Search" + baseFunc.myStore));
        baseFunc.getWait().until(ExpectedConditions.visibilityOfElementLocated(SEARCHRESULT));
        selectItem();
    }

    private WebElement selectItem() {

        Assert.assertTrue("There aren't any searched Items!", 0 < Integer.parseInt(baseFunc.getElement(SEARCHEDITEM).getText().split(" ", 2)[0]));
        return baseFunc.getElement(ITEM);
    }
}

