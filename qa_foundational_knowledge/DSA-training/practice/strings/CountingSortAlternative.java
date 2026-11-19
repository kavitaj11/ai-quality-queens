import java.util.*;
/**
 * Use character codes as frequency-table indices. Trace the repeated letters in BABA. The 256-slot table does not cover all Java char values.
 */
public class CountingSortAlternative {
public static void countingSortCharacters(String str) {
   int[] count = new int[256]; // ASCII range
   char[] result = new char[str.length()];


   // Count occurrences
   for (char c : str.toCharArray()) {
       count[c]++;
   }


   // Build the sorted string
   int index = 0;
   for (int i = 0; i < count.length; i++) {
       while (count[i]-- > 0) {
           result[index++] = (char) i;
       }
   }


   System.out.println(new String(result));
}
}
