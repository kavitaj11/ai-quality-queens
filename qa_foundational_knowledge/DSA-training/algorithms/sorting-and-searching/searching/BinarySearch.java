import java.util.*;
/** Find the first occurrence in a sorted array. Keep a half-open interval [lo, hi); values before lo are strictly below the key. On equality, keep looking left. Trace [2,4,4,9] with key 4: the answer is index 1. An unsorted input invalidates the discard step. O(log n) time, O(1) space. */
public class BinarySearch {
    static int first(int[] a,int key) { int lo=0, hi=a.length; while(lo<hi){int m=lo+(hi-lo)/2; if(a[m]<key) lo=m+1; else hi=m;} return lo<a.length && a[lo]==key ? lo : -1; }
}
