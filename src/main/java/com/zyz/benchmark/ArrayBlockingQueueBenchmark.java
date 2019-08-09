package com.zyz.benchmark;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.*;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
@Fork(1)
@Warmup(iterations=2)
@Measurement(iterations = 5)
public class ArrayBlockingQueueBenchmark {

    private BlockingQueue<Long> msgQueue;

    public static final Long LONGVAL = 0xba53ba1L;

    private static Thread addTask;


    @Setup
    public void setup() {
        msgQueue = new ArrayBlockingQueue<Long>(1024);


        // 从容器中取出元素
        addTask = new Thread(new Runnable() {
            @Override
            public void run() {
                Long a= null;
                while(true) {
                    a= msgQueue.poll();
                    System.out.println(a);
                }
            }
        });
        addTask.start();
    }

    @Benchmark
    public void sendOneM() {
        try {
            msgQueue.put(LONGVAL);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
