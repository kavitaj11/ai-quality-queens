import java.util.*;
/**
 * A set records membership without multiplicity. Try inserting the same ID twice: the boolean result distinguishes a new event from one already observed.
 */
public class SetDemo {
    public static void main(String[] args) {
        Set<String> seen=new HashSet<>(); for(String id:List.of("a","a")) System.out.println(seen.add(id));
    }
}
