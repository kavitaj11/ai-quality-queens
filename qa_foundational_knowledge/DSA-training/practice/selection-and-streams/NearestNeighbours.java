import java.util.*;
/**
 * Partition points by distance until the first k positions contain the nearest candidates. Try two equidistant points; membership need not imply sorted output.
 */
public class NearestNeighbours {
static ArrayList<ArrayList<Integer>> nearest_neighbours(Integer p_x, Integer p_y, Integer k, ArrayList<ArrayList<Integer>> n_points) {
   int min = 0;
   int max = n_points.size()-1;
   ArrayList<Integer> distanceFrom = new ArrayList();
   distanceFrom.add( p_x);
   distanceFrom.add( p_y);

   ArrayList<Point> distanceArray = new ArrayList();
   for(int i = 0; i < n_points.size(); i++) {
       distanceArray.add(new Point(i, distance(distanceFrom, n_points.get(i))));
   }

   quickSelectHelper(k-1, distanceArray, min, max);
   ArrayList<ArrayList<Integer>> result = new ArrayList<>();
   for(int i =0; i<k; i++){
       result.add(n_points.get(distanceArray.get(i).index));
   }
   return result;
}

static void quickSelectHelper(Integer k, ArrayList<Point> distanceArray, int min, int max){
   if(min>max ) return;
   int left = min+1;
   int right = max;
   int randomPivot = new Random().nextInt(max - min + 1) + min;
   swap(distanceArray, randomPivot, min);
   // double distanceFromPivot = distance(distanceFrom, n_points.get(min));

   while(left<=right){
       if( distanceArray.get(left).dist < distanceArray.get(min).dist){
           left++;
       }else if(distanceArray.get(right).dist > distanceArray.get(min).dist){
           right--;
       }else{
           swap(distanceArray, left, right);
           left++;
           right--;
       }
   }

   swap(distanceArray, min, left-1);
   if(left-1 == k){
       return;
   }
   if (left-1 > k) {
       quickSelectHelper(k, distanceArray, min, left-2);// quickselect left side
   } else {
       // quickSelectHelper(k, distanceArray, min, left-2, distanceFrom);//quickselect left side
       quickSelectHelper(k, distanceArray, left, max); //quickselect right side
   }
}

static double distance(ArrayList<Integer> p1, ArrayList<Integer> p2){
   int x1 = p1.get(0);
   int x2 = p2.get(0);
   int y1 = p1.get(1);
   int y2 = p2.get(1);

   return( Math.sqrt( ((x1-x2)*1l*(x1-x2)) + ((y1-y2)*1l*(y1-y2))));
}

static void swap(ArrayList<Point> distanceArray , int p1, int p2){
   Point temp = distanceArray.get(p1);
   distanceArray.set(p1, distanceArray.get(p2));
   distanceArray.set(p2, temp);
}

static class Point implements Comparable<Point>{
   int index;
   double dist;

   public Point(int i, double dist){
       this.index = i;
       this.dist = dist;
   }

   public int compareTo(Point p){
       return Double.compare(this.dist, p.dist);
   }
}
}
