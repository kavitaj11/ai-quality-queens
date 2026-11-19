package com.k11.pwj.config;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Run settings. Each value comes from a JVM system property (-Dname=value), then an
 * environment variable, then a default, so the same code runs on a laptop and in CI.
 * Secrets such as passwords come from the environment in real projects, never from code.
 */
public final class Config {

    private Config() {
    }

    public static String baseUrl() {
        return get("baseUrl", "BASE_URL", "http://127.0.0.1:8091");
    }

    /** chromium, firefox or webkit. */
    public static String browser() {
        return get("browser", "BROWSER", "chromium").toLowerCase();
    }

    public static boolean headless() {
        return Boolean.parseBoolean(get("headless", "HEADLESS", "true"));
    }

    public static double slowMoMillis() {
        return Double.parseDouble(get("slowMo", "SLOW_MO", "0"));
    }

    public static double timeoutMillis() {
        return Double.parseDouble(get("timeoutMillis", "TIMEOUT_MILLIS", "10000"));
    }

    public static String password() {
        return get("shopPassword", "SHOP_PASSWORD", "Passw0rd!");
    }

    /** The practice shop's SQLite file. Default: the shop's own folder in this repository. */
    public static Path dbPath() {
        return Paths.get(get("dbPath", "SHOP_DB_PATH", "../../practice-apps/shop-web/shop.db"))
                .toAbsolutePath().normalize();
    }

    /** Where traces and screenshots of failed tests are written. */
    public static Path artifactsDir() {
        return Paths.get("target", "playwright-artifacts");
    }

    private static String get(String property, String envVar, String fallback) {
        String value = System.getProperty(property);
        if (value == null || value.isBlank()) {
            value = System.getenv(envVar);
        }
        return (value == null || value.isBlank()) ? fallback : value.trim();
    }
}
