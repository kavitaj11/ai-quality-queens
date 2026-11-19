package com.k11.pwj.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import java.math.BigDecimal;

public class OrderPage {

    private final Locator orderId;
    private final Locator status;
    private final Locator total;

    public OrderPage(Page page) {
        this.orderId = page.getByTestId("order-id");
        this.status = page.getByTestId("order-status");
        this.total = page.getByTestId("order-total");
    }

    public String orderId() {
        return orderId.innerText().trim();
    }

    public Locator status() {
        return status;
    }

    public BigDecimal total() {
        return Money.parse(total.innerText());
    }
}
