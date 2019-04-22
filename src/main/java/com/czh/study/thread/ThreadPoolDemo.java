package com.czh.study.thread;

import com.sun.corba.se.spi.orbutil.threadpool.ThreadPool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author: cai.zhenghao
 * @Description:
 * @Date: Created in 2019/4/22  7:04 PM
 * @Modified By:
 */
public class ThreadPoolDemo {

    public static void main(String[] args) {
        int cpuCoreCount = Runtime.getRuntime().availableProcessors();
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(10, 20, 60000,
                TimeUnit.MILLISECONDS, new ArrayBlockingQueue(50));

        //只要未到达最小线程数，无论有无空闲线程都会新建线程
        Runnable runnable = () -> {
        };
        for (int i = 0; i < 10; i++) {
            threadPool.execute(runnable);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        outPutPoolState(threadPool);


        //这里加入20个任务，10条会被线程执行，由于任务队列未满，剩下十个会被放入待执行队列
        Runnable runnableSleep = () -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        for (int i = 0; i < 20; i++) {
            threadPool.execute(runnableSleep);
        }
        outPutPoolState(threadPool);

        //这50个任务都会被放入待执行队列
        for (int i = 0; i < 50; i++) {
            threadPool.execute(runnableSleep);
        }
        outPutPoolState(threadPool);

        //任务队列已满，开始继续增加线程数到最大线程数到最大
        for (int i = 0; i < 10; i++) {
            threadPool.execute(runnableSleep);
        }
        outPutPoolState(threadPool);

        //任务队列已满和线程数达到最大线程数，开始执行异常处理逻辑
        for (int i = 0; i < 1; i++) {
            threadPool.execute(runnableSleep);
        }
        outPutPoolState(threadPool);
    }

    public static void outPutPoolState(ThreadPoolExecutor threadPool) {
        System.out.println("threadPool.getPoolSize()=" + threadPool.getPoolSize()
                + ",threadPool.queueUseSize"
                + threadPool.getQueue().remainingCapacity());

    }
}
