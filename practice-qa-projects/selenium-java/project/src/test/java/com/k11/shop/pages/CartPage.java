package com.k11.shop.pages;

import java.math.BigDecimal;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CartPage extends BasePage {

    private static final By HEADING = By.tagName("h1");
    private static final By ROW = testId("cart-row");
    private static final By TOTAL = testId("cart-total");
    private static final By TOAST = testId("toast");
    private static final By CHECKOUT = testId("checkout");
    private static final By EMPTY = testId("empty-cart");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public CartPage open() {
        open("/cart");
        return waitUntilLoaded();
    }

    public CartPage waitUntilLoaded() {
        visible(HEADING);
        return this;
    }

    public boolean toastShown() {
        return !driver.findElements(TOAST).isEmpty();
    }

    public boolean isEmpty() {
        return !driver.findElements(EMPTY).isEmpty();
    }

    public int lineCount() {
        return driver.findElements(ROW).size();
    }

    public int quantityOf(String productName) {
        List<WebElement> rows = driver.findElements(ROW);
        return rows.stream()
                .filter(r -> r.findElement(testId("cart-item-name")).getText().trim().equals(productName))
                .map(r -> Integer.parseInt(r.findElement(testId("cart-item-qty")).getText().trim()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("'" + productName + "' is not in the cart"));
    }

    public BigDecimal total() {
        return Money.parse(text(TOTAL));
    }

    public OrderPage checkout() {
        clickAndWaitForNewPage(CHECKOUT);
        return new OrderPage(driver).waitUntilLoaded();
    }
}
