package com.k11.shop.api;

import com.k11.shop.config.Config;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

/**
 * Test setup through the API instead of the UI. Logging in and filling a cart through the
 * UI takes seconds per test; through the API it takes milliseconds and cannot flake.
 * Use the UI only for the behaviour the test is actually about.
 */
public final class ShopApi {

    private static final Pattern SID = Pattern.compile("\"sid\"\\s*:\\s*\"([0-9a-f]+)\"");
    private final HttpClient http = HttpClient.newHttpClient();
    private String sid;

    public ShopApi login(String username, String password) {
        HttpResponse<String> resp = post("/api/login",
                "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}");
        if (resp.statusCode() != 200) {
            throw new IllegalStateException("API login failed: " + resp.statusCode() + " " + resp.body());
        }
        Matcher m = SID.matcher(resp.body());
        if (!m.find()) {
            throw new IllegalStateException("No session id in login response: " + resp.body());
        }
        sid = m.group(1);
        return this;
    }

    public ShopApi addToCart(int productId, int quantity) {
        HttpResponse<String> resp = post("/api/cart",
                "{\"productId\":" + productId + ",\"quantity\":" + quantity + "}");
        if (resp.statusCode() != 201) {
            throw new IllegalStateException("Add to cart failed: " + resp.statusCode() + " " + resp.body());
        }
        return this;
    }

    /**
     * Hand the API session to the browser. A cookie can only be set for the domain that is
     * currently open, so load a cheap page of the app first.
     */
    public void applySessionTo(WebDriver driver) {
        driver.get(Config.baseUrl() + "/health");
        driver.manage().addCookie(new Cookie("sid", sid, "/"));
    }

    private HttpResponse<String> post(String path, String json) {
        HttpRequest.Builder req = HttpRequest.newBuilder(URI.create(Config.baseUrl() + path))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json));
        if (sid != null) {
            req.header("Cookie", "sid=" + sid);
        }
        try {
            return http.send(req.build(), HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new IllegalStateException("Cannot reach " + Config.baseUrl() + path, e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted calling " + path, e);
        }
    }
}
