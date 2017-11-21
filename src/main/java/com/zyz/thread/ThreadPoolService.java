package com.zyz.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * Created by yaozheng on 17/11/21.
 */
public class ThreadPoolService {

    private final static Logger LOGGER = LoggerFactory.getLogger(ThreadPoolService.class);


    private ThreadPoolService(){

    }

    private ThreadPoolService threadPoolService;

    private static class ThreadPoolServiceHolder{
        private static final ThreadPoolService INSTANCE = new ThreadPoolService();
    }

    public static ThreadPoolService getInstance(){
        return ThreadPoolServiceHolder.INSTANCE;
    }

    public static void main(String[] args) {

        ExecutorService executorService = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(2),new InfoRejectedExecutionHandler());



        for(int i=0;i<10;i++){
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    try {
                        //Thread.sleep(10000L);
                        while (true){
                            if(Thread.isInterrupted()){

                            }
                        }
                    } catch (Exception e) {
                        LOGGER.error("InterruptedException! ",e);
                        Thread.currentThread().interrupt();
                    }
                }
            });
            thread.setName("Thread"+i);
            executorService.submit(thread);
        }

        executorService.shutdownNow();
    }

     static class InfoRejectedExecutionHandler implements RejectedExecutionHandler{

        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            Thread thread = new Thread(r);
            LOGGER.info("rejectedExecution {} ...",thread.getName());
        }
    }



}
