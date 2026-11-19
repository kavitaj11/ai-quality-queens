package com.k11.pwj.pages;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import java.util.regex.Pattern;

public class LoginPage {

    private final Page page;
    private final Locator username;
    private final Locator password;
    private final Locator submit;
    private final Locator error;

    public LoginPage(Page page) {
        this.page = page;
        this.username = page.getByTestId("username");
        this.password = page.getByTestId("password");
        // Role + accessible name: how a user (and a screen reader) finds the button.
        this.submit = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Log in"));
        this.error = page.getByTestId("login-error");
    }

    public LoginPage open() {
        page.navigate("/login");
        return this;
    }

    public void submit(String user, String pass) {
        username.fill(user);
        password.fill(pass);
        submit.click();
    }

    /** Successful login lands on the products page; wait for its URL before returning it. */
    public ProductsPage loginAs(String user, String pass) {
        submit(user, pass);
        assertThat(page).hasURL(Pattern.compile("/products$"));
        return new ProductsPage(page).waitUntilLoaded();
    }

    public Locator error() {
        return error;
    }
}
