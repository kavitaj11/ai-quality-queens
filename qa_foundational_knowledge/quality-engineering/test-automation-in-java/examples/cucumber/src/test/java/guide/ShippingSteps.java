package guide;
import io.cucumber.java.en.*;
import static org.junit.Assert.assertEquals;

public class ShippingSteps {
    private int subtotal;
    private int fee;

    @Given("a subtotal of {int} cents")
    public void subtotal(int cents) { subtotal = cents; }

    @When("shipping is calculated")
    public void calculate() { fee = Shipping.fee(subtotal); }

    @Then("the fee is {int} cents")
    public void fee(int expected) { assertEquals(expected, fee); }
}
