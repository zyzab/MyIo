package com.zyz.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by yaozheng on 17/12/18.
 */
public class LockTest {

    private final static Logger LOGGER = LoggerFactory.getLogger(LockTest.class);


    private static ReentrantLock reentrantLock = new ReentrantLock(true);


    public static void main(String[] args) {
        ReentrantLockThread thread = new ReentrantLockThread();
        thread.setName("thread");
        thread.start();
        ReentrantLockThread thread1 = new ReentrantLockThread();
        thread1.setName("thread1");
        thread1.start();

        ReentrantLockThread thread2 = new ReentrantLockThread();
        thread2.setName("thread2");
        thread2.start();

        ReentrantLockThread thread3 = new ReentrantLockThread();
        thread3.setName("thread3");
        thread3.start();


    }

    static class ReentrantLockThread extends Thread{
        public void run() {
            try {
                reentrantLock.lock();
                LOGGER.info("Thread==>{}",Thread.currentThread().getName());
                //Thread.sleep(10000L);
            }catch (Exception e){
                LOGGER.error("error:",e);
            }finally {
                reentrantLock.unlock();
            }
        }
    }
}
