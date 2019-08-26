package com.zyz.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by yaozheng on 19/8/22.
 */
public class CompletableFutureTest {

    private final static Logger LOGGER = LoggerFactory.getLogger(CompletableFutureTest.class);

    public static void main(String[] args) {
        // 任务 1: 洗水壶 -> 烧开水
        CompletableFuture<Void> f1 = CompletableFuture.runAsync(()->{
            LOGGER.info("T1 : 洗水壶...");
            sleep(1,TimeUnit.SECONDS);

            LOGGER.info("T1 : 烧开水...");
            sleep(15,TimeUnit.SECONDS);
        });

        // 任务 2: 洗茶壶 -> 洗茶壶 -> 拿茶叶
        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(()->{
            LOGGER.info("T2 : 洗茶壶...");
            sleep(1,TimeUnit.SECONDS);

            LOGGER.info("T2 : 洗茶壶...");
            sleep(2,TimeUnit.SECONDS);

            LOGGER.info("T2 : 拿茶叶...");
            sleep(1,TimeUnit.SECONDS);

            return "龙井";
        });

        // 任务3: 任务1 和任务2 完成后执行: 泡茶
        CompletableFuture<String> f3 = f1.thenCombine(f2,(__,tf)->{
            LOGGER.info("T3 : 拿到茶叶:"+tf);
            LOGGER.info("T3 : 泡茶...");
            return " 上茶:"+tf;
        });

        LOGGER.info(f3.join());
    }


    static void sleep(int t,TimeUnit u){
        try {
            u.sleep(t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
