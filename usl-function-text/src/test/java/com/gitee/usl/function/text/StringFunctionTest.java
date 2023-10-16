package com.gitee.usl.function.text;

import com.gitee.usl.USLRunner;
import com.gitee.usl.kernel.domain.Param;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author hongda.li
 */
class StringFunctionTest {
    static USLRunner runner;

    @BeforeAll
    static void before() {
        runner = new USLRunner();
        runner.start();
    }

    @Test
    void strIsEmpty() {
        assertEquals(true, runner.run(new Param().setScript("str.isEmpty('')")).getData());
        assertEquals(false, runner.run(new Param().setScript("str.isEmpty('str')")).getData());
    }

    @Test
    void strIsBlank() {
    }

    @Test
    void strEmptyToDefault() {
    }
}