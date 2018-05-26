package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.math.BigDecimal;
import java.util.List;
import model.Menu;
import static org.junit.Assert.assertFalse;

public class DressesPage {
    BaseFunc baseFunc;
    private final static By COLORS = By.xpath(".//ul[@id='ul_layered_id_attribute_group_3']/li");
    private final static By COLORS_BUTTON = By.xpath(".//input[contains(@class, 'color-option')]");
    private final static By ITEM = By.xpath(".//div[@class = 'product-container']");
    private final static By ITEM_PRICE = By.xpath(".//div[@class = 'right-block']/div[@class = 'content_price']/span[@class = 'price product-price']");
    private final static By ADD_TO_CART = By.xpath(".//a[@class='button ajax_add_to_cart_button btn btn-default']");
    private final static By SHOPPING = By.xpath(".//span[@title = 'Continue shopping']");
    private final static By CHECKOUT = By.xpath(".//a[@title = 'Proceed to checkout']");
    private final static By SEARCH = By.id("search_query_top");
    private final static By SEARCH_RESULT = By.xpath(".//*[@id='center_column']/h1");
    private final static By SEARCHED_ITEM = By.xpath(".//span[@class='heading-counter']");
    private final static By MIN_SLIDER = By.xpath(".//*[@id='layered_price_slider']/a[1]");
    private final static By MAX_SLIDER = By.xpath(".//*[@id='layered_price_slider']/a[2]");
    private final static By SLIDER = By.xpath(".//*[@id='layered_price_slider']/div");
    private final static By SLIDER_PRICE_RANGE = By.id("layered_price_range");
    private final static Logger LOGGER = LogManager.getLogger(DressesPage.class);
    private String colorItem = "";

    public DressesPage(BaseFunc baseFunc) {
        this.baseFunc = baseFunc;
    }

    public void clickOnColor (String color) {

        List<WebElement> colors = baseFunc.getElements(COLORS);
        List<WebElement> colorsButton = baseFunc.getElements(COLORS_BUTTON);
        assertFalse("Colors List is empty", colors.isEmpty());
        assertFalse("Color buttons List is empty", colorsButton.isEmpty());

        Integer rowCount = baseFunc.getElements(COLORS).size();
        int i;
        for (i = 0; i < rowCount; i++) {

           String colorName = colors.get(i).getText().split(" ", 2)[0];
           if (colorName.equals(color)) {
               break;
           }
        }
        colorItem = baseFunc.getElements(COLORS_BUTTON).get(i).getCssValue("background-color");
        colorsButton.get(i).click();
        baseFunc.getWait().until(ExpectedConditions.titleIs(Menu.SUMMER_DRESSES.item() + " > Color "+ color  + baseFunc.MY_STORE));
        Assert.assertEquals("There isn't page for " + color + " items.",
                                      baseFunc.requestTitle(), Menu.SUMMER_DRESSES.item() + " > Color "+ color  + baseFunc.MY_STORE);
        LOGGER.info("Item(s) in " + color + " color has(have) been filtered.\n");
    }

    public ItemPage goToSelectedItemPage(int item) {

        selectItem(item).click();
        return new ItemPage(baseFunc);
    }

    public WebElement selectItem(int item) {

        Assert.assertTrue("There aren't any searched Items!",
                Integer.parseInt(baseFunc.getElement(SEARCHED_ITEM).getText().split(" ", 2)[0]) > 0);
        Assert.assertTrue(" Number(" + (item+1) +")" + "of item is out of range." + " Total items:" + baseFunc.getElements(SEARCHED_ITEM).size(),
                item <= baseFunc.getElements(SEARCHED_ITEM).size());

        return baseFunc.getElements(ITEM).get(item);
    }

    public void continueShopping() {

        baseFunc.getWait().until(ExpectedConditions.visibilityOfElementLocated(SHOPPING));
        baseFunc.getElement(SHOPPING).click();
    }

    public OrderPage proceedToChekout() {

        baseFunc.getWait().until(ExpectedConditions.elementToBeClickable(CHECKOUT));
        baseFunc.getElement(CHECKOUT).click();
        return new OrderPage(baseFunc);
    }

