package com.k11.pwj.pages;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import java.math.BigDecimal;
import java.util.regex.Pattern;

public class CartPage {

    private final Page page;
    private final Locator rows;
    private final Locator total;
    private final Locator toast;
    private final Locator empty;
    private final Locator checkout;

    public CartPage(Page page) {
        this.page = page;
        this.rows = page.getByTestId("cart-row");
        this.total = page.getByTestId("cart-total");
        this.toast = page.getByRole(AriaRole.STATUS);
        this.empty = page.getByTestId("empty-cart");
        this.checkout = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Checkout"));
    }

    public CartPage open() {
        page.navigate("/cart");
        return this;
    }

    public Locator rows() {
        return rows;
    }

    public Locator row(String productName) {
        return rows.filter(new Locator.FilterOptions().setHasText(productName));
    }

    public Locator toast() {
        return toast;
    }

    public Locator emptyMessage() {
        return empty;
    }

    public BigDecimal total() {
        assertThat(total).isVisible();
        return Money.parse(total.innerText());
    }

    public OrderPage checkout() {
        checkout.click();
        assertThat(page).hasURL(Pattern.compile("/orders/ORD-[0-9A-F]{8}$"));
        return new OrderPage(page);
    }
}
