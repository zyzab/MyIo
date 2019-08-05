package com.zyz.benchmark;

import com.zyz.logtest.LogTest;
import org.openjdk.jmh.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by yaozheng on 19/8/4.
 */
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 2)
@Measurement(iterations = 1, time = 5, timeUnit = TimeUnit.SECONDS)
@Threads(4)
@Fork(1)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class LogBenchmark {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogTest.class);

    @Benchmark
    public void test() {
        for (int i = 0; i < 10; i++) {
            LOGGER.info("hello log4j");
        }

    }
}
