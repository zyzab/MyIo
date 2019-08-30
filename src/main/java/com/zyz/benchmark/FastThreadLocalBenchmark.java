package com.zyz.benchmark;

import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.ChainedOptionsBuilder;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Warmup(iterations = 0)
@Measurement(iterations = 1, batchSize = 100)
@Threads(1)
@State(Scope.Thread)
public class FastThreadLocalBenchmark extends AbstractMicrobenchmark {

    private final InternalLogger logger = InternalLoggerFactory.getInstance(FastThreadLocalBenchmark.class);

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

    public FastThreadLocalBenchmark() {
        super(false, false);
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

    public static final class HarnessExecutor extends ThreadPoolExecutor {
        public HarnessExecutor(int maxThreads, String prefix) {
            super(maxThreads, maxThreads, 0, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<Runnable>(), new DefaultThreadFactory(prefix));
        }
    }

    public static void main(String[] args) throws Exception {
        FastThreadLocalBenchmark fastThreadLocalBenchmark = new FastThreadLocalBenchmark();

        new Runner(fastThreadLocalBenchmark.newOptionsBuilder().build()).run();
    }



    public void run() throws Exception {
        logger.info("zyz");
        System.out.println("zyzab");
        new Runner(newOptionsBuilder().build()).run();
    }

    protected  ChainedOptionsBuilder newOptionsBuilder1() throws Exception {
        String className = getClass().getSimpleName();
        final String[] customArgs  = new String[]{"-Xms768m", "-Xmx768m", "-XX:MaxDirectMemorySize=768m", "-Djmh.executor=CUSTOM",
                "-Djmh.executor.class=com.zyz.benchmark.FastThreadLocalBenchmark$HarnessExecutor"};
        ChainedOptionsBuilder runnerOptions = new OptionsBuilder()
                .include(".*" + className + ".*")
                .jvmArgs(customArgs);
        return runnerOptions;
    }

    protected ChainedOptionsBuilder newOptionsBuilder() throws Exception {
        ChainedOptionsBuilder runnerOptions = newOptionsBuilder1();
        if (getForks() > 0) {
            runnerOptions.forks(getForks());
        }

        return runnerOptions;
    }

    protected int getForks() {
        return SystemPropertyUtil.getInt("forks", -1);
    }

}
