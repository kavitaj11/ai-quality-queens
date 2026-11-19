package com.k11.mobile.driver;

import com.k11.mobile.config.MobileConfig;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.chromium.options.ChromiumOptions;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Creates one Appium session per thread. Appium is a server: the test sends W3C WebDriver
 * commands to it, and the "automationName" capability picks the driver that does the work
 * (UiAutomator2 for Android, XCUITest for iOS, Chromium for desktop Chrome, ...).
 */
public final class DriverFactory {

    private static final ThreadLocal<AppiumDriver> DRIVER = new ThreadLocal<>();

    private DriverFactory() {
    }

    public static AppiumDriver getDriver() {
        AppiumDriver driver = DRIVER.get();
        if (driver == null) {
            throw new IllegalStateException("No Appium session for this thread. Call DriverFactory.start() first.");
        }
        return driver;
    }

    public static AppiumDriver start() {
        AppiumDriver driver;
        switch (MobileConfig.platform()) {
            case "android-native":
                driver = new AndroidDriver(server(), androidSettingsApp());
                break;
            case "android-chrome":
                driver = new AndroidDriver(server(), androidChrome());
                break;
            case "desktop-chromium":
                driver = new AppiumDriver(server(), desktopChromium());
                break;
            default:
                throw new IllegalArgumentException("Unknown platform: " + MobileConfig.platform());
        }
        DRIVER.set(driver);
        return driver;
    }

    public static void quit() {
        AppiumDriver driver = DRIVER.get();
        if (driver != null) {
            try {
                driver.quit();
            } finally {
                DRIVER.remove();
            }
        }
    }

    /** A native app that every Android emulator already has: the Settings app. */
    private static UiAutomator2Options androidSettingsApp() {
        return new UiAutomator2Options()
                .setDeviceName(MobileConfig.deviceName())
                .setAppPackage("com.android.settings")
                .setAppActivity(".Settings")
                .setNoReset(true)                       // don't clear the app's data between sessions
                .setNewCommandTimeout(java.time.Duration.ofSeconds(120));
    }

    /** Mobile web: Chrome on the Android device, driven through UiAutomator2 + chromedriver. */
    private static UiAutomator2Options androidChrome() {
        UiAutomator2Options options = new UiAutomator2Options()
                .setDeviceName(MobileConfig.deviceName())
                .withBrowserName("Chrome");
        options.setCapability("appium:chromedriverAutodownload", true);  // needs --allow-insecure on the server
        return options;
    }

    /**
     * Desktop Chrome through Appium's Chromium driver. Used in this tutorial to run the
     * mobile-web tests through a real Appium server on a computer without an Android emulator.
     * Chrome's mobile emulation makes it render the phone layout.
     */
    private static ChromiumOptions desktopChromium() {
        // Appium requires platformName on every session; the Chromium driver accepts windows, mac or linux.
        String os = System.getProperty("os.name").toLowerCase();
        String platformName = os.contains("win") ? "windows" : os.contains("mac") ? "mac" : "linux";
        ChromiumOptions options = new ChromiumOptions().withBrowserName("chrome");
        options.setPlatformName(platformName);
        List<String> args = MobileConfig.headless()
                ? List.of("--headless=new", "--window-size=412,915")
                : List.of("--window-size=412,915");
        options.setCapability("goog:chromeOptions", Map.of(
                "args", args,
                "mobileEmulation", Map.of("deviceName", "Pixel 7")));
        options.setCapability("appium:autodownloadEnabled", true);
        return options;
    }

    private static URL server() {
        try {
            return URI.create(MobileConfig.appiumUrl()).toURL();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Bad appiumUrl: " + MobileConfig.appiumUrl(), e);
        }
    }
}
