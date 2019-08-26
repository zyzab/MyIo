package com.zyz.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class FutureTaskTest {

    private final static Logger LOGGER = LoggerFactory.getLogger(FutureTaskTest.class);


    /**
     * 洗水壶，烧开水，泡茶
     */
    static class T1Task implements Callable<String>{

        FutureTask<String> ft2;

        T1Task(FutureTask<String> ft2){
            this.ft2 = ft2;
        }

        public String call() throws Exception {
            LOGGER.info("T1: 洗水壶...");
            TimeUnit.SECONDS.sleep(1);

            LOGGER.info("T1: 烧开水...");
            TimeUnit.SECONDS.sleep(15);

            //阻塞获取T2线程的茶叶
            String tf = ft2.get();
            LOGGER.info("T1: 拿到茶叶:"+tf);

            LOGGER.info("T1: 泡茶...");
            TimeUnit.SECONDS.sleep(1);

            return " 上茶:"+tf;
        }
    }

    /**
     * 洗茶壶，洗茶杯，拿茶叶
     */
    static class T2Task implements Callable<String>{

        public String call() throws Exception {
            LOGGER.info("T2: 洗茶壶...");
            TimeUnit.SECONDS.sleep(1);

            LOGGER.info("T2: 洗茶杯...");
            TimeUnit.SECONDS.sleep(1);

            LOGGER.info("T2: 拿茶叶...");
            TimeUnit.SECONDS.sleep(1);

            return " 龙井 ";
        }
    }

    static Integer result = null;

    static class FutureRunnable implements Runnable{

        @Override
        public void run() {
            LOGGER.info("FutureRunnable start");
            try {
                Thread.sleep(4000L);
                result = 100;
            } catch (InterruptedException e) {
                LOGGER.error("InterruptedException",e);
            }
            LOGGER.info("FutureRunnable end");
        }
    }

    static class Task implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            LOGGER.info("子线程在进行计算");
            Thread.sleep(3000);
            int sum = 0;
            for(int i=0;i<100;i++)
                sum += i;
            return sum;
        }
    }

    public static void test1()  {
        Task task = new Task();
        FutureTask futureTask = new FutureTask(task);
        Thread thread = new Thread(futureTask);
        thread.start();
        try {
            LOGGER.info("result=[{}]",futureTask.get(1000L,TimeUnit.MILLISECONDS));
        } catch (Exception e) {
            LOGGER.error("Exception",e);
        }
        try {
            LOGGER.info("result=[{}]",futureTask.get(1000L,TimeUnit.MILLISECONDS));
        } catch (Exception e) {
            LOGGER.error("Exception",e);
        }
        try {
            LOGGER.info("result=[{}]",futureTask.get(1000L,TimeUnit.MILLISECONDS));
        } catch (Exception e) {
            LOGGER.error("Exception",e);
        }
        try {
            LOGGER.info("result=[{}]",futureTask.get(1000L,TimeUnit.MILLISECONDS));
        } catch (Exception e) {
            LOGGER.error("Exception",e);
        }
        try {
            LOGGER.info("result=[{}]",futureTask.get(1000L,TimeUnit.MILLISECONDS));
        } catch (Exception e) {
            LOGGER.error("Exception",e);
        }
    }

    public static void paocha(){
        FutureTask<String> ft2 = new FutureTask<>(new T2Task());
        FutureTask<String> ft1 = new FutureTask<>(new T1Task(ft2));

        Thread t1 = new Thread(ft1);
        Thread t2 = new Thread(ft2);

        t1.start();
        t2.start();

        try {
            LOGGER.info(" "+ft1.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) throws Exception{
//        test1();
        paocha();
    }

}
