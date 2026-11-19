package com.k11.pwj.steps;

import static org.assertj.core.api.Assertions.assertThat;

import com.k11.pwj.config.Config;
import com.k11.pwj.json.Json;
import com.k11.pwj.json.SchemaValidator;
import com.k11.pwj.models.Order;
import com.k11.pwj.models.Product;
import com.k11.pwj.support.TestContext;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.math.BigDecimal;
import java.util.Map;

/** API steps: everything goes through the ShopApi service object over APIRequestContext. */
public class OrderApiSteps {

    private final TestContext ctx;

    public OrderApiSteps(TestContext ctx) {
        this.ctx = ctx;
    }

    @Given("I am signed in to the API as {string}")
    public void iAmSignedInToTheApi(String username) {
        ctx.username = username;
        ctx.userApi = ctx.session().newApiClient().login(username, Config.password());
    }

    /** A DataTable: each row is a product name and quantity. Names are resolved to IDs through the API. */
    @Given("my cart contains:")
    public void myCartContains(DataTable table) {
        for (Map<String, String> row : table.asMaps()) {
            String name = row.get("product");
            Product product = ctx.userApi.searchProducts(name).items().stream()
                    .filter(p -> p.name().equals(name))
                    .findFirst()
                    .orElseThrow(() -> new AssertionError("No product named '" + name + "'"));
            ctx.userApi.addToCart(product.id(), Integer.parseInt(row.get("quantity")));
        }
    }

    @When("I check out through the API")
    public void iCheckOutThroughTheApi() {
        ctx.lastResponse = ctx.userApi.rawCheckout();
        if (ctx.lastResponse.status() == 201) {
            ctx.lastOrder = Json.read(ctx.lastResponse.text(), Order.class);
            ctx.lastOrderId = ctx.lastOrder.orderId();
        }
    }

    @When("another signed-in user asks for that order")
    public void anotherUserAsksForThatOrder() {
        ctx.lastResponse = ctx.session().newApiClient()
                .login("someone-else", Config.password())
                .rawGetOrder(ctx.lastOrderId);
    }

    @When("an anonymous client asks for order {string}")
    public void anAnonymousClientAsks(String orderId) {
        ctx.lastResponse = ctx.session().newApiClient().rawGetOrder(orderId);
    }

    @Then("the API responds with status {int}")
    public void theApiRespondsWith(int status) {
        assertThat(ctx.lastResponse.status()).as("HTTP status; body: %s", ctx.lastResponse.text()).isEqualTo(status);
    }

    @Then("the API responds with status {int} and an error containing {string}")
    public void theApiRespondsWithError(int status, String message) {
        theApiRespondsWith(status);
        assertThat(Json.tree(ctx.lastResponse.text()).path("error").asText()).contains(message);
    }

    @Then("the response matches the {string} schema")
    public void theResponseMatchesTheSchema(String schemaName) {
        SchemaValidator.assertMatches(schemaName, ctx.lastResponse.text());
    }

    @Then("the order total is {string}")
    public void theOrderTotalIs(String expected) {
        assertThat(ctx.lastOrder.total()).isEqualByComparingTo(expected);
    }

    @Then("every order line total is quantity times unit price")
    public void everyLineTotalIsQuantityTimesPrice() {
        for (Order.OrderLine line : ctx.lastOrder.lines()) {
            assertThat(line.lineTotal())
                    .as("line total for %s", line.name())
                    .isEqualByComparingTo(line.unitPrice().multiply(BigDecimal.valueOf(line.quantity())));
        }
        BigDecimal sum = ctx.lastOrder.lines().stream().map(Order.OrderLine::lineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        assertThat(ctx.lastOrder.total()).as("order total = sum of lines").isEqualByComparingTo(sum);
    }
}
