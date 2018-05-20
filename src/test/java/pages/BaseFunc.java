package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.List;
import static org.junit.Assert.*;

public class BaseFunc {
    private static final Logger LOGGER = LogManager.getLogger(BaseFunc.class);
    public final static String myStore = " - My Store";

    WebDriver driver;

    public BaseFunc() {

        System.setProperty("webdriver.gecko.driver", "/Users/alex/Downloads/geckodriver");
        this.driver = new FirefoxDriver();
        driver.manage().window().maximize();
    }

    public void goToUrl(String url){

        if (!url.contains("http://") && !url.contains("https://")) {
            url = "http://" + url;
        }
        LOGGER.info("Openning web page - " + url + "\n");
        driver.get(url);
    }

    public String requestTitle() {
        return driver.getTitle();
    }

    public List<WebElement> getElements(By locator) {

        assertFalse(" List is empty", driver.findElements(locator).isEmpty());
        return driver.findElements(locator);
    }

    public WebElement getElement(By locator) {

        assertTrue("Element doesn't exist", driver.findElement(locator).isDisplayed());
        return driver.findElement(locator);
    }

    public WebDriverWait getWait () {

        WebDriverWait wait = new WebDriverWait(driver, 10);
        return wait;
    }

    public void closeBrowser() {
        driver.quit();
    }

    public Actions addToBascket() {

        Actions action = new Actions(driver);
        return action;
    }

    public void refreshPage(String page) {

        driver.navigate().refresh();
        getWait().until(ExpectedConditions.titleIs(page + myStore));
    }
}
