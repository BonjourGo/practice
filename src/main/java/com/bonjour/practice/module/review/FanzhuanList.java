package com.bonjour.practice.module.review;

import org.w3c.dom.ls.LSOutput;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * @authur tc
 * @date 2024/6/26 10:44
 */
public class FanzhuanList {

    // 链表节点
    static class ListNode {
        ListNode next;
        int val;

        ListNode(int val) {
            this.val = val;
        }

        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    // 反转链表：链表存在值和指向下一个节点的指针，要反转链表则要将当前节点的指针指向上一个节点
    public static ListNode reverseList(ListNode head) {
        // 前置节点
        ListNode prev = null;
        // 当前节点
        ListNode cur = head;
        while (cur != null) {
            // 临时节点
            ListNode temp = cur.next;
            cur.next = prev;
            prev = cur;
            cur = temp;
        }
        return prev;
    }

    public static ListNode fz(ListNode node) {
        // 将节点的
        if (node == null || node.next == null) {
            return node;
        }
        ListNode head = node;
        ListNode pre = null;
        while (head != null) {
            ListNode temp = head.next;
            head.next = pre;
            pre = head;
            head = temp;
        }
        return pre;
    }


    // 环形链表 双指针 一个快指针和一个慢指针
    public static boolean isSycle(ListNode listNode) {
        if (listNode == null || listNode.next == null) {
            return false;
        }
        ListNode slow = listNode;
        ListNode fast = listNode;
        do {
            if (fast.next == null || fast.next.next == null) {
                return false;
            }
            slow = slow.next;
            fast = fast.next.next;
        } while (fast != slow);
        return true;
    }

    // 二叉树的层序遍历
//    public static List<List<Integer>> cengxubianli(ListNode node) {
//
//    }

    public static int[] mp(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - 1 - i; j++) {
                if (arr[j + 1] < arr[j]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
        return arr;
    }

    public static int binarySearch(int[] arr, int key) {
        int low = 0;
        int high = arr.length - 1;
        if (key < arr[low] || key > arr[high]) {
            return -1;
        }
        int count = 0;
        while (low <= high) {
            System.out.println(count++);
            int mid = (low + high) / 2;
            if (key == arr[mid]) {
                return mid;
            }
            if (key < arr[mid]) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return -1;
    }

    //
    public static List<Integer> matrix(int[][] matrix) {
        /**
         * 1  2  3  4
         * 5  6  7  8
         * 9 10 11 12
         * 1-2-3-4-8-12-11-10-5-6-7
         */
        List<Integer> list = new ArrayList<>();
        list.add(matrix[0][0]);
        int length = matrix[0].length;
//        int high = matrix.
        return list;
    }


    public static void main(String[] args) {

//        ListNode listNode1 = new ListNode(1);
//        ListNode listNode2 = new ListNode(2);
//        ListNode listNode3 = new ListNode(3);
//        listNode1.next = listNode2;
//        listNode2.next = listNode3;
//        listNode3.next = null;
//        ListNode temp = fz(listNode1);
//        ListNode result = temp;
//        while (result != null) {
//            System.out.println(result.val);
//            result = result.next;
//        }


//        ListNode listNode1 = new ListNode(1);
//        ListNode listNode2 = new ListNode(2);
//        ListNode listNode3 = new ListNode(3);
//        listNode1.next = listNode2;
//        listNode2.next = listNode1;
////        listNode3.next = null;
//        System.out.println(isSycle(listNode1));
//        int[] arr = new int[]{3, 2, 1};
//        arr = mp(arr);
//        System.out.println(binarySearch(arr, 3));
//        System.out.println(Arrays.toString(arr));
//        int[][] matrix = {{1, 2, 3}, {3,4,5}, {6,7,8}};
        String s = "abcafea";
        System.out.println(s.charAt(2));
        int n = s.length();
        Set<Character> set = new HashSet<>();
        int ans = 0, i = 0, j = 0;
        while (i < n && j < n) {
            if (!set.contains(s.charAt(j))){
                set.add(s.charAt(j++));
                ans = Math.max(ans, j - i);
            }
        }
        System.out.println(set);
//        Semaphore semaphore = new Semaphore(2);
//        try {
//            semaphore.acquire();
//        } catch (Exception e) {
//            semaphore.release();
//        }
    }

//    public static String sout() {
//
//    }

}
