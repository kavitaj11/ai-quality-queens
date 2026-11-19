package com.k11.shop.pages;

import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ProductsPage extends BasePage {

    private static final By SEARCH_INPUT = testId("search-input");
    private static final By SEARCH_BUTTON = testId("search-button");
    private static final By SPINNER = testId("spinner");
    private static final By RESULT_COUNT = testId("result-count");
    private static final By CARD = testId("product-card");
    private static final By CARD_NAME = testId("product-name");
    private static final By NO_RESULTS = testId("no-results");
    private static final By WELCOME = testId("welcome");

    public ProductsPage(WebDriver driver) {
        super(driver);
    }

    public ProductsPage open() {
        open("/products");
        return waitUntilLoaded();
    }

    /**
     * Results arrive from an asynchronous API call (400-1200 ms). Wait for the real signal
     * that loading finished: the result count text appears. Never Thread.sleep.
     */
    public ProductsPage waitUntilLoaded() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(SPINNER));
        wait.until(d -> !d.findElement(RESULT_COUNT).getText().isBlank());
        return this;
    }

    public ProductsPage search(String term) {
        type(SEARCH_INPUT, term);
        clickAndWaitForNewPage(SEARCH_BUTTON);
        return waitUntilLoaded();
    }

    public String welcomeText() {
        return text(WELCOME);
    }

    public String resultCount() {
        return text(RESULT_COUNT);
    }

    public List<String> productNames() {
        return driver.findElements(CARD).stream()
                .map(card -> card.findElement(CARD_NAME).getText().trim())
                .collect(Collectors.toList());
    }

    public boolean noResultsShown() {
        return !driver.findElements(NO_RESULTS).isEmpty();
    }

    /** Open a product by its visible name, using a locator scoped to the matching card. */
    public ProductPage openProduct(String name) {
        WebElement card = driver.findElements(CARD).stream()
                .filter(c -> c.findElement(CARD_NAME).getText().trim().equals(name))
                .findFirst()
                .orElseThrow(() -> new AssertionError("No product card named '" + name + "'"));
        WebElement oldPage = driver.findElement(By.tagName("html"));
        card.findElement(testId("view-product")).click();
        wait.until(ExpectedConditions.stalenessOf(oldPage));
        return new ProductPage(driver).waitUntilLoaded();
    }
}