    public void searchItems(String itemName) {

        baseFunc.getElement(SEARCH).clear();
        baseFunc.getElement(SEARCH).sendKeys(itemName);
        baseFunc.getElement(SEARCH).submit();
        baseFunc.getWait().until(ExpectedConditions.titleIs("Search" + baseFunc.MY_STORE));
        baseFunc.getWait().until(ExpectedConditions.visibilityOfElementLocated(SEARCH_RESULT));
    }

    public void setPriceFilter(Double lowPrice, Double highPrice) throws Exception {

        Integer sliderWidth = baseFunc.getElement(SLIDER).getSize().getWidth();
        Double minItemPrice = Double.parseDouble(baseFunc.getElement(SLIDER_PRICE_RANGE).getText().split(" ", 3)[0].substring(1));
        Double maxItemPrice = Double.parseDouble(baseFunc.getElement(SLIDER_PRICE_RANGE).getText().split(" ", 3)[2].substring(1));

        Assert.assertTrue("Requested range is out of boundaries. " + "\n" +
                                   "Low price is: " + lowPrice +" and should be <= High price " + highPrice + "\n" ,
                          lowPrice <= highPrice);

        Double xOffsetMin = round((lowPrice - minItemPrice) * (sliderWidth / (maxItemPrice - minItemPrice)),0);
        Double xOffsetMax = round(sliderWidth - (highPrice - minItemPrice) * (sliderWidth / (maxItemPrice - minItemPrice)),0);

        Integer xPositionMinSlider = xOffsetMin.intValue();
        Integer xPositionMaxSlider = xOffsetMax.intValue();

        Actions action = baseFunc.actions();

        action.moveToElement(baseFunc.getElement(MIN_SLIDER))
                .clickAndHold()
                .moveByOffset(xPositionMinSlider,0)
                .release()
                .perform();
        baseFunc.getWait().until((ExpectedConditions.visibilityOfElementLocated(ITEM)));

        action.moveToElement(baseFunc.getElement(MAX_SLIDER))
                .clickAndHold()
                .moveByOffset(-xPositionMaxSlider,0)
                .release()
                .perform();
         baseFunc.getWait().until((ExpectedConditions.presenceOfElementLocated(ITEM)));

         compareSearchCriteria(lowPrice, highPrice);
    }

    private void compareSearchCriteria(Double lowPrice, Double highPrice) {

        String typeOfItems = baseFunc.driver.getTitle().split(" ", 2)[0];

        if (!baseFunc.getElements(ITEM).isEmpty()) {

            List<WebElement> items = baseFunc.getElements(ITEM);

            for (WebElement item : items) {
                Double itemPrice = Double.parseDouble(baseFunc.getElement(ITEM_PRICE).getText().substring(1));
                String itemType = baseFunc.getElement(ITEM).getText();
                String colorOfItem = baseFunc.getElements(By.xpath(".//a[@class='color_pick']")).get(3).getCssValue("background-color");

                Assert.assertTrue("Item(s) doesn't(haven't) match search criteria.\n" +
                                "Item price is: " + itemPrice  + " (range " + lowPrice + " and " + highPrice + " )" + "\n" +
                                "Item color is: " + colorOfItem  + " should be : " + colorItem + "\n" +
                                "Item type is : " + itemType.split(" ", 3)[1] + " should be : " + typeOfItems + "\n",
                        (itemPrice >= lowPrice && itemPrice <= highPrice)
                                && colorOfItem.equals(colorItem)
                                && itemType.contains(typeOfItems));
            }
        }
        LOGGER.info("Items in price range $"+ lowPrice + "USD and $" + highPrice + "USD have been selected.\n");
    }

    public void addItemToCart(int item) throws InterruptedException {

        assertFalse(" List is empty.", baseFunc.getElements(ITEM).isEmpty());
        Assert.assertTrue(" Number(" + (item+1) +")" + "of item is out of range." + " Total items:" + baseFunc.getElements(ITEM).size(),
                          item <= baseFunc.getElements(ITEM).size());

        baseFunc.actions().moveToElement(baseFunc.getElements(ITEM).get(item)).build().perform();
        baseFunc.getElements(ADD_TO_CART).get(item).click();
    }

    private static double round(double number, int decimalPlace) {
        BigDecimal bd = new BigDecimal(number);
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
}

