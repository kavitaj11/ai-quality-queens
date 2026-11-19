import java.util.*;
/**
 * Balance a max-heap for the lower half against a min-heap for the upper half. Trace [5,1,8]; use both roots when the sizes match. Shared heaps mean concurrent calls need isolation.
 */
public class OnlineMedianAlternative {
/*
* Asymptotic complexity in terms of `n` = size of the input array:
* Time: O(n * log(n)).
* Auxiliary space: O(n).
* Total space: O(n).
*/

// To store the smaller half of the input numbers.
public static PriorityQueue<Integer> max_heap = new PriorityQueue<Integer>((a, b) -> {
   return Integer.compare(b, a);
});

// To store the larger half of the input numbers.
public static PriorityQueue<Integer> min_heap = new PriorityQueue<Integer>();

static void add_new_element(int num) {
   // Balancing heaps to make sure.
   // - smaller half of input numbers are always in the max heap
   // - larger half of input numbers are always in the min heap
   max_heap.add(num);
   min_heap.add(max_heap.peek());
   max_heap.poll();

   // Maintain size property.
   // 1. max_heap.size() = min_heap.size(), when number of elements is even
   // 2. max_heap.size() = min_heap.size() + 1, when number of elements is odd
   if (min_heap.size() > max_heap.size()) {
       max_heap.add(min_heap.peek());
       min_heap.poll();
   }
}

static int get_current_stream_median() {
   // If number of elements in the stream is even.
   if (max_heap.size() == min_heap.size())
       return (int)(((long)max_heap.peek() + min_heap.peek()) / 2);

   // If number of elements in the stream is odd.
   return max_heap.peek();
}

static ArrayList<Integer> online_median(ArrayList<Integer> stream) {
   max_heap.clear(); min_heap.clear();
   ArrayList<Integer> medians = new ArrayList<Integer>();

   for (int i = 0; i < stream.size(); i++) {
       add_new_element(stream.get(i));
       medians.add(get_current_stream_median());
   }
   return medians;
}
}
