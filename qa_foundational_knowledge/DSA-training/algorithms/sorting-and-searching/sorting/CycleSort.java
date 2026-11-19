import java.util.*;
/*
* Asymptotic complexity in terms of size of `arr` `n`:
* Time: O(n^2).
* space: O(1).
 */
/**
 * Count smaller elements to find an item's final position, then rotate displaced values. Trace [3,1,2] and count writes. Why must duplicates be skipped?
 */
public class CycleSort {
   public static int cycleSort(int[] arr) {
       int n = arr.length;
       int writes = 0; // To count the number of write operations

       // Traverse the array and treat each position as the start of a cycle
       for (int cycleStart = 0; cycleStart < n - 1; cycleStart++) {
           int item = arr[cycleStart];

           // Find the correct position for the item
           int pos = cycleStart;
           for (int i = cycleStart + 1; i < n; i++) {
               if (arr[i] < item) {
                   pos++;
               }
           }
           // If the item is already in the correct position, continue
           if (pos == cycleStart) {
               continue;
           }
           // Skip duplicate items
           while (item == arr[pos]) {
               pos++;
           }
           // Swap the item with the element at its correct position
           if (pos != cycleStart) {
               int temp = item;
               item = arr[pos];
               arr[pos] = temp;
               writes++;
           }
           // Rotate the rest of the cycle
           while (pos != cycleStart) {
               pos = cycleStart;
               for (int i = cycleStart + 1; i < n; i++) {
                   if (arr[i] < item) {
                       pos++;
                   }
               }
               while (item == arr[pos]) {
                   pos++;
               }


               if (item != arr[pos]) {
                   int temp = item;
                   item = arr[pos];
                   arr[pos] = temp;
                   writes++;
               }
           }
       }


       return writes;
   }


   public static void main(String[] args) {
       int[] arr = {3, 3, 2, 1, 4, 3, 2, 4};
       System.out.println("Original array: ");
       printArray(arr);


       int writes = cycleSort(arr);


       System.out.println("Sorted array: ");
       printArray(arr);
       System.out.println("Minimum writes: " + writes);
   }


   private static void printArray(int[] arr) {
        //for (int num : arr) { System.out.print(num + " "); }


       // Use Java 8 Arrays.stream to convert array to stream and then forEach to     print each element
       Arrays.stream(arr).forEach(num -> System.out.print(num + " "));


       System.out.println();
   }
}
