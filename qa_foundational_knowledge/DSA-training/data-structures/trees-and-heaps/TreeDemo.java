import java.util.*;
/**
 * Use a TreeSet when sorted keys and range queries matter. Trace the subset bounds on 2,5,9: the lower bound is included and the upper one excluded.
 */
public class TreeDemo {
    public static void main(String[] args) {
        TreeSet<Integer> tree=new TreeSet<>(List.of(5,2,9)); System.out.println(tree.subSet(2,true,9,false));
    }
}
