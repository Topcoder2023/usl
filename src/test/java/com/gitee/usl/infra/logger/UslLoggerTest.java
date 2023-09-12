package com.gitee.usl.infra.logger;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hongda.li
 */
class UslLoggerTest {
    static Logger logger;

    @BeforeAll
    public static void before() {
        logger = LoggerFactory.getLogger(UslLoggerTest.class);
    }

    @Test
    void handleNormalizedLoggingCall() {
        // 获取日志名称测试
        Assertions.assertEquals(logger.getName(), UslLoggerTest.class.getName());

        // Debug测试
        logger.debug("This is a simple test of [{}]", "DEBUG");

        // Trace测试
        logger.trace("This is a simple test of [{}]", "TRACE");

        // Info测试
        logger.info("This is a simple test of [{}]", "INFO");

        // Warn测试
        logger.warn("This is a simple test of [{}]", "WARN");

        // Error测试
        logger.error("This is a simple test of [{}]", "ERROR");
    }
}