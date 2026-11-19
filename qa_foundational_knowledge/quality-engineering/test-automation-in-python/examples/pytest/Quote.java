import guide.Shipping;
public class Quote {
    public static void main(String[] args) {
        System.out.println(Shipping.fee(Integer.parseInt(args[0])));
    }
}
