package com.k11.shop.pages;

import java.math.BigDecimal;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class ProductPage extends BasePage {

    private static final By TITLE = testId("product-title");
    private static final By PRICE = testId("product-detail-price");
    private static final By QUANTITY = testId("quantity");
    private static final By ADD_TO_CART = testId("add-to-cart");

    public ProductPage(WebDriver driver) {
        super(driver);
    }

    public ProductPage open(int productId) {
        open("/products/" + productId);
        return waitUntilLoaded();
    }

    public ProductPage waitUntilLoaded() {
        visible(TITLE);
        return this;
    }

    public String title() {
        return text(TITLE);
    }

    public BigDecimal price() {
        return Money.parse(text(PRICE));
    }

    /** A native <select> element: use Selenium's Select helper, not clicks on options. */
    public ProductPage chooseQuantity(int quantity) {
        new Select(visible(QUANTITY)).selectByValue(String.valueOf(quantity));
        return this;
    }

    public CartPage addToCart() {
        clickAndWaitForNewPage(ADD_TO_CART);
        return new CartPage(driver).waitUntilLoaded();
    }
}
