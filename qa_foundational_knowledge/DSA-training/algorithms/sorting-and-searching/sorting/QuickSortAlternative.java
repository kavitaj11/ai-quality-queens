import java.util.*;
/**
 * Three-way partitioning keeps values equal to the pivot together. Trace [2,1,2,3,2]; only the less-than and greater-than regions need recursion.
 */
public class QuickSortAlternative {
   private static Random random = new Random();
   private static int[] partition3(int[] a, int l, int r) {
       int x = a[l];
       int m1 = l, m2 = r;
       int i = l + 1;
       while (i <= m2) {
           if (a[i] < x) swap(a, m1++, i++);
           else if (a[i] > x) swap(a, i, m2--);
           else   i++;
       }
       return new int[] {m1, m2};
   }


   private static void randomizedQuickSort(int[] a, int l, int r) {
       if (l >= r)  return;
       int k = random.nextInt(r - l + 1) + l;
       swap(a,l,k);
       //use partition3
       int m[] = partition3(a, l, r);
       randomizedQuickSort(a, l, m[0] - 1);
       randomizedQuickSort(a, m[1] + 1, r);
   }


   private static void swap(int[] a, int i, int j) {
       int swap = a[i];
       a[i] = a[j]; a[j] = swap;
   }
}
