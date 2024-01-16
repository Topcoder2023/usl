package com.gitee.usl.function.base;

import com.gitee.usl.Runner;
import com.gitee.usl.kernel.configure.Configuration;
import com.gitee.usl.kernel.domain.ResourceParam;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author hongda.li
 */
class BaseFunctionTest {
    static Runner runner;
    static ResourceParam param;

    @BeforeAll
    static void before() {
        Configuration configuration = Runner.defaultConfiguration();
        runner = new Runner(configuration);
        runner.start();

        param = new ResourceParam("list_test.usl");
    }

    @Test
    void list() {
        runner.run(param);
    }
}