package com.gitee.usl.function.base;

import com.gitee.usl.USLRunner;
import com.gitee.usl.kernel.engine.USLConfiguration;
import com.gitee.usl.kernel.domain.ResourceParam;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author hongda.li
 */
class BaseFunctionTest {
    static USLRunner runner;
    static ResourceParam param;

    @BeforeAll
    static void before() {
        USLConfiguration configuration = USLRunner.defaultConfiguration();
        runner = new USLRunner(configuration);
        runner.start();

        param = new ResourceParam("list_test.usl");

        param.addContext("x", 10.5);
    }

    @Test
    void list() {
        System.out.println(runner.run(param).getData());
    }
}