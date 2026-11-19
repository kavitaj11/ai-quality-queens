import java.util.*;
/**
 * Associate an event key with a count. Run the merge twice and inspect the old and new count; Hashtable synchronizes individual operations and disallows null keys.
 */
public class HashTableDemo {
    public static void main(String[] args) {
        Hashtable<String,Integer> h=new Hashtable<>(); h.merge("event",1,Integer::sum); System.out.println(h.get("event"));
    }
}
