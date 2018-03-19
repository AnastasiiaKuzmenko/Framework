package utils;

import model.ProductData;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Contains main script actions that may be used in scripts.
 */
public class GeneralActions {
    private WebDriver driver;
    private WebDriverWait wait;

    public GeneralActions(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, 30);
    }

    @DataProvider
    public static Object[][] getCredentials() {
        return new Object[][] { { "webinar.test@gmail.com", "Xcg7299bnSmMuRLp9ITw" }};
    }

    /**
     * Logs in to Admin Panel.
     * @param login
     * @param password
     */

    public void login(String login, String password) {
        driver.get(Properties.getBaseAdminUrl());
        WebElement email = driver.findElement(By.name("email"));
        email.sendKeys(login);
        WebElement passwd = driver.findElement(By.name("passwd"));
        passwd.sendKeys(password);
        WebElement submit = driver.findElement(By.name("submitLogin"));
        submit.click();
    }

    public void createProduct(String title, int qty, String price) {

        Actions actions = new Actions(driver);
        WebElement catalogue = driver.findElement(By.id("subtab-AdminCatalog"));
        actions.moveToElement(catalogue).build().perform();
        WebElement goods = driver.findElement(By.linkText("товары"));
        goods.click();
        waitForContentLoad();
        WebElement newGood = driver.findElement(By.id("page-header-desc-configuration-add"));
        newGood.click();
        WebElement name = driver.findElement(By.id("form_step1_name_1"));
        name.sendKeys(title);
        WebElement quantity = driver.findElement(By.id("form_step1_qty_0_shortcut"));
        quantity.sendKeys("\b");
        quantity.sendKeys(String.valueOf(qty));
        WebElement priceCell = driver.findElement(By.id("form_step1_price_ttc_shortcut"));
        priceCell.sendKeys("\b");
        priceCell.sendKeys(price);

        WebElement activation = driver.findElement(By.cssSelector("div.switch-input"));
        activation.click();

        WebDriverWait wait = new WebDriverWait(driver, 10);
//
        WaitAndCloseMessage(actions);

        WebElement save = driver.findElement(By.id("submit"));
        save.click();

        WaitAndCloseMessage(actions);
    }



    private void WaitAndCloseMessage(Actions actions) {

        wait.until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return driver.findElement(By.xpath("//*[contains(text(), 'Настройки обновлены')]")).isDisplayed();
            }
        });
        actions.contextClick(driver.findElement(By.id("growls")));
    }

    /**
     * Waits until page loader disappears from the page
     */
    public void waitForContentLoad() {
        WebDriverWait wait = new WebDriverWait(driver, 30);

        wait.until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver wdriver) {
                return ((JavascriptExecutor) driver).executeScript(
                        "return document.readyState"
                ).equals("complete");
            }
        });
    }

    public boolean foundProduct(String productName)
    {
        List<WebElement> products = driver.findElements(By.cssSelector(".h3.product-title a"));

        for (WebElement product : products)
        {
            if (product.getText().contains(productName))
                return true;
        }
        return false;
    }

    public void ScrollToBottom() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,document.body.scrollHeight)");
    }

    public void ClickOnNewItem(String productName) {
        List<WebElement> items = driver.findElements(By.cssSelector("h1.h3.product-title a"));

        for (WebElement item: items) {
            if (item.getText().equals(productName)){
                item.click();
            }
        }
    }
}