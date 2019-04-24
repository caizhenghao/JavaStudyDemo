package com.czh.study.lock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @Auther caizhenghao
 * @Description 通过信号量实现限流器功能
 * @Date Create in 2019/4/24 11:51 PM
 * @Modified by
 */
public class SemaphoreDemo {

    public static void main(String[] args) {
        final Semaphore semaphore = new Semaphore(5);
        ExecutorService newCachedTheadPool = Executors.newCachedThreadPool();
        for (int i = 0; i < 20; i++) {
            final int index = i;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        semaphore.acquire();
                        System.out.println(index);
                        semaphore.release();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            newCachedTheadPool.execute(runnable);
        }
    }

}
