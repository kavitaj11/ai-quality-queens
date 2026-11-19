package com.k11.shop.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private static final By USERNAME = testId("username");
    private static final By PASSWORD = testId("password");
    private static final By LOGIN_BUTTON = testId("login-button");
    private static final By ERROR = testId("login-error");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open() {
        open("/login");
        return this;
    }

    /** Successful login lands on the products page, so this returns that page object. */
    public ProductsPage loginAs(String username, String password) {
        submit(username, password);
        return new ProductsPage(driver).waitUntilLoaded();
    }

    /** For negative tests: the form posts and the login page comes back with an error. */
    public LoginPage loginExpectingError(String username, String password) {
        submit(username, password);
        return this;
    }

    public String errorMessage() {
        return text(ERROR);
    }

    private void submit(String username, String password) {
        type(USERNAME, username);
        type(PASSWORD, password);
        clickAndWaitForNewPage(LOGIN_BUTTON);
    }
}
