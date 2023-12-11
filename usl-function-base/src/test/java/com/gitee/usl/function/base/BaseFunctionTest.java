package com.gitee.usl.function.base;

import com.gitee.usl.USLRunner;
import com.gitee.usl.kernel.domain.ResourceParam;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author hongda.li
 */
class BaseFunctionTest {
    static USLRunner runner;

    @BeforeAll
    static void before() {
        runner = new USLRunner();
        runner.start();
    }

    @Test
    void list() {
        System.out.println(runner.run(new ResourceParam("list_test.js")).getData());
    }
}