package com.k11.shop.tests;

import static org.assertj.core.api.Assertions.assertThat;

import com.k11.shop.config.Config;
import com.k11.shop.pages.LoginPage;
import com.k11.shop.pages.ProductsPage;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LoginTests extends BaseTest {

    @Test(groups = {"smoke", "regression"}, description = "A valid user lands on the products page")
    public void validUserCanLogIn() {
        ProductsPage products = new LoginPage(driver()).open().loginAs("alice", Config.password());

        assertThat(products.welcomeText()).isEqualTo("Hi, alice");
        assertThat(driver().getCurrentUrl()).endsWith("/products");
    }

    /** One test method, many rows of data: TestNG's DataProvider. */
    @DataProvider(name = "invalidLogins")
    public Object[][] invalidLogins() {
        return new Object[][] {
            {"alice", "wrong-password", "Invalid username or password."},
            {"", "Passw0rd!", "Username and password are required."},
            {"alice", "", "Username and password are required."},
            {"locked", "Passw0rd!", "This account is locked. Contact support."},
        };
    }

    @Test(dataProvider = "invalidLogins", groups = "regression",
          description = "Invalid logins stay on the login page with the right message")
    public void invalidLoginShowsError(String username, String password, String expectedError) {
        LoginPage login = new LoginPage(driver()).open().loginExpectingError(username, password);

        assertThat(login.errorMessage()).isEqualTo(expectedError);
        assertThat(driver().getCurrentUrl()).endsWith("/login");
    }
}
