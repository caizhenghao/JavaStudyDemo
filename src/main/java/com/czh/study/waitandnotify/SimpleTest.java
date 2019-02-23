package com.czh.study.waitandnotify;

/**
 * @Author: cai.zhenghao
 * @Description:
 * @Date: Created in 2019/2/23  4:59 PM
 * @Modified By:
 */
public class SimpleTest {
    public static void main(String[] args) {
        String object = new String("Test");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (object) {
                    System.out.println("branch thread notify");
                    object.notify();
                }
            }
        };
        new Thread(runnable).start();
        System.out.println("main thread start wait");
        synchronized (object) {
            try {
                object.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("main thread after wait");

    }
}
