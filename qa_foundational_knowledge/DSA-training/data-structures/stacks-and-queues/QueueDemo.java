import java.util.*;
/**
 * Use a queue when arrival order matters. Offer first and second, then remove one element. Compare remove with poll when the queue becomes empty.
 */
public class QueueDemo {
    public static void main(String[] args) {
        Queue<String> q=new ArrayDeque<>(); q.offer("first"); q.offer("second"); System.out.println(q.remove());
    }
}
