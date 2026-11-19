import java.util.*;
/** Choose the maximum number of non-overlapping meetings. Pick the earliest finishing available meeting: exchanging the first chosen meeting for this one cannot leave less time for the rest. Trace [1,4), [1,2), [2,3): the two short meetings fit. Sorting costs O(n log n); the scan is linear. Assumes valid start-before-end intervals. */
public class IntervalScheduling {
    static int count(int[][] intervals) { Arrays.sort(intervals,Comparator.comparingInt(x->x[1])); int end=Integer.MIN_VALUE,n=0; for(int[] x:intervals) if(x[0]>=end){end=x[1];n++;} return n; }
}
