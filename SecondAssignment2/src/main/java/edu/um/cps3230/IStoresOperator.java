package edu.um.cps3230;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class IStoresOperator {

    WebDriver driver;
    WebElement firstProduct;
    String firstProductText;

    public IStoresOperator(WebDriver driver) {
        this.driver = driver;
    }

    public void initializeWelcomePage() {
        driver.get("https://www.istores.cz/");
    }

    public void goToWelcome() {
        driver.findElement(By.xpath("//a[@class='d-block']")).sendKeys(Keys.ENTER);
    }

    public void goToCategory() {
        driver.findElement(By.xpath("//a[text() = 'iPhone ']")).sendKeys(Keys.ENTER);
    }

    public void selectProduct() {
        firstProduct = driver.findElement(By.xpath("//a[@data-cy='product-card-title-link']"));
        saveFirstProductText();
        firstProduct.sendKeys(Keys.ENTER);
    }

    private void saveFirstProductText() {
        firstProductText = firstProduct.getText();
    }

    public String returnProductText() {
        if (firstProductText == null){
            return "";
        }
            return firstProductText;
    }

    public void addToCart(){
        driver.findElement(By.xpath("//button[@data-cy='product-detail-add-to-cart-button']")).sendKeys(Keys.ENTER);
    }

    public void goToCart() throws InterruptedException {
        Thread.sleep(3500);
        driver.findElement(By.xpath("//a[@class='btn d-inline-flex position-relative overflow-hidden justify-content-center " +
                "btn-primary btn-normal align-items-center d-flex w-100 align-items-center mb-4']")).sendKeys(Keys.ENTER);

    }

    public void stayOnSite() throws InterruptedException {
        Thread.sleep(3500);
        driver.findElement(By.xpath("//button[@class='btn position-relative overflow-hidden d-inline-flex justify-content-center " +
                "btn-secondary-outline btn-normal align-items-center d-flex w-100 align-items-center']")).sendKeys(Keys.ENTER);

    }

    public void addToCartFromCategory() {
        driver.findElement(By.xpath("//button[@data-cy='product-card-add-to-cart-button']")).sendKeys(Keys.ENTER);
    }

}
