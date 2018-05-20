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
    private final static By YELLOW = By.id("layered_id_attribute_group_16");
    private final static By COLORS = By.xpath(".//ul[@id='ul_layered_id_attribute_group_3']/li");
    private final static By ITEM = By.xpath(".//div[@class = 'product-container']");
    private final static By PRICE = By.xpath(".//span[@class = 'price product-price']");
    private final static By ADDTOCART = By.xpath(".//*[@id='center_column']/ul/li[1]/div/div[2]/div[2]/a[1]");
    private final static By SHOPPING = By.xpath(".//div[@class = 'button-container']/span[@class = 'continue btn btn-default button exclusive-medium']");
    private final static By SEARCH = By.id("search_query_top");
    private final static By CART = By.xpath(".//b[.= 'Cart']");
    private final static By SEARCHRESULT = By.xpath(".//*[@id='center_column']/h1");
    private final static Logger LOGGER = LogManager.getLogger(HomePage.class);

    public HomePage(BaseFunc baseFunc) {
        this.baseFunc = baseFunc;
    }

    private List<WebElement> getAllItems() {
        return baseFunc.getElements(ITEM);
    }

    private List<Color> setColor() {

        List<Color> resultList = new ArrayList<Color>();

        Integer rowcount = baseFunc.getElements(COLORS).size();
        for (int i = 0; i < rowcount; i++) {

            Color color = new Color();

            WebElement element = baseFunc.getElements(COLORS).get(i);

            color.setName(element.getText().split(" ", 2)[0]);
            color.setLink(element.findElement(By.xpath(".//label/a")).getAttribute("href"));
            color.setElement(element);

            resultList.add(color);
        }
        String qqq = "";
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

    public List<WebElement> getMenuDresses() {

        assertFalse("The menu item 'Dersses' doesn't exist", baseFunc.getElements(MENUDRESSES).isEmpty());
        return baseFunc.getElements(MENUDRESSES);
    }

    public WebElement getSearch() {

        assertFalse("The Search bar doesn't exist", !baseFunc.getElement(SEARCH).isDisplayed());
        return baseFunc.getElement(SEARCH);
    }

    public WebElement getCart() {

        assertFalse("The Cart doesn't exist", !baseFunc.getElement(CART).isDisplayed());
        return baseFunc.getElement(CART);
    }

    public void clickOnMenuDresses() {

        baseFunc.getElement(MENUDRESSES).click();
        Assert.assertEquals("There isn't Dresses page!",
                            "Dresses - My Store", baseFunc.requestTitle());
    }

    private String getColor(String color) {

        List<Color> colorList = setColor();
        int i;
        for (i = 0; i < colorList.size(); i++) {
            String colorInList = colorList.get(i).getName();
            if (colorInList.matches(color)) {
                break;
// else !!!!!!!!!!!!
            }
        }
        return colorList.get(i).getLink();
    }

    public void setColorFilter(String color) {

        setColor();
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
        LOGGER.info("One Dress in price range between $"+ lowPrice + "USD and $" + highPrice + "USD have been selected.");
        baseFunc.addToBascket().moveToElement(baseFunc.getElements(ITEM).get(i)).build().perform();
        baseFunc.getElement(ADDTOCART).click();
        baseFunc.getWait().until(ExpectedConditions.visibilityOfElementLocated(By.id("layer_cart")));
        LOGGER.info("One Dress in price range between $"+ lowPrice + "USD and $" + highPrice + "USD have been added to the Basket.\n");
    }

    public void continueShopping() {

        baseFunc.getElement(SHOPPING).click();
    }

    public WebElement searchItems(String itemName) {

        baseFunc.getElement(SEARCH).clear();
        baseFunc.getElement(SEARCH).sendKeys(itemName);
        baseFunc.getElement(SEARCH).submit();
        baseFunc.getWait().until(ExpectedConditions.titleIs("Search" + baseFunc.myStore));
        baseFunc.getWait().until(ExpectedConditions.visibilityOfElementLocated(SEARCHRESULT));
        selectItem();
        return baseFunc.getElement(ITEM);
    }

    public WebElement selectItem() {

        Assert.assertTrue("There aren't any searched Items!", !getAllItems().isEmpty());
        return baseFunc.getElement(ITEM);
    }
}

