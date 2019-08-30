package com.zyz.benchmark;

import io.netty.util.concurrent.FastThreadLocal;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Random;

@Threads(4)
@Measurement(iterations = 1, batchSize = 100)
public class FastThreadLocalBenchmark extends AbstractMicrobenchmark {

    private static final Logger logger = LoggerFactory.getLogger(FastThreadLocalBenchmark.class);

    private static final Random rand = new Random();

    @SuppressWarnings("unchecked")
    private static final ThreadLocal<Integer>[] jdkThreadLocals = new ThreadLocal[128];
    @SuppressWarnings("unchecked")
    private static final FastThreadLocal<Integer>[] fastThreadLocals = new FastThreadLocal[jdkThreadLocals.length];

    static {
        for (int i = 0; i < jdkThreadLocals.length; i ++) {
            final int num = rand.nextInt();
            jdkThreadLocals[i] = new ThreadLocal<Integer>() {
                @Override
                protected Integer initialValue() {
                    return num;
                }
            };
            fastThreadLocals[i] = new FastThreadLocal<Integer>() {
                @Override
                protected Integer initialValue() {
                    return num;
                }
            };
        }
    }

    public static void main(String[] args) throws Exception {
      //  FastThreadLocalBenchmark fastThreadLocalBenchmark = new FastThreadLocalBenchmark();
//        Options options = fastThreadLocalBenchmark.newOptionsBuilder().build();
//        logger.info("options=[{}]",options);


        Options opt = new OptionsBuilder()
                .include(FastThreadLocalBenchmark.class.getSimpleName())
                .forks(1)
                .warmupIterations(5).jvmArgs("-Djmh.executor.class=com.zyz.benchmark.AbstractMicrobenchmark$HarnessExecutor")
                .measurementIterations(2)
                .build();
        Collection<RunResult> results =  new Runner(opt).run();

    }

    @Benchmark
    public void jdkThreadLocalGet(Blackhole bh) {
        for (ThreadLocal<Integer> i: jdkThreadLocals) {
            bh.consume(i.get());
        }
    }

    @Benchmark
    public void fastThreadLocal(Blackhole bh) {
        for (FastThreadLocal<Integer> i: fastThreadLocals) {
            bh.consume(i.get());
        }
    }

}
