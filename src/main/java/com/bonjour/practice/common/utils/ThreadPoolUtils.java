package com.bonjour.practice.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * 线程池工具类
 * @authur tc
 * @date 2022/10/8 16:54
 */
@Slf4j
public class ThreadPoolUtils {

    // 线程池
    private volatile static ThreadPoolExecutor threadPool;
    // 核心线程数
    public static final int CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    // 最大线程数
    public static final int MAX_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 2;
    // 线程最大存活时间
    public static final int KEEP_ALIVE_TIME = 1000;
    // 队列最大等待数
    public static final int BLOCK_QUEUE_SIZE = 1000;

    public static ThreadPoolExecutor getThreadPool() {
        if (threadPool != null) {
            return threadPool;
        } else {
            synchronized (ThreadPoolUtils.class) {
                threadPool = new ThreadPoolExecutor(
                        CORE_POOL_SIZE, // 核心线程数
                        MAX_POOL_SIZE, // 最大线程数
                        KEEP_ALIVE_TIME, // 空闲线程存活时间
                        TimeUnit.SECONDS, // 时间单位
                        new LinkedBlockingDeque<>(BLOCK_QUEUE_SIZE), // 阻塞队列
                        new ThreadPoolExecutor.CallerRunsPolicy()); // 拒绝策略
                return threadPool;
            }
        }
    }

    public static ExecutorService getExector() {
        return new ThreadPoolExecutor(
                CORE_POOL_SIZE, // 核心线程数
                MAX_POOL_SIZE, // 最大线程数
                KEEP_ALIVE_TIME, // 空闲线程存活时间
                TimeUnit.SECONDS, // 时间单位
                new LinkedBlockingDeque<>(BLOCK_QUEUE_SIZE), // 阻塞队列
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    /**
     * 无返回结果
     *
     * @param runnable
     */
    public static void executor(Runnable runnable) {
        getThreadPool().execute(runnable);
    }

    /**
     * 有返回结果
     *
     * @param callable
     * @param <T>
     * @return
     */
    public static <T> Future<T> submit(Callable<T> callable) {
        return getThreadPool().submit(callable);
    }
}
