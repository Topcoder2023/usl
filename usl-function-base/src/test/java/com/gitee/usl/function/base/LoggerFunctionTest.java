package com.gitee.usl.function.base;

import com.gitee.usl.USLRunner;
import com.gitee.usl.kernel.domain.Param;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author hongda.li
 */
class LoggerFunctionTest {
    static USLRunner runner;

    @BeforeAll
    static void before() {
        runner = new USLRunner();
        runner.start();
    }

    @Test
    void debug() {
        Param param = new Param()
                .setScript("logger.debug('hello : {}', var)")
                .addContext("var", "debug");

        assertDoesNotThrow(() -> runner.run(param));
    }

    @Test
    void warn() {
        Param param = new Param().setScript("logger.warn('hello : {}', 'warn')");
        assertDoesNotThrow(() -> runner.run(param));
    }

    @Test
    void info() {
        Param param = new Param().setScript("logger.info('hello : {}', 'info')");
        assertDoesNotThrow(() -> runner.run(param));
    }

    @Test
    void error() {
        Param param = new Param().setScript("logger.error('hello : {}', 'error')");
        assertDoesNotThrow(() -> runner.run(param));
    }
}