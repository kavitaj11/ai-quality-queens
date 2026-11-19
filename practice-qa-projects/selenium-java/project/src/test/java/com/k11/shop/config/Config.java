package com.k11.shop.config;

import java.time.Duration;

/**
 * One place for run settings. Each value comes from a JVM system property (-Dname=value),
 * then an environment variable, then a default, so the same tests run locally and in CI.
 */
public final class Config {

    private Config() {
    }

    public static String baseUrl() {
        return get("baseUrl", "BASE_URL", "http://127.0.0.1:8091");
    }

    /** chrome, edge or firefox. */
    public static String browser() {
        return get("browser", "BROWSER", "chrome").toLowerCase();
    }

    public static boolean headless() {
        return Boolean.parseBoolean(get("headless", "HEADLESS", "true"));
    }

    /** Optional Selenium Grid URL, for example http://localhost:4444. Empty means a local browser. */
    public static String gridUrl() {
        return get("gridUrl", "GRID_URL", "");
    }

    public static Duration timeout() {
        return Duration.ofSeconds(Long.parseLong(get("timeoutSeconds", "TIMEOUT_SECONDS", "10")));
    }

    public static String password() {
        return get("shopPassword", "SHOP_PASSWORD", "Passw0rd!");
    }

    private static String get(String property, String envVar, String fallback) {
        String value = System.getProperty(property);
        if (value == null || value.isBlank()) {
            value = System.getenv(envVar);
        }
        return (value == null || value.isBlank()) ? fallback : value.trim();
    }
}
