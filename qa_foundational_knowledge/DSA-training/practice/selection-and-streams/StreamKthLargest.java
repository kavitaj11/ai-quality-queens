import java.util.*;
/**
 * Append each arrival and sort the accumulated history before reading the kth value from the end. Compare this baseline with a bounded heap as history grows.
 */
public class StreamKthLargest {
/*
* Asymptotic complexity in terms of the size of `initial_stream` `n` and the size of `append_stream` `m`:
* Time: O(m * (m + n) * log(n + m)).
* Auxiliary space: O(m).
* Total space: O(n + m).
*/


static ArrayList<Integer> kth_largest(Integer k, ArrayList<Integer> initial_stream, ArrayList<Integer> append_stream) {
   ArrayList<Integer> result = new ArrayList<>();

   for (int i = 0; i < append_stream.size(); i++) {
       initial_stream.add(append_stream.get(i));

       Collections.sort(initial_stream);

       // kth element from the end will be the kth largest element.
       result.add(initial_stream.get(initial_stream.size() - k));
   }

   return result;
}
}
