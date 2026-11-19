import java.util.*;
/**
 * Use ArrayDeque as a last-in, first-out stack. Push start and finish; predict the first pop and compare it with a queue over the same events.
 */
public class StacksDemo {
    public static void main(String[] args) {
        Deque<String> s=new ArrayDeque<>(); s.push("start"); s.push("finish"); System.out.println(s.pop());
    }
}
