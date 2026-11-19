import java.util.*;
/**
 * An ArrayList grows on demand but inserting near the front shifts later elements. Predict what get(1) returns after inserting A before B and C.
 */
public class ArrayListDemo {
    public static void main(String[] args) {
        ArrayList<String> list=new ArrayList<>(List.of("B","C")); list.add(0,"A"); System.out.println(list.get(1));
    }
}
