package com.bonjour.practice.module.review.leetcode;

import java.util.Arrays;

/**
 * @authur tc
 * @date 2024/7/18 17:07
 */
public class 两数之和 {

    public static int[] two(int[] nums, int n) {
        int[] result = new int[2];
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] + nums[j] == n) {
                    return new int[]{i, j};
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        int[] ints = {1,2,3,5};
        System.out.println(Arrays.toString(two(ints, 5)));
    }
}
