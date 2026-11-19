package com.k11.mobile.pages.web;

import io.appium.java_client.AppiumDriver;
import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

public class ProductsScreen extends WebScreen {

    private static final By CARD = testId("product-card");
    private static final By NAME = testId("product-name");

    public ProductsScreen(AppiumDriver driver) {
        super(driver);
    }

    /** Results load from a slow API behind a spinner: on a phone network, waiting well matters even more. */
    public ProductsScreen waitUntilLoaded() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(testId("spinner")));
        wait.until(d -> !d.findElement(testId("results")).getText().isBlank());
        return this;
    }

    public ProductsScreen search(String term) {
        type(testId("search-input"), term);
        tapAndWaitForNewPage(testId("search-button"));
        return waitUntilLoaded();
    }

    public List<String> productNames() {
        return driver.findElements(CARD).stream()
                .map(card -> card.findElement(NAME).getText().trim())
                .collect(Collectors.toList());
    }

    /** Opens the product, chooses a quantity and adds it to the cart. */
    public CartScreen addToCart(String productName, int quantity) {
        WebElement card = driver.findElements(CARD).stream()
                .filter(c -> c.findElement(NAME).getText().trim().equals(productName))
                .findFirst()
                .orElseThrow(() -> new AssertionError("No product named '" + productName + "'"));
        WebElement oldPage = driver.findElement(By.tagName("html"));
        card.findElement(testId("view-product")).click();
        wait.until(ExpectedConditions.stalenessOf(oldPage));

        new Select(visible(testId("quantity"))).selectByValue(String.valueOf(quantity));
        tapAndWaitForNewPage(testId("add-to-cart"));
        return new CartScreen(driver).waitUntilLoaded();
    }
}
