package guide;
public class ShippingTest {
    public static void main(String[] args) {
        int[][] cases = {{0, 499}, {4999, 499}, {5000, 0}, {5001, 0}};
        for (int[] row : cases) {
            int actual = Shipping.fee(row[0]);
            if (actual != row[1]) {
                throw new AssertionError("Subtotal " + row[0] + ": expected " + row[1] + ", got " + actual);
            }
        }
        try {
            Shipping.fee(-1);
            throw new AssertionError("Expected negative subtotal to be rejected");
        } catch (IllegalArgumentException expected) {
            System.out.println("All shipping checks passed");
        }
    }
}
