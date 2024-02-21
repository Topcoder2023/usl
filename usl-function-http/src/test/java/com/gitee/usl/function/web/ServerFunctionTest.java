package com.gitee.usl.function.web;

import com.gitee.usl.USLRunner;
import com.gitee.usl.kernel.domain.Param;
import com.gitee.usl.kernel.domain.ResourceParam;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author hongda.li
 */
class ServerFunctionTest {
    static USLRunner runner;

    public static void main(String[] args) {
        ResourceParam param = new ResourceParam("test.js");
        runner = new USLRunner();
        runner.start();

        System.out.println(runner.run(param));
    }

    @Test
    void serverTest() {
        assertDoesNotThrow(() -> runner.run(new Param("server(10001)")));
    }
}