package com.zyz.logtest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogTest.class);

    public static void main(String[] args) {
        LOGGER.info("hello");
        LOGGER.info("world");
    }
}
