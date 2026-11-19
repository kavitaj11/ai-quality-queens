package com.k11.mobile.pages.web;

import io.appium.java_client.AppiumDriver;
import java.math.BigDecimal;
import org.openqa.selenium.By;

public class CartScreen extends WebScreen {

    public CartScreen(AppiumDriver driver) {
        super(driver);
    }

    public CartScreen waitUntilLoaded() {
        visible(By.tagName("h1"));
        return this;
    }

    public BigDecimal total() {
        return money(text(testId("cart-total")));
    }

    /** Checks out and returns the confirmed order's total. */
    public BigDecimal checkout() {
        tapAndWaitForNewPage(testId("checkout"));
        visible(testId("order-confirmation"));
        if (!"CONFIRMED".equals(text(testId("order-status")))) {
            throw new AssertionError("Order was not confirmed");
        }
        return money(text(testId("order-total")));
    }

    private static BigDecimal money(String displayed) {
        return new BigDecimal(displayed.replace("$", "").replace(",", "").trim());
    }
}
