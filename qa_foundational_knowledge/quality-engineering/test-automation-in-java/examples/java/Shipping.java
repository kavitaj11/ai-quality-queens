package guide;
public class Shipping {
    public static int fee(int subtotalCents) {
        if (subtotalCents < 0) throw new IllegalArgumentException("Negative subtotal");
        return subtotalCents >= 5000 ? 0 : 499;
    }
}
