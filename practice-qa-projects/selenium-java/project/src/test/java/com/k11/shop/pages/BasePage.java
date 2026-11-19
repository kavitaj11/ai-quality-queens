package com.k11.shop.pages;

import com.k11.shop.config.Config;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** Shared helpers for every page object. Every interaction waits for the element first. */
public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Config.timeout());
    }

    /** Locator for a data-testid attribute: the most stable kind of locator. */
    protected static By testId(String id) {
        return By.cssSelector("[data-testid='" + id + "']");
    }

    protected WebElement visible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected void click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    /**
     * Click something that loads a new page, and wait until the old page is gone.
     * Without this, a wait that runs right after the click can match an element on the
     * OLD page (it has an h1 too) and the test races ahead of the browser.
     */
    protected void clickAndWaitForNewPage(By locator) {
        WebElement oldPage = driver.findElement(By.tagName("html"));
        click(locator);
        wait.until(ExpectedConditions.stalenessOf(oldPage));
    }

    protected void type(By locator, String text) {
        WebElement el = visible(locator);
        el.clear();
        el.sendKeys(text);
    }

    protected String text(By locator) {
        return visible(locator).getText().trim();
    }

    protected void open(String path) {
        driver.get(Config.baseUrl() + path);
    }
}
