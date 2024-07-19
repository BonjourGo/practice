package com.bonjour.practice.module.review.leetcode;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @authur tc
 * @date 2024/7/18 17:27
 */
@Slf4j
public class 两数相加 {

    static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) {
            this.val = val;
        }
        ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    public static ListNode twoAdd(ListNode one, ListNode two) {
        ListNode result = new ListNode();
        StringBuilder s = new StringBuilder();
        StringBuilder s1 = new StringBuilder();
        if (one == null) {
            return two;
        } else if (two == null) {
            return one;
        }
        while (one != null) {
            s.append(one.val);
            one = one.next;
        }
        while (two != null) {
            s1.append(two.val);
            two = two.next;
        }
        BigDecimal bigDecimal = new BigDecimal(s.toString()).add(new BigDecimal(s1.toString()));
        String s2 = bigDecimal.toString();
        for (int i = 0; i < s2.length(); i++) {
            int sum = Integer.parseInt(String.valueOf(s2.charAt(i)));
            if (i == 0) {
                result.val = sum;
                result.next = new ListNode();
            } else {
                result.next = new ListNode(sum, new ListNode());
            }
        }
        return result;
    }

    public static ListNode twoNodeAdd(ListNode l1, ListNode l2) {
        ListNode result = new ListNode();
        // 99999999
        //        9
        //100000008
        // 9,9,9,9,9,9,9
        // 9,9,9,9
        // 8,9,9,9,0,0,0,1
        int v1 = 0;
        while (l1 != null || l2 != null) {
            int l1Val = l1 != null ? l1.val : 0;
            int l2Val = l2 != null ? l2.val : 0;
            int value = l1Val + l2Val + v1;
            v1 = value / 10;
            int v2 = value % 10;
            ListNode temp = new ListNode(v2);
            result.next = temp;
            result = temp;
            if (l1 != null) {
                l1 = l1.next;
            }
            if (l2 != null) {
                l2 = l2.next;
            }
        }
        return result.next;
    }

    public static void main(String[] args) {
        int val = 9;
        int val1 = 9;
        int value = val + val1;
        log.info("value = {}, v1 = {}, v2 = {}", value, value / 10, value % 10);
        ListNode listNode = new ListNode(5);
        ListNode listNode1 = new ListNode(0);
        ListNode listNode2 = new ListNode(0);
        listNode.next = listNode1;
        listNode1.next = listNode2;
        ListNode two = new ListNode(5);
        ListNode two1 = new ListNode(0);
        ListNode two2 = new ListNode(0);
        two.next = two1;
        two1.next = two2;
        ListNode result = twoNodeAdd(listNode, two);
//        ListNode result = twoAdd(listNode, two);
        while (result.next != null) {
            System.out.println(result.val);
            result = result.next;
            if (result.next == null && ObjectUtil.isNotNull(result.val)) {
                System.out.println(result.val);
            }
        }

        int[] ints = new int[]{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1};
        BigDecimal bigDecimal = new BigDecimal("1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1".replaceAll(", ", ""));
        System.out.println(bigDecimal.toString());
//        while (listNode != null) {
//            System.out.println(listNode.val);
//            listNode = listNode.next;
//        }
    }
}
