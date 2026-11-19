import java.util.*;
/**
 * On a sorted list, adjust opposite ends according to their sum. With [1,3,5,8] and target 8, explain why discarding 8 first cannot lose a solution.
 */
public class SortedPairSum {
/*
* Asymptotic complexity in terms of the size of `numbers` = `n`:
* Time: O(n).
* Auxiliary space: O(1).
* Total space: O(n).
*/

static ArrayList<Integer> pair_sum_sorted_array(ArrayList<Integer> numbers, Integer target) {
   // A list to store the pair of indices that adds to target.
   ArrayList<Integer> result = new ArrayList<Integer>();

   int left = 0, right = numbers.size() - 1;

   while (left < right) {
       // Current sum is the sum of numbers at left and right indices.

       if (numbers.get(left) + numbers.get(right) == target) {
           result.add(left);
           result.add(right);
           return result;
       }
       // If the current sum is less than target, move left to (left + 1).
       // Element at (left + 1) is greater than the element at left, as the given array is sorted.
       else if (numbers.get(left) + numbers.get(right) < target) {
           left++;
       }
       // If the current sum is more than target, move right to (right - 1).
       // Element at (right - 1) is less than the element at right.
       else {
           right--;
       }
   }

  // If no such pair of indices exist, add -1, -1 to list.
   result.add(-1);
   result.add(-1);
   return result;
}
}
