package com.k11.shop.bdd;

import static org.assertj.core.api.Assertions.assertThat;

import com.k11.shop.api.ShopApi;
import com.k11.shop.config.Config;
import com.k11.shop.driver.DriverFactory;
import com.k11.shop.pages.CartPage;
import com.k11.shop.pages.Money;
import com.k11.shop.pages.OrderPage;
import com.k11.shop.pages.ProductsPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;

/**
 * Step definitions stay thin: each step calls page objects, never raw Selenium.
 * Cucumber creates a new instance of this class for every scenario, so these fields are
 * per-scenario state. With several step classes, share state with dependency injection
 * (cucumber-picocontainer) instead of static fields.
 */
public class ShoppingSteps {

    private ProductsPage products;
    private CartPage cart;
    private OrderPage order;

    private WebDriver driver() {
        return DriverFactory.getDriver();
    }

    @Given("I am logged in as {string}")
    public void iAmLoggedInAs(String username) {
        new ShopApi().login(username, Config.password()).applySessionTo(driver());
        products = new ProductsPage(driver()).open();
    }

    @When("I search for {string}")
    public void iSearchFor(String term) {
        products = products.search(term);
    }

    @When("I add {int} of {string} to my cart")
    public void iAddToMyCart(int quantity, String productName) {
        cart = products.openProduct(productName).chooseQuantity(quantity).addToCart();
    }

    @Then("my cart total is {string}")
    public void myCartTotalIs(String expected) {
        assertThat(cart.total()).isEqualByComparingTo(Money.parse(expected));
    }

    @When("I check out")
    public void iCheckOut() {
        order = cart.checkout();
    }

    @Then("my order is confirmed with a total of {string}")
    public void myOrderIsConfirmed(String expected) {
        assertThat(order.status()).isEqualTo("CONFIRMED");
        assertThat(order.total()).isEqualByComparingTo(Money.parse(expected));
    }
}
