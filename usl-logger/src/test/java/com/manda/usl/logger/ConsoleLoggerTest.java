package com.manda.usl.logger;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author hongda.li
 */
class ConsoleLoggerTest {

    @Test
    void append() {
        Logger logger = LoggerFactory.getLogger(ConsoleLoggerTest.class);
        assertDoesNotThrow(() -> {
            logger.debug("debug");
            logger.info("info : {}", "msg");
            logger.error("error", new RuntimeException("error"));
            logger.warn("warn : {}", Arrays.asList("a", "b", "c"));
        });
    }
}