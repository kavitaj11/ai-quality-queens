import java.util.*;
/**
 * Partition around a randomly chosen pivot, then sort the smaller subproblems. Trace a pivot of 3 in [4,1,3,2]; the partitions need not be equal in size.
 */
public class QuickSort {
/*
Asymptotic complexity in terms of `n` =  size of the input array:
* Time: O(n^2).
* Auxiliary space: O(1).
* Total space: O(n).
*/
// This function will partition arr[low ... high] around any random pivot_element such that all of the
// elements less than pivot_element move to its left and all of the elements greater than or equal to
// pivot_element move to its right.


public static int randomPartition(List<Integer> arr, int low, int high) {
   // Swapping arr[high] with any random element in the array and then partitioning around
   // pivot_element = arr[high].
   int randomIndex = low + new Random().nextInt(high - low + 1);
   Collections.swap(arr, randomIndex, high);
   int pivot = low;
   int pivotElement = arr.get(high);

   for (int i = low; i < high; i++) {
       if (arr.get(i) < pivotElement) {
           Collections.swap(arr, i, pivot);
           pivot++;
       }
   }
   // Taking pivot_element to pivot position.
   Collections.swap(arr, pivot, high);
   return pivot;
}

// Recursive helper function that sorts arr[low ... high].
public static void quickSortHelper(List<Integer> arr, int low, int high) {
   if (low >= high) {
       return;
   }
   int pivot = randomPartition(arr, low, high);
   quickSortHelper(arr, low, pivot - 1);
   quickSortHelper(arr, pivot + 1, high);
}




public static ArrayList<Integer> quick_sort(ArrayList<Integer> arr) {
   quickSortHelper(arr, 0, arr.size() - 1);
   return arr;
}
}
