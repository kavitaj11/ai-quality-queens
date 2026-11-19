package com.k11.shop.tests;

import static org.assertj.core.api.Assertions.assertThat;

import com.k11.shop.api.ShopApi;
import com.k11.shop.config.Config;
import com.k11.shop.pages.ProductsPage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/** Search results load asynchronously, so these tests exercise explicit waits. */
public class SearchTests extends BaseTest {

    /** TestNG runs the parent's @BeforeMethod (startBrowser) first, so the driver exists here. */
    @BeforeMethod(alwaysRun = true)
    public void logInThroughApi() {
        new ShopApi().login("search-user", Config.password()).applySessionTo(driver());
    }

    @Test(groups = {"smoke", "regression"})
    public void searchReturnsOnlyMatchingProducts() {
        ProductsPage products = new ProductsPage(driver()).open().search("lamp");

        assertThat(products.productNames()).containsExactlyInAnyOrder("Desk Lamp", "Smart Lamp");
        assertThat(products.resultCount()).isEqualTo("2 result(s)");
    }

    @Test(groups = "regression")
    public void searchIsCaseInsensitiveAndMatchesCategory() {
        ProductsPage products = new ProductsPage(driver()).open().search("OFFICE");

        assertThat(products.productNames()).contains("Desk Lamp", "Smart Lamp", "Ergonomic Chair");
    }

    @Test(groups = "regression")
    public void searchWithNoMatchShowsMessage() {
        ProductsPage products = new ProductsPage(driver()).open().search("unicorn");

        assertThat(products.noResultsShown()).isTrue();
        assertThat(products.productNames()).isEmpty();
    }
}
