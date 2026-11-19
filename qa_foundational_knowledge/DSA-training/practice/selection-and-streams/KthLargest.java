import java.util.*;
/**
 * Maintain a min-heap of the largest k items. Trace k=2 on [4,1,7]; the smallest retained value is the answer, not the maximum.
 */
public class KthLargest {
/*
* Asymptotic complexity in terms of the length of `numbers` `n` and `k`:
* Time: O(k + (n - k) * log(k)).
* Auxiliary space: O(k).
* Total space: O(n).
*/
static Integer kth_largest_in_an_array(ArrayList<Integer> numbers, Integer k) {
   // Creating a min-heap.
   PriorityQueue<Integer> top_k = new PriorityQueue<Integer>();


   for (int i = 0; i < k; i++) {
       top_k.add(numbers.get(i));
   }


   for (int i = k; i < numbers.size(); i++) {
       if (numbers.get(i) > top_k.peek()) {
           top_k.poll();
           top_k.add(numbers.get(i));
       }
   }
   return top_k.peek();
}
}
