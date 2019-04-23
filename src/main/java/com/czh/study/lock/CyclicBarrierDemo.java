package com.czh.study.lock;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @Auther caizhenghao
 * @Description
 * @Date Create in 2019/4/23 11:31 PM
 * @Modified by
 */
public class CyclicBarrierDemo {

    class Worker implements Runnable{

        CyclicBarrier cyclicBarrier;

        public Worker(CyclicBarrier cyclicBarrier){
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            try {
                cyclicBarrier.await(); // 等待其它线程
                System.out.println(Thread.currentThread().getName() + "启动@" + System.currentTimeMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

    public void doTest() throws InterruptedException {
        final int N = 5; // 线程数
        CyclicBarrier cyclicBarrier = new CyclicBarrier(N);
        for(int i=0;i<N;i++){
            new Thread(new Worker(cyclicBarrier)).start();
            Thread.sleep(100);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        CyclicBarrierDemo testCyclicBarrier = new CyclicBarrierDemo();
        testCyclicBarrier.doTest();
    }

}
