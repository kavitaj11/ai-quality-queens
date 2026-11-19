package com.k11.mobile.pages.web;

import com.k11.mobile.config.MobileConfig;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Base for mobile-web screens. Mobile web is still a web page, so locators and waits are
 * the same as in Selenium; the differences are the device, the viewport and the network
 * path to the server (10.0.2.2 from an emulator).
 */
public abstract class WebScreen {

    protected final AppiumDriver driver;
    protected final WebDriverWait wait;

    protected WebScreen(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, MobileConfig.timeout());
    }

    protected static By testId(String id) {
        return By.cssSelector("[data-testid='" + id + "']");
    }

    protected WebElement visible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected void tap(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    /** Tap something that loads a new page, then wait until the old page is gone. */
    protected void tapAndWaitForNewPage(By locator) {
        WebElement oldPage = driver.findElement(By.tagName("html"));
        tap(locator);
        wait.until(ExpectedConditions.stalenessOf(oldPage));
    }

    protected void type(By locator, String text) {
        WebElement field = visible(locator);
        field.clear();
        field.sendKeys(text);
    }

    protected String text(By locator) {
        return visible(locator).getText().trim();
    }
}
