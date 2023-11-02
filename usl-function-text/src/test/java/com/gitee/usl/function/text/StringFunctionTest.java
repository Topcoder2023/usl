package com.gitee.usl.function.text;

import com.gitee.usl.USLRunner;
import com.gitee.usl.kernel.domain.Param;
import com.googlecode.aviator.runtime.type.AviatorFunction;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

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
    void stringTest() {
        Param param1 = new Param("string.isEmpty('')");
        assertEquals(true, runner.run(param1).getData());

        Param param2 = new Param("string.isBlank('str')");
        assertEquals(false, runner.run(param2).getData());

        Param param3 = new Param("string.trim('       hello    \n\n    ')");
        assertEquals("hello", runner.run(param3).getData());
    }
}