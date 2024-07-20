package com.bonjour.practice.module.review;

import com.bonjour.practice.common.utils.MyRunable;
import com.bonjour.practice.common.utils.ThreadPoolUtils;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @authur tc
 * @date 2024/5/29 9:37
 */
public class CompletableFutureReview {

    private static ThreadPoolExecutor executor = ThreadPoolUtils.getThreadPool();

    public static String method01() {
//        executor.submit()
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return threadLocal.get();
    }

    public static String method02() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return threadLocal.get();
    }

    public static String method03() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return threadLocal.get();
    }

    public static String method04() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return threadLocal.get();
    }

    /**
     * 1 2 3 4 四个任务并行执行
     */
    public static void binxing() {
        Long startTime = System.currentTimeMillis();
        CompletableFuture<String> completableFuture01 = CompletableFuture.supplyAsync(CompletableFutureReview::method01, executor);
        CompletableFuture<String> completableFuture02 = CompletableFuture.supplyAsync(() -> method02(), executor);
        CompletableFuture<String> completableFuture03 = CompletableFuture.supplyAsync(() -> method03(), executor);
        CompletableFuture<String> completableFuture04 = CompletableFuture.supplyAsync(() -> method04(), executor);
        CompletableFuture<Void> completableFuture = CompletableFuture.allOf(completableFuture01, completableFuture02, completableFuture03, completableFuture04);
        List<String> list = new ArrayList<>();
        CompletableFuture<List<String>> future = completableFuture.thenApply(v -> {
            try {
                list.add(completableFuture01.get());
                list.add(completableFuture02.get());
                list.add(completableFuture03.get());
                list.add(completableFuture04.get());
                return list;
            } catch (Exception e) {
                throw new RuntimeException("");
            }
        });
        List<String> result = future.join();
        System.out.println(result);
        Long endTime = System.currentTimeMillis();
        System.out.println(((endTime - startTime) / 1000) + "s");
    }

    // 依次运行
    public static void yiciyunxing() {
        CompletableFuture.runAsync(() -> {
            System.out.println("hello");
        }, executor).thenRun(() -> {
            System.out.println("hello1");
        }).thenRunAsync(() -> {
            System.out.println("hello2");
        });
    }

    // thenApply 拿到上一个的任务结果运行下一一步
    public static String thenApply() {
        String s = "";
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            return "hello";
        }, executor).thenApply((result) -> {
            System.out.println("receive " + result);
            return "hello1";
        });
        try {
            s = completableFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    public static void method() {

    }

    public static final ThreadLocal<String> threadLocal = new InheritableThreadLocal<>();

    public static final List<String> list = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) {
//        yiciyunxing();
        System.out.println(thenApply());
//        for (int i = 0; i < 1000; i++) {
//            executor.execute(new Runnable() {
//                @Override
//                public void run() {
//                    getAjbh("110101");
//                }
//            });
//        }
//        try {
//            Thread.sleep(3000);
//        } catch (Exception e) {
//
//        }
//        System.out.println(list.size());
//        Set<String> set = new HashSet<>();
//        set.addAll(list);
//        System.out.println(set.size());
//        executor.shutdown();
//        threadLocal.set("hello");
//        System.out.println("主线程获取到的值 = " + threadLocal.get());
//        CompletableFuture<String> completableFuture01 = CompletableFuture.supplyAsync(CompletableFutureReview::method01, executor);
//        CompletableFuture<String> completableFuture02 = CompletableFuture.supplyAsync(() -> method02(), executor);
//        CompletableFuture<String> completableFuture03 = CompletableFuture.supplyAsync(() -> method03(), executor);
//        CompletableFuture<String> completableFuture04 = CompletableFuture.supplyAsync(() -> method04(), executor);
////        CompletableFuture<String> completableFuture04 = CompletableFuture.supplyAsync(new Runnable().run(););
////        executorService.
//        CompletableFuture<Void> completableFuture = CompletableFuture.allOf(completableFuture01, completableFuture02, completableFuture03, completableFuture04);
//        List<String> list = new ArrayList<>();
//        CompletableFuture<List<String>> future = completableFuture.thenApply(v -> {
//            try {
//                list.add(completableFuture01.get());
//                list.add(completableFuture02.get());
//                list.add(completableFuture03.get());
//                list.add(completableFuture04.get());
//                return list;
//            } catch (Exception e) {
//                throw new RuntimeException("");
//            }
//        });
//        List<String> result = future.join();
//        System.out.println(result);
//        for (int i = 0; i < 50; i++) {
//            executor.submit(new Runnable() {
//                @Override
//                public void run() {
//                    System.out.println("Runable子线程" + Thread.currentThread() + "获取到的值 = " + threadLocal.get());
//                }
//            });
//        }
//        for (int i = 0; i < 50; i++) {
//            executor.submit(new MyRunable() {
//                @Override
//                public void runTask() {
//                    System.out.println("MyRunable子线程获取到的值 = " + threadLocal.get());
//                }
//            });
//        }
//        LinkedBlockingQueue queue1 = new LinkedBlockingQueue();
//        threadLocal.remove();
    }

    public static String getAjbh(String jfqx00) {
        String ajbh = jfqx00 + new SimpleDateFormat("MMdd").format(new Date(System.currentTimeMillis())) + Math.round((Math.random()) * 10000);
        System.out.println(ajbh);
        list.add(ajbh);
        return ajbh;
    }
}
