import java.util.*;
/**
 * Group three colors in place using region boundaries. Start with one ball of each color in reverse order and trace which boundary moves on each swap.
 */
public class DutchNationalFlag {
/* Given some balls of three colors arranged in a line, rearrange them such that all the red balls go first, then green and then blue ones.
Asymptotic complexity in terms of size of `balls` `n`:
* Time: O(n).
* Auxiliary space: O(1).
* Total space: O(n).
*/

static ArrayList<Character> dutch_flag_sort(ArrayList<Character> balls) {
   int n = balls.size();

   // When we will encounter any red ball, we will swap it with character present at this index.
   int swap_red_here = 0;

   // Position we are processing currently.
   int current_idx = 0;

   // When we will encounter any blue ball, we will swap it with character present at this index.
   int swap_blue_here = n - 1;

   // Continue till we have sorted all the balls.
   while (current_idx <= swap_blue_here) {
       if (balls.get(current_idx) == 'R') {
           char temp = balls.get(current_idx);
           balls.set(current_idx, balls.get(swap_red_here));
           balls.set(swap_red_here, temp);
           swap_red_here++;
           current_idx++;
       }
       else if (balls.get(current_idx) == 'G') {
           current_idx++;
       }
       else {
           char temp = balls.get(current_idx);
           balls.set(current_idx, balls.get(swap_blue_here));
           balls.set(swap_blue_here, temp);
           swap_blue_here--;
       }
   }
   return balls;
}
}
