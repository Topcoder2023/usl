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
        Param param1 = new Param().setScript("string.isEmpty('')");
        assertEquals(true, runner.run(param1).getData());

        Param param2 = new Param().setScript("string.isBlank('str')");
        assertEquals(false, runner.run(param2).getData());

//        Param param3 = new Param()
//                .setScript("str.nullToDefault(var, 'def')")
//                .addContext("var", null);
//        assertEquals("def", runner.run(param3).getData());
    }
}