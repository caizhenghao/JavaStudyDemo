package com.czh.study.leetcode.pro1;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther caizhenghao
 * @Description
 * @Date Create in 2019/1/18 12:20 AM
 * @Modified by
 */
public class TwoSum {

    public int[] twoSum(int[] nums, int target) {
        Map<Integer,Integer> numMap = new HashMap<Integer,Integer>();
        for(int i=0;i<nums.length;i++){
            int needAdd = target - nums[i];
            if(numMap.containsKey(needAdd)){
                return new int[] {numMap.get(needAdd),i};
            }
            numMap.put(nums[i],i);
        }
        throw new IllegalArgumentException("No two sum solution");
    }
}
