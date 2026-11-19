package com.k11.pwj.steps;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

import com.k11.pwj.config.Config;
import com.k11.pwj.pages.CartPage;
import com.k11.pwj.pages.Money;
import com.k11.pwj.pages.OrderPage;
import com.k11.pwj.pages.ProductsPage;
import com.k11.pwj.support.TestContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ShoppingSteps {

    private final TestContext ctx;
    private ProductsPage products;
    private CartPage cart;
    private OrderPage order;

    public ShoppingSteps(TestContext ctx) {
        this.ctx = ctx;
    }

    /**
     * Log in through the API (fast, stable). ctx.ui() uses context.request(), which shares
     * cookies with the browser context, so the page is logged in too.
     */
    @Given("I am logged in as {string}")
    public void iAmLoggedInAs(String username) {
        ctx.username = username;
        ctx.ui().login(username, Config.password());
        products = new ProductsPage(ctx.page()).open();
    }

    @When("I search for {string}")
    public void iSearchFor(String term) {
        products = products.search(term);
    }

    @When("I add {int} of {string} to my cart")
    public void iAddToMyCart(int quantity, String productName) {
        cart = products.openProduct(productName).addToCart(quantity);
        assertThat(cart.toast()).hasText("Added to cart");
    }

    @Then("my cart total is {string}")
    public void myCartTotalIs(String expected) {
        assertThat(cart.total()).isEqualByComparingTo(Money.parse(expected));
    }

    @When("I check out")
    public void iCheckOut() {
        order = cart.checkout();
        ctx.lastOrderId = order.orderId();
    }

    @Then("my order is confirmed with a total of {string}")
    public void myOrderIsConfirmed(String expected) {
        assertThat(order.status()).hasText("CONFIRMED");
        assertThat(order.total()).isEqualByComparingTo(Money.parse(expected));
    }
}
