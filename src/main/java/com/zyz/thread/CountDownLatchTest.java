package com.zyz.thread;

import java.util.concurrent.CountDownLatch;

/**
 * Created by yaozheng on 17/12/23.
 */
public class CountDownLatchTest {
    private static final int NThreads = 10;// 线程数

    private static final int M = 100000;//循环次数，太小的话（比如10）可能看不出来效果

    private volatile static int n = 0;//加volatile的目的是为了证明volatile没有“原子性”！



    public static void main(String[] args)throws InterruptedException {

        final CountDownLatch startGate = new CountDownLatch(1);

        final CountDownLatch endGate = new CountDownLatch(NThreads);



        for(int i = 0; i < NThreads; i++){

            new Thread(new Runnable(){

                public void run(){

                    try{

                        startGate.await();//所有线程start之后等待“门“打开，保证同时真正开始运行

                    }catch(InterruptedException e){

                        e.printStackTrace();

                    }



                    for(int j = 0; j < M; j++){

                        n += 1;

                    }



                    endGate.countDown();

                }

            }).start();

        }



        startGate.countDown();//打开“门”，让所有线程同时run起来

        startGate.await();

        long t1 = System.currentTimeMillis();

        endGate.await();//等所有线程都结束之后才打印n，否则总是会打出错误的n；我见过这里用Thread.sleep()，但是问题在于，你怎么知道该等多久才能保证所有线程结束以及刚好结束呢？！

        long t2 = System.currentTimeMillis();

        System.out.println("cost time:" + (t2 - t1));

        System.out.println("n: " + n);

        int a = 16;
        System.out.println("a="+(a>>>2));

    }


}
