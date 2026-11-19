package com.k11.mobile.pages.nativeapp;

import com.k11.mobile.config.MobileConfig;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Page object for Android's built-in Settings app: a native screen, so there is no HTML.
 * Elements are found by accessibility id, resource id, or UiAutomator selectors.
 * Find these values with Appium Inspector.
 */
public class SettingsScreen {

    /** Every Settings row title uses this resource id. */
    private static final By ROW_TITLE = AppiumBy.id("android:id/title");

    private final AndroidDriver driver;
    private final WebDriverWait wait;

    public SettingsScreen(AndroidDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, MobileConfig.timeout());
    }

    public SettingsScreen waitUntilLoaded() {
        wait.until(ExpectedConditions.presenceOfElementLocated(ROW_TITLE));
        return this;
    }

    /**
     * Scroll until a row with this text is on screen. UiScrollable runs on the device,
     * which is faster and more reliable than swiping blindly from the test.
     */
    public WebElement scrollToRow(String text) {
        return driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))"
                        + ".scrollIntoView(new UiSelector().text(\"" + text + "\"))"));
    }

    public void openRow(String text) {
        scrollToRow(text).click();
    }

    /** True if any element on the current screen contains this text. */
    public boolean isShowingText(String text) {
        return !driver.findElements(AppiumBy.androidUIAutomator(
                "new UiSelector().textContains(\"" + text + "\")")).isEmpty();
    }

    public void goBack() {
        driver.navigate().back();
    }
}
