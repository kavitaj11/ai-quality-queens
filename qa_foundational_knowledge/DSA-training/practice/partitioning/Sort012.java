import java.util.*;
/**
 * Count zeros, ones and twos before rewriting the array. Trace [2,0,2,1]; this works because the allowed values are known in advance.
 */
public class Sort012 {
  public int[] sortArray_0_1_2(int[] nums) {
    //Counting Sort Algorithm
    
    // Make three variables
    // For counting 0,1 and 2 respectively
    int count0 = 0, count1 = 0, count2 = 0;
        
        //Iterate through the array and count the frequencies of each number
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == 0) {count0++;}
            if (nums[i] == 1) {count1++;}
            if (nums[i] == 2) {count2++;}
        }
        
        //Fill the original array
        //First fill all 0, then 1 and at last 2
        for(int i = 0; i < nums.length; i++) {
            if (i < count0) {nums[i] = 0;}
            else if (i < count0 + count1) {nums[i] = 1;}
            else {nums[i] = 2;}
        }
        
      return nums;
    
  }
}
