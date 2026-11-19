package com.k11.pwj.services;

import com.k11.pwj.json.Json;
import com.k11.pwj.json.SchemaValidator;
import com.k11.pwj.models.Order;
import com.k11.pwj.models.ProductSearch;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import java.util.Map;

/**
 * Service object for the shop's REST API: the API equivalent of a page object. Tests and step
 * definitions call business methods; they never build URLs, headers or JSON bodies themselves.
 *
 * It wraps any APIRequestContext:
 *  - context.request() shares cookies with a browser context, so an API login logs the page in;
 *  - playwright.request().newContext(...) is a standalone client for pure API tests.
 *
 * Methods named like the business action return typed results and fail fast with the response
 * body in the message. The raw* methods return the APIResponse for negative tests.
 */
public class ShopApi {

    private final APIRequestContext api;

    public ShopApi(APIRequestContext api) {
        this.api = api;
    }

    // ---- authentication -------------------------------------------------------------------
    public ShopApi login(String username, String password) {
        expectStatus(rawLogin(username, password), 200, "login as " + username);
        return this;
    }

    public APIResponse rawLogin(String username, String password) {
        return api.post("/api/login", RequestOptions.create()
                .setData(Map.of("username", username, "password", password)));
    }

    // ---- products ---------------------------------------------------------------------------
    public ProductSearch searchProducts(String term) {
        APIResponse response = api.get("/api/products", RequestOptions.create().setQueryParam("q", term));
        expectStatus(response, 200, "search for '" + term + "'");
        SchemaValidator.assertMatches("product-search", response.text());
        return Json.read(response.text(), ProductSearch.class);
    }

    // ---- cart and orders ----------------------------------------------------------------------
    public ShopApi addToCart(int productId, int quantity) {
        expectStatus(rawAddToCart(productId, quantity), 201, "add product " + productId + " x" + quantity);
        return this;
    }

    public APIResponse rawAddToCart(int productId, int quantity) {
        return api.post("/api/cart", RequestOptions.create()
                .setData(Map.of("productId", productId, "quantity", quantity)));
    }

    /** Places the order and checks the response against the order schema before mapping it. */
    public Order checkout() {
        APIResponse response = rawCheckout();
        expectStatus(response, 201, "checkout");
        SchemaValidator.assertMatches("order", response.text());
        return Json.read(response.text(), Order.class);
    }

    public APIResponse rawCheckout() {
        return api.post("/api/checkout");
    }

    public Order getOrder(String orderId) {
        APIResponse response = rawGetOrder(orderId);
        expectStatus(response, 200, "get order " + orderId);
        SchemaValidator.assertMatches("order", response.text());
        return Json.read(response.text(), Order.class);
    }

    public APIResponse rawGetOrder(String orderId) {
        return api.get("/api/orders/" + orderId);
    }

    private static void expectStatus(APIResponse response, int expected, String action) {
        if (response.status() != expected) {
            throw new AssertionError("Expected HTTP " + expected + " for " + action + " but got "
                    + response.status() + ": " + response.text());
        }
    }
}
