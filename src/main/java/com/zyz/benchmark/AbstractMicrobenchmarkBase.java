package com.zyz.benchmark;

import io.netty.util.ResourceLeakDetector;
import io.netty.util.internal.SystemPropertyUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.options.ChainedOptionsBuilder;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for all JMH benchmarks.
 */
@Warmup(iterations = AbstractMicrobenchmarkBase.DEFAULT_WARMUP_ITERATIONS)
@Measurement(iterations = AbstractMicrobenchmarkBase.DEFAULT_MEASURE_ITERATIONS)
@State(Scope.Thread)
public abstract class AbstractMicrobenchmarkBase {

    private final Logger logger = LoggerFactory.getLogger(AbstractMicrobenchmarkBase.class);

    protected static final int DEFAULT_WARMUP_ITERATIONS = 1;
    protected static final int DEFAULT_MEASURE_ITERATIONS = 1;
    protected static final String[] BASE_JVM_ARGS = {
            "-server",
            "-XX:+HeapDumpOnOutOfMemoryError"};

    protected ChainedOptionsBuilder newOptionsBuilder() throws Exception {
        String className = getClass().getSimpleName();

        ChainedOptionsBuilder runnerOptions = new OptionsBuilder()
                .include(".*" + className + ".*")
                .jvmArgs(jvmArgs());

        if (getWarmupIterations() > 0) {
            runnerOptions.warmupIterations(getWarmupIterations());
        }

        if (getMeasureIterations() > 0) {
            runnerOptions.measurementIterations(getMeasureIterations());
        }

        if (getReportDir() != null) {
            String filePath = getReportDir() + className + ".json";
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            } else {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            runnerOptions.resultFormat(ResultFormatType.JSON);
            runnerOptions.result(filePath);
        }
        logger.info("runnerOptions=[{}]",runnerOptions);
        return runnerOptions;
    }

    protected abstract String[] jvmArgs();

    protected static String[] removeAssertions(String[] jvmArgs) {
        List<String> customArgs = new ArrayList<>(jvmArgs.length);
        for (String arg : jvmArgs) {
            if (!arg.startsWith("-ea")) {
                customArgs.add(arg);
            }
        }
        if (jvmArgs.length != customArgs.size()) {
            jvmArgs = customArgs.toArray(new String[0]);
        }
        return jvmArgs;
    }

    protected int getWarmupIterations() {
        return SystemPropertyUtil.getInt("warmupIterations", -1);
    }

    protected int getMeasureIterations() {
        return SystemPropertyUtil.getInt("measureIterations", -1);
    }

    protected String getReportDir() {
        return SystemPropertyUtil.get("perfReportDir");
    }

}
