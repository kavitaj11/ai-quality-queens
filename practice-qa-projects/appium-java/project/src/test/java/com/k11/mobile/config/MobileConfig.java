package com.k11.mobile.config;

import java.time.Duration;

/** Run settings from -D properties, then environment variables, then defaults. */
public final class MobileConfig {

    private MobileConfig() {
    }

    /** android-native, android-chrome or desktop-chromium. */
    public static String platform() {
        return get("platform", "PLATFORM", "desktop-chromium").toLowerCase();
    }

    public static String appiumUrl() {
        return get("appiumUrl", "APPIUM_URL", "http://127.0.0.1:4723");
    }

    /** Name of the emulator or device, as shown by `adb devices` (for example emulator-5554). */
    public static String deviceName() {
        return get("deviceName", "DEVICE_NAME", "emulator-5554");
    }

    /**
     * Where the practice shop is, from the browser's point of view. An Android emulator
     * reaches the host computer at 10.0.2.2, not 127.0.0.1 (that would be the emulator itself).
     */
    public static String shopUrl() {
        String fallback = platform().startsWith("android") ? "http://10.0.2.2:8091" : "http://127.0.0.1:8091";
        return get("shopUrl", "SHOP_URL", fallback);
    }

    public static boolean headless() {
        return Boolean.parseBoolean(get("headless", "HEADLESS", "true"));
    }

    public static Duration timeout() {
        return Duration.ofSeconds(Long.parseLong(get("timeoutSeconds", "TIMEOUT_SECONDS", "15")));
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
