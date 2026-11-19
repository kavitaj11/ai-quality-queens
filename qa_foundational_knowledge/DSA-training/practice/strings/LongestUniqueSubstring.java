import java.util.*;
/** Find the length of the longest substring without repeated Unicode code points. Track the most recent index of each symbol; the left boundary never moves backwards. Trace abba: after the second b the left boundary stays at index 2 even when a returns. Expected O(n) time, O(n) space including the code-point array. */
public class LongestUniqueSubstring {
    static int length(String text) { int[] a=text.codePoints().toArray(); Map<Integer,Integer> last=new HashMap<>(); int left=0,best=0; for(int i=0;i<a.length;i++){Integer prev=last.put(a[i],i); if(prev!=null) left=Math.max(left,prev+1); best=Math.max(best,i-left+1);} return best; }
}
