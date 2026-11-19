package com.k11.shop.tests;

import static org.assertj.core.api.Assertions.assertThat;

import com.k11.shop.api.ShopApi;
import com.k11.shop.config.Config;
import com.k11.shop.pages.CartPage;
import com.k11.shop.pages.LoginPage;
import com.k11.shop.pages.OrderPage;
import com.k11.shop.pages.ProductPage;
import java.math.BigDecimal;
import org.testng.annotations.Test;

public class CheckoutTests extends BaseTest {

    /** The full journey through the UI: worth having, but keep only a few tests like this. */
    @Test(groups = {"smoke", "regression"})
    public void customerCanBuyAProductEndToEnd() {
        ProductPage product = new LoginPage(driver()).open()
                .loginAs("e2e-user", Config.password())
                .search("mug")
                .openProduct("Travel Mug");
        BigDecimal unitPrice = product.price();

        CartPage cart = product.chooseQuantity(2).addToCart();
        assertThat(cart.toastShown()).isTrue();
        assertThat(cart.quantityOf("Travel Mug")).isEqualTo(2);
        assertThat(cart.total()).isEqualByComparingTo(unitPrice.multiply(BigDecimal.valueOf(2)));

        OrderPage order = cart.checkout();
        assertThat(order.status()).isEqualTo("CONFIRMED");
        assertThat(order.orderId()).matches("ORD-[0-9A-F]{8}");
        assertThat(order.total()).isEqualByComparingTo("36.00");
    }

    /**
     * Same checkout, but login and cart are set up through the API. Only the behaviour
     * under test (checkout) goes through the UI, so the test is faster and less flaky.
     */
    @Test(groups = "regression")
    public void checkoutTotalsAMixedCart() {
        new ShopApi().login("api-user", Config.password())
                .addToCart(1001, 2)   // Blue Mug   2 x 12.50
                .addToCart(1006, 1)   // Eco Bottle 1 x 15.25
                .applySessionTo(driver());

        CartPage cart = new CartPage(driver()).open();
        assertThat(cart.lineCount()).isEqualTo(2);
        assertThat(cart.total()).isEqualByComparingTo("40.25");

        OrderPage order = cart.checkout();
        assertThat(order.total()).isEqualByComparingTo("40.25");
        assertThat(new CartPage(driver()).open().isEmpty()).isTrue();
    }
}
