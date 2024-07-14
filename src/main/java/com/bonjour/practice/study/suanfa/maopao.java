package com.bonjour.practice.study.suanfa;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * simple 算法
 */
public class maopao {

    /**
     * 冒泡排序
     * 比较交换的过程
     *
     * @param arr
     */
    public static void maopaoSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    /**
     * 二分查找
     * 给定值，从中间查找
     *
     * @param arr
     * @param value
     * @return
     */
    public static int binarySearch(int[] arr, int value) {
        int min = 0;
        int max = arr.length - 1;
        int mid = 0;
        if (min > max) {
            return -1;
        }
        while (min <= max) {
            mid = (min + max) / 2;
            if (arr[mid] == value) {
                return mid;
            } else if (arr[mid] > value) {
                max = mid - 1;
            } else if (arr[mid] < value) {
                min = mid + 1;
            }
        }
        return -1;
    }

    /**
     * 插入排序：循环选择当前的数组下标的我值，如果当前的值比前一个值小，则将前一个值交换到当前的下标，最后将当前的下标的值放在对应的位置
     * @param val
     * @return
     */
    public static void insertSort(int[] val) {
        for (int i = 1; i < val.length; i++) {
            int value = val[i];
            int index = i -1;
            while (index >= 0 && value < val[index]) {
                // 如果选择插入的值比数组前一个值还要小，则交换位置
                val[index + 1] = val[index];
                index--;
            }
            val[index + 1] = value;
        }
    }

    public static void main(String[] args) {
        int[] arr = {1, 5, 7, 4, 3, 2, 6};
        insert(arr);
//        mp(arr);
        System.out.println("冒泡----" + Arrays.toString(arr));
        int[] binarySearchArr = {2, 3, 5, 7, 9};
        System.out.println("二分----" + binary(binarySearchArr, 5));
    }

    public static int[] mp(int[] val) {
        for (int i = 0; i < val.length; i++) {
            for (int j = 0; j < val.length - 1 - i; j++) {
                if (val[j] > val[j + 1]) {
                    int temp = val[j];
                    val[j] = val[j + 1];
                    val[j + 1] = temp;
                }
            }
        }
        return val;
    }

    public static int binary(int[] val, int value) {
        int first = 0;
        int last = val.length - 1;
        int mid = 0;
        while (first <= last) {
            mid = (first + last) / 2;
            if (val[mid] == value) {
                return mid;
            } else if (val[mid] > value) {
                last = mid - 1;
            } else if (val[mid] < value) {
                first = mid + 1;
            }
        }
        return -1;
    }

    public static void insert(int[] val) {
        for (int i = 1; i < val.length; i++) {
            int value = val[i];
            int pre = i - 1;
            while (pre >= 0 && value < val[pre]) {
                // 将大的值交换到后一位
                val[pre + 1] = val[pre];
                pre--;
            }
            val[pre + 1] = value;
        }
    }

}
