package com.bonjour.practice.study.suanfa;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class lianbiao {

    static class Node {
        int val;

        Node next;

        Node pre;

        Node() {

        }

        Node(int val) {
            this.val = val;
        }
    }

    // 将当前节点指向
    public static Node fanzhuan(Node node) {
        Node pre = null;
        Node head = node;
        while (head != null) {
            Node temp = head.next;
            head.next = pre;
            pre = head;
            head = temp;
        }
        return pre;
    }

    public static boolean isSyscle(Node node) {
        Node slow = node;
        Node fast = node;
        do {
            if (slow.next == null || fast.next.next == null) {
                return false;
            }
            slow = slow.next;
            fast = fast.next.next;
        } while (slow != fast);
        return true;
    }

    public static Node delNode(Node node, int val) {
        if (node == null || ((node.next == null) && (node.val == val))) {
            return null;
        }
        Node node1 = new Node();
        while (node.next != null) {
            Node temp = node.next;
            if (node.val == val) {
                node1 = temp;
            } else {
                node1 = node;
                node1.next = node.next;
            }
        }
        return node;
    }

    public static void main(String[] args) {
        Node node1 = new Node(1);
        Node node2 = new Node(2);
        Node node3 = new Node(3);
        Node node4 = new Node(4);
        node1.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node2;
        LinkedList list = new LinkedList();
        List list1 = new ArrayList();
//        list1.remove()
//        Node node = fanzhuan(node1);
//        while (node != null) {
//            System.out.println(node.val);
//            node = node.next;
//        }
        System.out.println(isSyscle(node1));
    }
}
