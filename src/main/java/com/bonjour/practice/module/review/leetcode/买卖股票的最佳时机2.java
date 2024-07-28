package com.bonjour.practice.module.review.leetcode;

public class 买卖股票的最佳时机2 {

    public static int maxProfit(int[] prices) {
        int result = 0;
        int length = prices.length;
        int first = prices[0];
        for (int i = 1; i < length; i++) {
            int curr = prices[i];
            if (curr - first > 0) {
                result = result + curr - first;
            }
            first = curr;
        }
        return result;
    }

    public static void main(String[] args) {
        int[] ints = new int[]{7, 1, 5, 3, 6, 4};
        System.out.println(maxProfit(ints));
    }
}
