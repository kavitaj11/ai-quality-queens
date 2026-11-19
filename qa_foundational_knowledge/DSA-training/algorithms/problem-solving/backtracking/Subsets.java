import java.util.*;
/** Enumerate subsets of distinct values. At each position choose to skip or take it; undo the addition after the recursive call. Trace [1,2]: exactly four leaf paths exist. Copy each completed path or later mutations will change previous answers. Output takes O(n × 2^n) time and space in total. */
public class Subsets {
    static void visit(int[] a,int i,List<Integer> path,List<List<Integer>> out) { if(i==a.length){out.add(List.copyOf(path));return;} visit(a,i+1,path,out); path.add(a[i]); visit(a,i+1,path,out); path.remove(path.size()-1); }
}
