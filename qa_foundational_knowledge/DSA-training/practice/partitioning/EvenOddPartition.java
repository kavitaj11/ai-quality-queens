import java.util.*;
/**
 * Move misplaced values across a left and right boundary. Try [3,2,1,4]: check that even values precede odd values, without assuming either group is sorted.
 */
public class EvenOddPartition {
/*
* Asymptotic complexity in terms of size of `numbers` `n`:
* Time: O(n).
* Auxiliary space: O(1).
* Total space: O(n).
*/

static ArrayList<Integer> segregate_evens_and_odds(ArrayList<Integer> numbers) {
   int n = numbers.size();

  int left = 0, right = n - 1;

   while (right >= left) { // Move pointers until they cross.
       if (numbers.get(right) % 2 == 0) {
           int tmp = numbers.get(right);
           numbers.set(right, numbers.get(left));
           numbers.set(left, tmp);
           left++; // All values to the left of the left pointer will be even.
       } else {
           right--; // All values to the right of the right pointer will be odd.
       }
   }
   return numbers;
}
}
