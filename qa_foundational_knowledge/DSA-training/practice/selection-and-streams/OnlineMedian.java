import java.util.*;
/**
 * Insert each arrival into the already-sorted prefix and read its middle. Trace [5,1,8]; this method changes the input list and reports integer medians.
 */
public class OnlineMedian {
/*
* Asymptotic complexity in terms of `n` = size of the input array:
* Time: O(n^2).
* Auxiliary space: O(1).
* Total space: O(n).
*/
static ArrayList<Integer> online_median(ArrayList<Integer> stream) {
   ArrayList<Integer> medians = new ArrayList<>();


   for (int i = 0; i < stream.size(); i++) {
       // Applying insertion sort.
       // Insert stream[i] in stream[0, 1,..., i - 1] which is already sorted.


       for (int j = i - 1; j >= 0; j--) { //j>=0 to ensure j never reaches -1. 
           if (stream.get(j + 1) < stream.get(j))
               Collections.swap(stream, j + 1, j);
           else
               break;
       }


       int median;
       int current_elements = i + 1;
       if (current_elements % 2 == 0)
           median = (stream.get(current_elements / 2) + stream.get(current_elements / 2 - 1)) / 2;
       else
           median = stream.get(current_elements / 2);


       medians.add(median);
   }
   return medians;
}
}
