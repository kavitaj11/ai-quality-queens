package com.k11.pwj.steps;

import static org.assertj.core.api.Assertions.assertThat;

import com.k11.pwj.db.ShopDb;
import com.k11.pwj.support.TestContext;
import io.cucumber.java.en.Then;
import java.math.BigDecimal;

/** Database checks shared by UI and API scenarios: both put the order id in the TestContext. */
public class DatabaseSteps {

    private final TestContext ctx;

    public DatabaseSteps(TestContext ctx) {
        this.ctx = ctx;
    }

    @Then("the database holds the order with status {string} and total {string}")
    public void theDatabaseHoldsTheOrder(String status, String total) {
        ShopDb.OrderRow row = ctx.db().findOrder(ctx.lastOrderId)
                .orElseThrow(() -> new AssertionError("Order " + ctx.lastOrderId + " is not in the database"));
        assertThat(row.status()).isEqualTo(status);
        assertThat(row.username()).isEqualTo(ctx.username);
        assertThat(row.total()).isEqualByComparingTo(total);
    }

    @Then("the database total equals the sum of the order lines")
    public void theDatabaseTotalEqualsTheLines() {
        assertThat(ctx.db().totalMinusLines(ctx.lastOrderId)).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(ctx.db().orderLines(ctx.lastOrderId)).hasSize(ctx.lastOrder.lines().size());
    }

    @Then("the order has exactly {int} {string} audit entry")
    public void theOrderHasAuditEntries(int count, String action) {
        assertThat(ctx.db().auditCount(ctx.lastOrderId, action)).isEqualTo(count);
    }
}
