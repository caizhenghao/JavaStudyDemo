package com.czh.study.lock;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * @Auther caizhenghao
 * @Description
 * @Date Create in 2019/4/23 11:35 PM
 * @Modified by
 */
public class CountDownLatchDemo {

    class Worker implements Runnable {

        CountDownLatch countDownLatch;

        Worker(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(100 + new Random().nextInt(100));
                countDownLatch.countDown();
                System.out.println(Thread.currentThread().getName() + "启动@" + System.currentTimeMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void doTest() throws InterruptedException {
        final int N = 5; // 线程数
        CountDownLatch countDownLatch = new CountDownLatch(N);
        for (int i = 0; i < N; i++) {
            new Thread(new Worker(countDownLatch)).start();
        }
        countDownLatch.await(); // 等待其它线程
        System.out.println("所有线程跑完了@" + System.currentTimeMillis());
    }

    public static void main(String[] args) throws InterruptedException {
        CountDownLatchDemo testCountDownLatch = new CountDownLatchDemo();
        testCountDownLatch.doTest();
    }


}
