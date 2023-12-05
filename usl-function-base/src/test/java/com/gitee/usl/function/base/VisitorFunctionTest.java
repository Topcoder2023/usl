package com.gitee.usl.function.base;

import com.gitee.usl.USLRunner;
import com.gitee.usl.kernel.domain.Param;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author hongda.li
 */
class VisitorFunctionTest {
    static USLRunner runner;

    @BeforeAll
    static void init() {
        runner = new USLRunner();
        runner.start();
    }

    @Test
    void get() {
        USLRunner obj = new USLRunner("testName", USLRunner.defaultConfiguration());
        assertEquals("testName", runner.run(new Param()
                        .setScript("get(obj, 'name')")
                        .addContext("obj", obj))
                .getData());
    }

    @Test
    void set() {
        USLRunner obj = new USLRunner();
        assertEquals("testName2", runner.run(new Param()
                        .setScript("set(obj, 'name', 'testName2')")
                        .addContext("obj", obj))
                .getData());
        assertEquals("testName2", runner.run(new Param()
                        .setScript("get(obj, 'name')")
                        .addContext("obj", obj))
                .getData());
    }
}