import java.util.*;
/**
 * Distribute nonnegative integers by units, tens and subsequent digits. Trace [21,12,11]; preserving bucket order keeps earlier digit decisions intact.
 */
public class RadixSort {
/*
* Asymptotic complexity in terms of size of `arr` `n`:
* Time: O(n).
* Auxiliary space: O(n).
* Total space: O(n).
*/
public static void bucketSort(ArrayList<Integer> arr, long div) {
   // Create an array of empty buckets
   List<List<Integer>> buckets = new ArrayList<>(10);
   for (int i = 0; i < 10; i++) {
       buckets.add(new ArrayList<>());
   }
   // Distribute the elements into the buckets based on the specified digit
   for (int i = 0; i < arr.size(); i++) {
       int bucketIndex = (int)((arr.get(i) / div) % 10);
       buckets.get(bucketIndex).add(arr.get(i));
   }
   // Merge the elements from all the buckets back into the original array
   int index = 0;
   for (int i = 0; i < 10; i++) {
       for (int j = 0; j < buckets.get(i).size(); j++) {
           arr.set(index++, buckets.get(i).get(j));
       }
   }
}
public static ArrayList<Integer> radix_sort(ArrayList<Integer> arr) {
   int n = arr.size();
   if (arr.isEmpty()) return arr;
   if (Collections.min(arr) < 0) throw new IllegalArgumentException("nonnegative keys required");
   int maxElement = arr.get(0);
   for (int i = 1; i < n; i++) {
       maxElement = Math.max(maxElement, arr.get(i));
   }

   long div = 1;
   // Perform bucket sort for each digit, starting from the least significant digit
      while (maxElement / div > 0) {
            bucketSort(arr, div);
            div *= 10; // Move to the next digit
        }
   return arr;
}
}
