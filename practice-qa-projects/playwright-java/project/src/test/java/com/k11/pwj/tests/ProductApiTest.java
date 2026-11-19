package com.k11.pwj.tests;

import static org.assertj.core.api.Assertions.assertThat;

import com.k11.pwj.config.Config;
import com.k11.pwj.db.ShopDb;
import com.k11.pwj.models.Product;
import com.k11.pwj.models.ProductSearch;
import com.k11.pwj.support.PlaywrightExtension;
import com.k11.pwj.support.ShopSession;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

/** Pure API tests: no browser page is used, only the standalone APIRequestContext client. */
@Tag("api")
@ExtendWith(PlaywrightExtension.class)
class ProductApiTest {

    /** Data-driven: one test method, one row per case in src/test/resources/testdata/product-search.csv. */
    @ParameterizedTest(name = "search \"{0}\" returns {1} product(s)")
    @CsvFileSource(resources = "/testdata/product-search.csv", numLinesToSkip = 1)
    void searchReturnsTheExpectedProducts(String term, int expectedCount, String expectedNames, ShopSession session) {
        ProductSearch result = session.newApiClient().searchProducts(term);   // also validates the JSON schema

        List<String> expected = expectedNames == null ? List.of() : Arrays.asList(expectedNames.split(";"));
        assertThat(result.count()).isEqualTo(expectedCount);
        assertThat(result.items()).extracting(Product::name).containsExactlyInAnyOrderElementsOf(expected);
    }

    /** API vs database: the price the API returns must be the price stored in the products table. */
    @Test
    void apiPricesMatchTheDatabase(ShopSession session) {
        ShopDb db = new ShopDb();
        for (Product product : session.newApiClient().searchProducts("").items()) {
            assertThat(product.price()).as("price of %s", product.name())
                    .isEqualByComparingTo(db.productPrice(product.id()));
        }
    }

    @Test
    void cartRejectsAQuantityOutOfRange(ShopSession session) {
        var api = session.newApiClient().login("validation-user", Config.password());

        assertThat(api.rawAddToCart(1001, 0).status()).isEqualTo(400);
        assertThat(api.rawAddToCart(1001, 6).status()).isEqualTo(400);
        assertThat(api.rawAddToCart(1001, 5).status()).isEqualTo(201);
    }
}
