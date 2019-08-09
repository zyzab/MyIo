package com.zyz.benchmark;

import com.revinate.guava.util.concurrent.RateLimiter;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 2)
@Measurement(iterations = 5, time = 5, timeUnit = TimeUnit.SECONDS)
@State(Scope.Thread)
@Fork(1)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class LimitLatchBenchmark {

    private Semaphore semaphore;

    private RateLimiter rateLimiter;


    @Setup
    public void setup() {
        semaphore = new Semaphore(5);
        rateLimiter = RateLimiter.create(5);
    }

    @Benchmark
    public void testSemaphore() {
        for (int i = 0; i < 10; i++) {
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                semaphore.release();
            }
        }
    }

    @Benchmark
    public void testRateLimiter() {
        for (int i = 0; i < 10; i++) {
            rateLimiter.acquire();
        }
    }

    private void print(String a) {
    }
}
