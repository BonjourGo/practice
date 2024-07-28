package com.bonjour.practice.module.review.leetcode;

/**
 * @authur tc
 * @date 2024/7/19 15:50
 */
public class 罗马数字转整数 {

    /**
     * 字符          数值
     * I             1
     * V             5
     * X             10
     * L             50
     * C             100
     * D             500
     * M             1000
     * I 可以放在 V (5) 和 X (10) 的左边，来表示 4 和 9。
     * X 可以放在 L (50) 和 C (100) 的左边，来表示 40 和 90。
     * C 可以放在 D (500) 和 M (1000) 的左边，来表示 400 和 900。
     */
    public static int toInt(String s) {
        int I = 1;
        int V = 5;
        int X = 10;
        int L = 50;
        int C = 100;
        int D = 500;
        int M = 1000;
        int result = 0;
        System.out.println(Enums.I.getKey());
        if (s.equals(Enums.I.getDesc())) {
//            return En
        }
        return result;
    }

    public static int getValue(String s) {
        switch (s) {
            case "I":
                return 1;
            case "V":
                return 5;
        }
        return 0;
    }

    public static enum Enums {
        I(1, "I"),
        V(5, "V"),
        X(10, "X"),
        L(50, "L"),
        C(100, "C"),
        D(500, "D"),
        M(1000, "M")
        ;

        private int key;

        private String desc;

        Enums(int key, String desc) {
            this.key = key;
            this.desc = desc;
        }

        public int getKey() {
            return this.key;
        }

        public String getDesc() {
            return this.desc;
        }
    }

    public static void main(String[] args) {
        System.out.println(Enums.I.getKey());
    }
}
