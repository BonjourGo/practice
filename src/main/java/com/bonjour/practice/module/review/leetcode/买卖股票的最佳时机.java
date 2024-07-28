package com.bonjour.practice.module.review.leetcode;

public class 买卖股票的最佳时机 {

    // 给定一个数组 prices ，它的第 i 个元素 prices[i] 表示一支给定股票第 i 天的价格。
    //你只能选择 某一天 买入这只股票，并选择在 未来的某一个不同的日子 卖出该股票。设计一个算法来计算你所能获取的最大利润。
    //返回你可以从这笔交易中获取的最大利润。如果你不能获取任何利润，返回 0 。
    //示例 1：
    //
    //输入：[7,1,5,3,6,4]
    //输出：5
    //解释：在第 2 天（股票价格 = 1）的时候买入，在第 5 天（股票价格 = 6）的时候卖出，最大利润 = 6-1 = 5 。
    //     注意利润不能是 7-1 = 6, 因为卖出价格需要大于买入价格；同时，你不能在买入前卖出股票。
    //示例 2：
    //
    //输入：prices = [7,6,4,3,1]
    //输出：0
    //解释：在这种情况下, 没有交易完成, 所以最大利润为 0。
    //
    public static int maxProfit(int[] prices) {
        int result = 0;
        int length = prices.length;
        int count = 0;
        int first = prices[0];
        for (int i = 0; i < length; i++) {
            int index = i + 1;
            int curr = prices[i];
            while (index < length) {
                System.out.println(count++);
                if (prices[index] > curr) {
                    result = Math.max(prices[index] - curr, result);
                }
                index++;
            }
        }
        return result;
    }

    //
    public static int maxProfit01(int[] prices) {
        int result = 0;
        int length = prices.length;
        int left = 0;
        int right = length - 1;
        // [3,3]
        while (left < right) {
            while (prices[left] >= prices[right] && left < right) {
                if (prices[left + 1] - prices[left] > prices[right - 1] - prices[right]) {
                    right--;
                }
            }
//            while (prices[left] >= prices[right] && left < right) {
//                if (prices[left] < prices[left + 1]) {
//                    right--;
//                } else {
//                    left++;
//                }
//            }
//            while (prices[left] <= prices[right] && left < right) {
//                result = Math.max(result, prices[right] - prices[left]);
//                if (prices[right] < prices[right - 1]) {
//                    right--;
//                } else {
//                    left++;
//                }
//            }
        }
        return result;
    }

    public static int maxProfit02(int[] prices) {
        int result = 0;
        int length = prices.length;
        int first = prices[0];
        for (int i = 1; i < length; i++) {
            result = Math.max(prices[i] - first, result);
            first = Math.min(prices[i], first);
        }
        return result;
    }

    public static void main(String[] args) {
//        int[] ints = new int[]{7, 1, 5, 3, 6, 4};
//        int[] ints = new int[]{2, 4, 1};
        int[] ints = new int[]{3, 2, 6, 5, 0, 3};
        System.out.println(maxProfit02(ints));
    }
}
