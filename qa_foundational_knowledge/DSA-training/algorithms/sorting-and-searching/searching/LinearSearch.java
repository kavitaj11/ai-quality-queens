import java.util.*;
/** Scan until the first match, even if the array is unsorted. Each rejected element extends a prefix known not to contain the key. Trace [7,1,7] with key 1; then try an absent key. Worst case O(n) time and O(1) space. */
public class LinearSearch {
    static int first(int[] a,int key) { for(int i=0;i<a.length;i++) if(a[i]==key) return i; return -1; }
}
