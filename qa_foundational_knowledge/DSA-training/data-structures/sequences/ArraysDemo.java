import java.util.*;
/**
 * Use an array for indexed access with a fixed length. Follow the sort before the binary search: the returned index belongs to the sorted array, not the original one.
 */
public class ArraysDemo {
    public static void main(String[] args) {
        int[] a={3,1,2}; Arrays.sort(a); System.out.println(Arrays.binarySearch(a,2));
    }
}
