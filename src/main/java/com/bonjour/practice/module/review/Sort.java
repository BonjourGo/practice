package com.bonjour.practice.module.review;

import com.bonjour.practice.common.entity.Address;
import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @authur tc
 * @date 2024/7/15 10:19
 */
@Slf4j
public class Sort {

    /**
     * 冒泡排序
     *
     * @param arr
     */
    public static void maopao(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length - i - 1; j++) {
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
     *
     * @param arr
     * @param value
     * @return
     */
    public static int binarySearch(int[] arr, int value) {
        if (arr == null) {
            return -1;
        }
        if (arr.length == 1 && arr[0] == value) {
            return 0;
        }
        int start = 0;
        int end = arr.length;
        int mid = 0;
        while (start <= end) {
            mid = (start + end) >>> 1;
            if (arr[mid] == value) {
                return mid;
            } else if (arr[mid] > value) {
                end = mid - 1;
            } else if (arr[mid] < value) {
                start = mid + 1;
            }
        }
        return -1;
    }

    /**
     * 插入排序
     *
     * @param arr
     */
    public static void insert(int[] arr) {
        if (arr == null || arr.length == 1) {
            return;
        }
        for (int i = 1; i < arr.length; i++) {
            int val = arr[i];
            int index = i - 1;
            while (index >= 0 && val < arr[index]) {
                arr[index + 1] = arr[index];
                index--;
            }
            arr[index + 1] = val;
        }
    }

    /**
     * 选择排序 找到最小的元素跟第一位交换，第二次选择第二小的元素跟第二位交换
     *
     * @param arr
     */
    public static void select(int[] arr) {
        long s = System.currentTimeMillis();
        for (int i = 0; i < arr.length; i++) {
            int index = i;
            for (int j = i + 1; j < arr.length; j++) {
                while (arr[j] < arr[index]) {
                    index = j;
                }
            }
            if (index != i) {
                int temp = arr[i];
                arr[i] = arr[index];
                arr[index] = temp;
            }
        }
        long end = System.currentTimeMillis();
        log.info("耗时{}ms", (end - s));
    }

    /**
     * 快排：选择一个基数，两个指针分别从数组前后遍历，找到比基数大的和比基数小的，交换两者位置
     *
     * @param arr
     * @param left
     * @param right
     */
    public static void qs(int[] arr, int left, int right) {
        int i = left;
        int j = right;
        int value = arr[left];
        while (i < j) {
            while (i < j && arr[i] <= value) {
                i++;
            }
            while (i < j && arr[j] > value) {
                j--;
            }
            if (i < j) {
                int temp = arr[j];
                arr[j] = arr[i];
                arr[i] = temp;
            }
        }
        arr[left] = arr[i];
        arr[i] = value;
        quickSort(arr, left, i - 1);
        quickSort(arr, i + 1, right);
    }


    public static void quickSort(int[] nums, int star, int end) {
        if (star > end) {
            return;
        }
        int i = star;
        int j = end;
        int key = nums[star];
        while (i < j) {
            while (i < j && nums[j] > key) {
                j--;
            }
            while (i < j && nums[i] <= key) {
                i++;
            }
            if (i < j) {
                int temp = nums[i];
                nums[i] = nums[j];
                nums[j] = temp;
            }
        }
        nums[star] = nums[i];
        nums[i] = key;
        quickSort(nums, star, i - 1);
        quickSort(nums, i + 1, end);
    }

    private static final SecureRandom RANDOM = new SecureRandom();
    public static void main(String[] args) {
//        Address.builder().address()
        List<Integer> list = new ArrayList<>();
        int[] ints = {1, 3, 2, 5, 4, 6};
        int[] arr = new int[100000];
        for (int i = 0; i < 100000; i++) {
            arr[i] = RANDOM.nextInt(100);
        }
//        ints = ;
        // 冒泡
//        maopao(ints);
        // 插入
//        insert(ints);
        // 选择
        select(arr);
        // 快排
//        qs(ints, 0, ints.length - 1);
//        System.out.println(Arrays.toString(ints));
//        System.out.println(binarySearch(ints, 4));
    }
}
