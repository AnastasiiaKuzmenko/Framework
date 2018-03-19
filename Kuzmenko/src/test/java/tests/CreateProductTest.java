package tests;


import model.ProductData;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import utils.BaseTest;
import org.testng.annotations.Test;
import utils.GeneralActions;
import utils.Properties;

import java.util.List;

public class CreateProductTest extends BaseTest {
    private ProductData newProduct = ProductData.generate();

    @Test(dataProvider="getCredentials", dataProviderClass=GeneralActions.class)
    public void createNewProduct(String login, String password) {
        actions.waitForContentLoad();
        actions.login(login, password);
        actions.createProduct(newProduct.getName(), newProduct.getQty(), newProduct.getPrice());
    }

    @Test(dependsOnMethods={"createNewProduct"})
    public void CheckTheCreatedProduct(){
        driver.get(Properties.getBaseUrl());
        WebElement allGoods = driver.findElement(By.xpath("//a[@class = 'all-product-link pull-xs-left pull-md-right h4']"));
        allGoods.click();

        actions.ScrollToBottom();

        //List<WebElement> list = driver.findElements(By.cssSelector("ul.page-list.clearfix.text-xs-center li"));

        WebElement next = driver.findElement(By.xpath("//a[@rel='next']"));

        //next.click();
        WebDriverWait wait = new WebDriverWait(driver, 1000);

        Assert.assertTrue(actions.foundProduct(newProduct.getName()), "Element is not found");

        actions.ClickOnNewItem(newProduct.getName());

        actions.waitForContentLoad();
        Assert.assertEquals(driver.findElement(By.cssSelector("h1.h1")).getText().toLowerCase(), newProduct.getName().toLowerCase(), "Names are different");

        String actualPrice = driver.findElement(By.cssSelector("div.current-price span")).getAttribute("content");
        Assert.assertEquals(Float.valueOf(actualPrice), Float.valueOf(newProduct.getPrice().replace(",", ".")));

        String actualQty = driver.findElement(By.cssSelector("div.product-quantities span")).getText();

        Assert.assertEquals(Integer.valueOf(actualQty.substring(0, actualQty.length()-7)), newProduct.getQty());

        }


    // TODO implement logic to check product visibility on website
}
