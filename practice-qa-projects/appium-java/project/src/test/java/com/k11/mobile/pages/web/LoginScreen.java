package com.k11.mobile.pages.web;

import com.k11.mobile.config.MobileConfig;
import io.appium.java_client.AppiumDriver;

public class LoginScreen extends WebScreen {

    public LoginScreen(AppiumDriver driver) {
        super(driver);
    }

    public LoginScreen open() {
        driver.get(MobileConfig.shopUrl() + "/login");
        visible(testId("login-form"));
        return this;
    }

    public ProductsScreen loginAs(String username, String password) {
        submit(username, password);
        return new ProductsScreen(driver).waitUntilLoaded();
    }

    public String loginExpectingError(String username, String password) {
        submit(username, password);
        return text(testId("login-error"));
    }

    private void submit(String username, String password) {
        type(testId("username"), username);
        type(testId("password"), password);
        tapAndWaitForNewPage(testId("login-button"));
    }
}
