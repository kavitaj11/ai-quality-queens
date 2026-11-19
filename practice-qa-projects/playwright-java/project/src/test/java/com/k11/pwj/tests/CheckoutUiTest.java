package com.k11.pwj.tests;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

import com.k11.pwj.config.Config;
import com.k11.pwj.db.ShopDb;
import com.k11.pwj.models.Order;
import com.k11.pwj.pages.CartPage;
import com.k11.pwj.pages.OrderPage;
import com.k11.pwj.support.PlaywrightExtension;
import com.k11.pwj.support.ShopSession;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * UI test with API setup: login and cart are created through context.request() (which shares
 * the page's cookies), and only checkout goes through the UI. Then the UI, the API and the
 * database must all agree on the order.
 */
@Tag("ui")
@ExtendWith(PlaywrightExtension.class)
class CheckoutUiTest {

    @Test
    void checkoutOfAnApiBuiltCartAgreesAcrossUiApiAndDatabase(ShopSession session) {
        session.ui().login("junit-shopper", Config.password())
                .addToCart(1001, 2)          // Blue Mug   2 x 12.50
                .addToCart(1006, 1);         // Eco Bottle 1 x 15.25

        CartPage cart = new CartPage(session.page()).open();
        assertThat(cart.rows()).hasCount(2);
        assertThat(cart.total()).isEqualByComparingTo("40.25");

        OrderPage orderPage = cart.checkout();
        assertThat(orderPage.status()).hasText("CONFIRMED");
        String orderId = orderPage.orderId();

        Order fromApi = session.ui().getOrder(orderId);              // schema-validated
        assertThat(fromApi.total()).isEqualByComparingTo(orderPage.total());

        ShopDb.OrderRow fromDb = new ShopDb().findOrder(orderId).orElseThrow();
        assertThat(fromDb.total()).isEqualByComparingTo("40.25");
        assertThat(fromDb.username()).isEqualTo("junit-shopper");
        assertThat(new CartPage(session.page()).open().emptyMessage()).isVisible();
    }
}
