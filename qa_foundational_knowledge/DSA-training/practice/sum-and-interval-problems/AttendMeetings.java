import java.util.*;
/**
 * Check whether any pair of meetings overlaps. Compare [1,3) with [3,4) and then [2,4): touching endpoints are allowed, shared time is not.
 */
public class AttendMeetings {
/*
* Asymptotic complexity in terms of the length of `intervals` `n`:
* Time: O(n^2).
* Auxiliary space: O(1).
* Total space: O(n).
*/

static Integer can_attend_all_meetings(ArrayList<ArrayList<Integer>> intervals) {

   for (int i = 0; i < intervals.size(); i++) {
       for (int j = i + 1; j < intervals.size(); j++) {
           // If overlap found, return 0.
           if (!(intervals.get(i).get(1) <= intervals.get(j).get(0)
                   || intervals.get(j).get(1) <= intervals.get(i).get(0))) {
               return 0;
           }
       }
   }
   return 1;
}
}
