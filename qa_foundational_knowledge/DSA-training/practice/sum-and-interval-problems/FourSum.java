import java.util.*;
/**
 * Reduce a four-value target to smaller sum problems and finish with pairs. Trace [0,0,0,0,0] for target zero: multiple index choices should not duplicate the value tuple.
 */
public class FourSum {
/*
 Asymptotic complexity in terms of the length of input list `n`:
 * Time: O(n^3).
 * Auxiliary space: O(n^3).
 * Total space: O(n^4).
 */

// This function will return all the distinct pairs in arr[start ... arr.size() - 1]
// with sum of the values equal to the current_target.
static ArrayList<ArrayList<Integer>> two_sum(ArrayList<Integer> arr, Integer current_target, Integer start) {

   ArrayList<ArrayList<Integer>> result = new ArrayList<>();
   int low = start, high = arr.size() - 1;
   while (low < high) {
       if (arr.get(low) + arr.get(high) == current_target) {
           result.add(new ArrayList<>(Arrays.asList(arr.get(low), arr.get(high))));
           low++;
           high--;
       } else if (arr.get(low) + arr.get(high) < current_target) {
           low++;
       } else {
           high--;
       }

       if (low > start) {
           while (low <= high && arr.get(low).equals(arr.get(low - 1))) {
               low++;
           }
       }

       if (high < arr.size() - 1) {
           while (low <= high && arr.get(high).equals(arr.get(high + 1))) {
               high--;
           }
       }
   }
   return result;
}

// This function will return all the distinct sets of size `k` in `arr[start ... arr.size() - 1]`
// with sum of the values equal to the `current_target`.
static ArrayList<ArrayList<Integer>> k_sum(ArrayList<Integer> arr, Integer current_target, Integer start, Integer k) {
   if (start + k > arr.size() || arr.get(start) * k > current_target || arr.get(arr.size() - 1) * k < current_target) {
       return new ArrayList<>();
   }

   if (k == 2) {
       return two_sum(arr, current_target, start);
   }
   ArrayList<ArrayList<Integer>> result = new ArrayList<>();
   for (int i = start; i < arr.size(); i++) {
       if (i == start || !arr.get(i).equals(arr.get(i - 1))) {
           ArrayList<ArrayList<Integer>> sub_result = k_sum(arr, current_target - arr.get(i), i + 1, k - 1);

           for (ArrayList<Integer> current : sub_result) {
               current.add(arr.get(i));
               result.add(current);
           }
       }
   }
   return result;
}

static ArrayList<ArrayList<Integer>> four_sum(ArrayList<Integer> arr, Integer target) {
   Collections.sort(arr);
   return k_sum(arr, target, 0, 4);
}
}
