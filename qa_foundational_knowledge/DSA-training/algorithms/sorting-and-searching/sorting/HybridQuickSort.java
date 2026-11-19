import java.util.*;
/**
 * Use quicksort for large partitions and insertion sort for small ones. Compare arrays just below and above the cutoff; the result should agree despite the change of technique.
 */
public class HybridQuickSort {
   private static final int INSERTION_SORT_THRESHOLD = 10; // Threshold to switch to insertion sort
   private static final Random random = new Random();

   /**
    * Sorts an array using hybrid sorting (QuickSort + Insertion Sort).
    *
    * @param A the array to be sorted
    */
   public void sort(int[] A) {
       if (A == null || A.length <= 1) return;
       quicksort(A, 0, A.length - 1);
   }

   /**
    * Recursive quicksort method with a switch to insertion sort for small subarrays.
    *
    * @param A     the array to be sorted
    * @param start the starting index
    * @param end   the ending index
    */
   private void quicksort(int[] A, int start, int end) {
       if (end - start <= INSERTION_SORT_THRESHOLD) {
           insertionSort(A, start, end);
           return;
       }

       if (start < end) {
           // Random pivot selection
           int pivotIndex = start + random.nextInt(end - start + 1);
           swap(A, start, pivotIndex);

           // Three-way partitioning
           int pivot = A[start];
           int lt = start, gt = end, i = start + 1;

           while (i <= gt) {
               if (A[i] < pivot) {
                   swap(A, i++, lt++);
               } else if (A[i] > pivot) {
                   swap(A, i, gt--);
               } else {
                   i++;
               }
           }


           // Recursive calls for the partitions
           quicksort(A, start, lt - 1);
           quicksort(A, gt + 1, end);
       }
   }


   /**
    * Insertion sort for small subarrays.
    *
    * @param A     the array to be sorted
    * @param start the starting index
    * @param end   the ending index
    */
   private void insertionSort(int[] A, int start, int end) {
       for (int i = start + 1; i <= end; i++) {
           int key = A[i];
           int j = i - 1;
           while (j >= start && A[j] > key) {
               A[j + 1] = A[j];
               j--;
           }
           A[j + 1] = key;
       }
   }


   /**
    * Swaps two elements in the array.
    *
    * @param A the array containing the elements
    * @param i the index of the first element
    * @param j the index of the second element
    */
   private void swap(int[] A, int i, int j) {
       int temp = A[i];
       A[i] = A[j];
       A[j] = temp;
   }


   // Main method for testing the hybrid sorting algorithm
   public static void main(String[] args) {
       HybridQuickSort sorter = new HybridQuickSort();
       int[] array = {5, 3, 8, 4, 2, 7, 1, 10, 6, 9, 0, 12, 11, 14, 13};
       sorter.sort(array);


       // Print sorted array
       System.out.println("Sorted array:");
       for (int num : array) {
           System.out.print(num + " ");
       }
   }
}
