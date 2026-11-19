import java.util.*;
/**
 * Fix one value in a sorted list and search for a complementary pair. Try [-1,0,1,1]; distinguish unique value triples from distinct index triples.
 */
public class ThreeSum {
/*
Asymptotic complexity in terms of size of the input array `n`:
* Time: O(n^2 + n * log(n)).
* Auxiliary space: O(1).
* Total space: O(n^2).
*/


static ArrayList<String> find_zero_sum(ArrayList<Integer> arr) {
   Set<String> answer = new HashSet<>();
   Collections.sort(arr); // A prerequisite for the two pointer technique to work.
   int n = arr.size();
   for (int index = 0; index < n; index++) {
       int currentElement = arr.get(index);
       // We will look for two elements that sum up to this:
       int neededSum = -currentElement;
       int left = index + 1, right = n - 1;
       while (left < right) {
           int sum = arr.get(left) + arr.get(right);
           if (sum == neededSum) {
               answer.add(currentElement + "," + arr.get(left) + "," + arr.get(right));
               left++; // "right--" would also work fine here.


               // Note that the three numbers in our magic triplet strings
               // will always be sorted in the increasing order because
               // we sorted the array and because index > left > right.
               // That and using a set to store the strings is enough for
               // avoiding duplicates in the answer.
           } else if (sum > neededSum) {
               right--;
           } else {
               left++;
           }
       }
   }
   return new ArrayList<>(answer);
}
}
