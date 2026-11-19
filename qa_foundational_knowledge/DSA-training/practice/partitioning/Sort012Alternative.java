import java.util.*;
/**
 * Maintain finished regions for zeros and twos and scan the unknown middle. Trace [2,1,0]; inspect the incoming item again after a swap from the right.
 */
public class Sort012Alternative {
  public int[] sortArray_0_1_2(int[] nums) {
        
        //Three Pointers Approach
        
        //Make a pointer for left (0)
        int left = 0;
        //Make a pointer for right (2)
        int i  = 0;
        //Make a pointer for current (1)
        int right = nums.length - 1;
        
        //Iterate till i<=right
        while (i <= right)  {
            //Case if nums[i]==0
                  //Swap its value with nums[left]
                  //Increment left
            if (nums[i] == 0)  {
                swap(nums, i++, left++);
            } 
            
            // nums[i] is 1, check next one
                        //Increment i
            else if (nums[i] == 1)  {
                i++;
            } 
            
            //Case if nums[i]==2
            //Swap it with the right pointer
            //Decrement right pointer
            else {
                swap(nums, i, right--);
            }
        }
        
        return nums;
    }
    
    //Helper function for swapping
    private void swap(int[] nums, int left, int right) {
        int temp = nums[left];
        nums[left] = nums[right];
        nums[right] = temp;
    }
}
