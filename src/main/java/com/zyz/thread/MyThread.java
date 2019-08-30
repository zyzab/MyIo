package com.zyz.thread;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

/**
 * Created by Administrator on 2017/11/22.
 */
public class MyThread implements Runnable {

    @Override
    public void run() {

    }

    public static void main(String[] args) {
        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("hello world f1");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "result f1";
        });
        CompletableFuture<String> f2 = f1.thenApply(r -> {
            System.out.println(r);
            try {
                TimeUnit.SECONDS.sleep(1000000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "f2";
        });
        CompletableFuture<String> f3 = f2.thenApply(r -> {
            System.out.println(r);
            try {
                TimeUnit.SECONDS.sleep(1000000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "f2";
        });

        CompletableFuture<String> f4 = f1.thenApply(r -> {
            System.out.println(r);
            try {
                TimeUnit.SECONDS.sleep(1000000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "f2";
        });
        CompletableFuture<String> f5 = f4.thenApply(r -> {
            System.out.println(r);
            try {
                TimeUnit.SECONDS.sleep(1000000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "f2";
        });
        CompletableFuture<String> f6 = f5.thenApply(r -> {
            System.out.println(r);
            try {
                TimeUnit.SECONDS.sleep(1000000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "f2";
        });

        System.out.println(f6.join());
    }
}
