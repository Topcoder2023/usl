package com.gitee.usl.function.base;

import com.gitee.usl.USLRunner;
import com.gitee.usl.function.base.entity.Script;
import com.gitee.usl.kernel.domain.Param;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author hongda.li
 */
class ScriptFunctionTest {
    static USLRunner runner;

    @BeforeAll
    static void init() {
        runner = new USLRunner();
        runner.start();
    }

    @Test
    void script() {
        Object data = runner.run(new Param().setScript("script('USL-Default@Test')")).getData();
        assertInstanceOf(Script.class, data);
    }

    @Test
    void run() {
        Object data = runner.run(new Param().setScript("script.run(script('USL-Default@Test'))")).getData();
        assertEquals(36288000L, data);
    }

    @Test
    void result() {
        Object data = runner.run(new Param().setScript("script.result(script('USL-Default@Test'))")).getData();
        assertEquals(36288000L, data);
    }
}