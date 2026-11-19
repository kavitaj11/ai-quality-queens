package com.k11.shop.pages;

import java.math.BigDecimal;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class OrderPage extends BasePage {

    private static final By CONFIRMATION = testId("order-confirmation");
    private static final By ORDER_ID = testId("order-id");
    private static final By STATUS = testId("order-status");
    private static final By TOTAL = testId("order-total");

    public OrderPage(WebDriver driver) {
        super(driver);
    }

    public OrderPage waitUntilLoaded() {
        visible(CONFIRMATION);
        return this;
    }

    public String orderId() {
        return text(ORDER_ID);
    }

    public String status() {
        return text(STATUS);
    }

    public BigDecimal total() {
        return Money.parse(text(TOTAL));
    }
}
