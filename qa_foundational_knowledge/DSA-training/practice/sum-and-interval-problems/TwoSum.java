import java.util.*;
/**
 * Remember the index of each earlier value and look up the complement before inserting. Trace [3,3] with target 6; the two indices must be distinct.
 */
public class TwoSum {
/*
* Asymptotic complexity in terms of the size of `numbers` = `n`:
* Time: O(n).
* Auxiliary space: O(n).
* Total space: O(n).
*/

static ArrayList<Integer> two_sum(ArrayList<Integer> numbers, Integer target) {
   // A list to store the pair of indices that adds to target.
   ArrayList<Integer> result = new ArrayList<Integer>();

   // This Map stores the array element as Key and its index as Value.
   HashMap<Integer, Integer> array_index = new HashMap<Integer, Integer>();

   for (int i = 0; i < numbers.size(); i++) {
       int current = numbers.get(i);
       int required = target - current; // complementary target pair
       if (array_index.containsKey(required)) {
           result.add(array_index.get(required)); // store index of required in result 
           result.add(i);
           return result;
       }
       // Add every element to map after checking for required.
       // This ensures that element does not match itself (indices to be unique).
       array_index.put(current, i);
   }

   // If no such pair of indices exist, add -1, -1 to list.
   result.add(-1);
   result.add(-1);
   return result;
}
}
