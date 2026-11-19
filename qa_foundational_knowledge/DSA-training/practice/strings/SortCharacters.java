import java.util.*;
/**
 * Use a counter per ASCII value, then emit each character as many times as it occurred. Trace [b,a,b]; input outside the 128-slot alphabet needs another representation.
 */
public class SortCharacters {
/*
* Asymptotic complexity in terms of size of `arr` `n`:
* Time: O(n).
* Auxiliary space: O(1).
* Total space: O(n).
*/
static ArrayList<Character> sort_array(ArrayList<Character> arr) {
   // An array to store the number of occurence of each character in `arr`.
   int frequency[] = new int[128];
   for(char c : arr) {
       frequency[c]++;
   }
   arr.clear();
   for(int i  = 0; i < 128; i++) {
       for(int j = 0; j < frequency[i]; j++) {
           arr.add((char)i);
       }
   }
   return arr;
}
}
