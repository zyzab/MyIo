package com.zyz.logtest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class LogTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogTest.class);

    public static void main(String[] args) {
        String trace_id = "trace_id";
        MDC.put(trace_id,"001");

        LOGGER.info("hello");
        MDC.remove(trace_id);

        MDC.put(trace_id,"002");

        LOGGER.info("world");
        MDC.remove(trace_id);

    }
}
