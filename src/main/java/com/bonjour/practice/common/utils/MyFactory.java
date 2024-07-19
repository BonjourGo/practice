package com.bonjour.practice.common.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @authur tc
 * @date 2024/4/11 15:17
 */
public class MyFactory implements ThreadFactory {

    private static AtomicInteger counter = new AtomicInteger();

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r);
//        Executors.newCachedThreadPool();
        t.setName("[thread]" + counter.incrementAndGet());
        return t;
    }
}
