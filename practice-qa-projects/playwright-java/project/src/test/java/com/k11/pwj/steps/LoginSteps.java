package com.k11.pwj.steps;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.k11.pwj.config.Config;
import com.k11.pwj.pages.LoginPage;
import com.k11.pwj.pages.ProductsPage;
import com.k11.pwj.support.TestContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.regex.Pattern;

/** Thin glue: every step calls a page object. No locators, no waits, no raw Playwright here. */
public class LoginSteps {

    private final TestContext ctx;
    private LoginPage login;
    private ProductsPage products;

    public LoginSteps(TestContext ctx) {
        this.ctx = ctx;
    }

    @Given("I am on the login page")
    public void iAmOnTheLoginPage() {
        login = new LoginPage(ctx.page()).open();
    }

    @When("I log in as {string} with the valid password")
    public void iLogInWithTheValidPassword(String username) {
        products = login.loginAs(username, Config.password());
    }

    @When("I log in as {string} with password {string}")
    public void iLogInWithPassword(String username, String password) {
        login.submit(username, password);
    }

    @Then("I see the products page with the greeting {string}")
    public void iSeeTheGreeting(String greeting) {
        assertThat(products.welcome()).hasText(greeting);
    }

    @Then("I see the login error {string}")
    public void iSeeTheLoginError(String message) {
        assertThat(login.error()).hasText(message);
        assertThat(ctx.page()).hasURL(Pattern.compile("/login$"));
    }
}
