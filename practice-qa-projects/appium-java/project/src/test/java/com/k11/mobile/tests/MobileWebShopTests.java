package com.k11.mobile.tests;

import static org.assertj.core.api.Assertions.assertThat;

import com.k11.mobile.config.MobileConfig;
import com.k11.mobile.pages.web.CartScreen;
import com.k11.mobile.pages.web.LoginScreen;
import com.k11.mobile.pages.web.ProductsScreen;
import java.math.BigDecimal;
import org.openqa.selenium.Dimension;
import org.testng.annotations.Test;

/**
 * Mobile web tests: the practice shop in a mobile browser.
 * Run on Chrome for Android with -Dplatform=android-chrome, or on desktop Chrome in phone
 * emulation through Appium's Chromium driver with -Dplatform=desktop-chromium (the default).
 */
public class MobileWebShopTests extends BaseMobileTest {

    @Test(groups = "mobile-web")
    public void pageUsesAPhoneSizedViewport() {
        new LoginScreen(driver()).open();

        Long viewportWidth = (Long) driver().executeScript("return window.innerWidth;");
        assertThat(viewportWidth).isLessThan(600L);
    }

    @Test(groups = "mobile-web")
    public void wrongPasswordShowsError() {
        String error = new LoginScreen(driver()).open().loginExpectingError("mobile-user", "nope");

        assertThat(error).isEqualTo("Invalid username or password.");
    }

    @Test(groups = "mobile-web")
    public void shopperCanSearchAndCheckOutOnAPhone() {
        ProductsScreen products = new LoginScreen(driver()).open()
                .loginAs("mobile-user", MobileConfig.password())
                .search("lamp");
        assertThat(products.productNames()).containsExactlyInAnyOrder("Desk Lamp", "Smart Lamp");

        CartScreen cart = products.addToCart("Desk Lamp", 2);
        assertThat(cart.total()).isEqualByComparingTo("69.98");

        BigDecimal paid = cart.checkout();
        assertThat(paid).isEqualByComparingTo("69.98");
    }

    @Test(groups = "mobile-web")
    public void windowIsPortrait() {
        new LoginScreen(driver()).open();
        Dimension size = driver().manage().window().getSize();

        assertThat(size.height).isGreaterThan(size.width);
    }
}
