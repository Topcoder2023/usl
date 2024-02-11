package com.gitee.usl.plugin.impl;

import com.gitee.usl.USLRunner;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;
import com.gitee.usl.infra.enums.ResultCode;
import com.gitee.usl.kernel.domain.Param;
import com.gitee.usl.kernel.domain.Result;
import com.gitee.usl.plugin.annotation.NegativeOrZero;
import com.gitee.usl.plugin.enhancer.NegativeOrZeroValidEnhancer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 非正注解测试
 *
 * @author jiahao.song
 */
class NegativeOrZeroValidPluginTest {

    @Test
    void valid() {
        USLRunner runner = new USLRunner(USLRunner.defaultConfiguration()
                .setEnableDebug(Boolean.TRUE)
                .enhancer(new NegativeOrZeroValidEnhancer()));

        Result result = runner.run(new Param("test13(1)"));
        assertEquals(ResultCode.FAILURE.code(), result.getCode());
    }

    @FunctionGroup
    static class NegativeOrZeroFunctionTest {

        @Function("test13")
        int test(@NegativeOrZero int num) {
            return num + 10;
        }
    }
}