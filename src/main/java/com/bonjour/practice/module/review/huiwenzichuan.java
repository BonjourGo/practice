package com.bonjour.practice.module.review;

/**
 * @authur tc
 * @date 2024/7/11 14:20
 */
public class huiwenzichuan {
    public String hwzc(String s) {
        // String s = "aabcdbc";
        int start = 0;
        int last = s.length();
        return "";
    }

    public static void main(String[] args) {
        ThreadLocal threadLocal = new ThreadLocal();
        threadLocal.set("hello");
        System.out.println(threadLocal.get());

//        String s = "aabcdbc";
//        int first = 0;
//        int last = 0;
//        for (int i = 0; i < s.length(); i++) {
//            int high = i;
//            while (high < s.length() && s.charAt(i) == s.charAt(high)) {
//                high++;
//            }
//            System.out.println(s.substring(i, high));
//        }
////        System.out.println(s.charAt(0));
    }
}
