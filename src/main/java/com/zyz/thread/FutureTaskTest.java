package com.zyz.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class FutureTaskTest {

    private final static Logger LOGGER = LoggerFactory.getLogger(FutureTaskTest.class);

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



    public static void main(String[] args) throws Exception{
        test1();
    }

}
