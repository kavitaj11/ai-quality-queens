import java.util.*;
/** Compute shortest distances with nonnegative edge weights. Extract the least known distance first; discard stale queue entries after an improvement. With A→B (5), A→C (1), C→B (2), B improves to 3. A negative edge breaks the greedy guarantee. With lazy heap entries, time is O((V+E) log(E+2)). */
public class Dijkstra {
    record Edge(int to,int weight) {}
    record State(int node,long distance) {}
    static long[] distances(List<List<Edge>> g,int start) { long[] d=new long[g.size()]; Arrays.fill(d,Long.MAX_VALUE); d[start]=0; var q=new PriorityQueue<State>(Comparator.comparingLong(State::distance)); q.add(new State(start,0)); while(!q.isEmpty()){var s=q.remove(); if(s.distance()!=d[s.node()]) continue; for(var e:g.get(s.node())){if(e.weight()<0) throw new IllegalArgumentException(); long n=s.distance()+e.weight(); if(n<d[e.to()]){d[e.to()]=n;q.add(new State(e.to(),n));}}} return d; }
}
