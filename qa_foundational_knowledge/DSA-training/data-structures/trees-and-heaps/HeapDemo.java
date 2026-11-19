import java.util.*;
/**
 * A PriorityQueue exposes the smallest remaining item at its head. Predict successive removals of 7,1,3; iterating the heap itself does not guarantee sorted order.
 */
public class HeapDemo {
    public static void main(String[] args) {
        PriorityQueue<Integer> heap=new PriorityQueue<>(List.of(7,1,3)); while(!heap.isEmpty()) System.out.println(heap.remove());
    }
}
