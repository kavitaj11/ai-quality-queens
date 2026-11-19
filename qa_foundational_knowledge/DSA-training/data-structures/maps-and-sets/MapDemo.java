import java.util.*;
/**
 * Program to Map while using a HashMap for frequency counting. Trace a,b,a and explain why the final count of a is two, regardless of iteration order.
 */
public class MapDemo {
    public static void main(String[] args) {
        Map<String,Integer> counts=new HashMap<>(); for(String id:List.of("a","b","a")) counts.merge(id,1,Integer::sum); System.out.println(counts.get("a"));
    }
}
