import java.util.*;

/**
 * Count values, bucket them by frequency, then visit the fullest frequency ranks first. Trace [1,1,2,3,3,3] for k=2; frequency, not numeric value, determines rank.
 */
public class TopKFrequent {
   public List<Integer> topKFrequent(int[] nums, int k) {

       // Step 1: Count the frequency of each element
       Map<Integer, Integer> frequencyMap = new HashMap<>();
       for (int num : nums) {
           frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
       }

       // Step 2: Create buckets to group elements by frequency
       List<Integer>[] buckets = new List[nums.length + 1];  // Buckets index corresponds to frequency
       for (int key : frequencyMap.keySet()) {
           int frequency = frequencyMap.get(key);
           if (buckets[frequency] == null) {
               buckets[frequency] = new ArrayList<>();
           }
           buckets[frequency].add(key);  // Place the element in the bucket corresponding to its frequency
       }

       // Step 3: Gather the k most frequent elements
       List<Integer> result = new ArrayList<>();
       for (int i = buckets.length - 1; i >= 0 && result.size() < k; i--) {
           if (buckets[i] != null) {
               result.addAll(buckets[i]);  // Add elements from the bucket with frequency 'i'
           }
       }

       // return the first k elements from the result list 
       return result.subList(0, k); 
   }

   public static void main(String[] args) {
       TopKFrequent sol = new TopKFrequent();
       int[] nums1 = {1, 1, 1, 2, 2, 3};
       int k1 = 2;
       System.out.println(sol.topKFrequent(nums1, k1));  // Output: [1, 2]

       int[] nums2 = {1};
       int k2 = 1;
       System.out.println(sol.topKFrequent(nums2, k2));  // Output: [1]
   }
}
