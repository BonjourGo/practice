package com.bonjour.practice.module.review.leetcode;

/**
 * @authur tc
 * @date 2024/7/19 11:28
 */
public class 回文数 {

    /**
     * 121 % 10 = 1
     * 12 % 10 = 2
     * 1 % 10 = 1
     * @param x
     * @return
     */
    public static boolean huiwenshu(int x) {
        if (x < 0) {
            return false;
        }
        if (x < 10) {
            return true;
        }
        int value = 0;
        int temp = x;
        int length = String.valueOf(x).length();
        for (int i = 0; i < length; i++) {
            value = value * 10 + temp % 10;
            temp = temp / 10;
        }
        return x == value;
    }

    public static void main(String[] args) {
        System.out.println(huiwenshu(1234321));
    }
}
