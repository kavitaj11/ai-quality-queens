import java.util.*;
/**
 * Use linked nodes when the work happens at the ends. Predict the item removed after adding A at the front; contrast that operation with looking up a middle index.
 */
public class LinkedListDemo {
    public static void main(String[] args) {
        LinkedList<String> q=new LinkedList<>(List.of("B")); q.addFirst("A"); System.out.println(q.removeFirst());
    }
}
