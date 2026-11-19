package com.k11.pwj.pages;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import java.math.BigDecimal;
import java.util.regex.Pattern;

public class ProductPage {

    private final Page page;
    private final Locator title;
    private final Locator price;
    private final Locator quantity;
    private final Locator addToCart;

    public ProductPage(Page page) {
        this.page = page;
        this.title = page.getByTestId("product-title");
        this.price = page.getByTestId("product-detail-price");
        this.quantity = page.getByTestId("quantity");
        this.addToCart = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add to cart"));
    }

    public Locator title() {
        return title;
    }

    public BigDecimal unitPrice() {
        return Money.parse(price.innerText());
    }

    public CartPage addToCart(int qty) {
        quantity.selectOption(String.valueOf(qty));   // native <select>
        addToCart.click();
        assertThat(page).hasURL(Pattern.compile("/cart\\?added=1$"));
        return new CartPage(page);
    }
}
