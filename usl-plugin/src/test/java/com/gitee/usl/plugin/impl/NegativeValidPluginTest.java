package com.gitee.usl.plugin.impl;

import com.gitee.usl.USLRunner;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;
import com.gitee.usl.infra.enums.ResultCode;
import com.gitee.usl.domain.Param;
import com.gitee.usl.domain.Result;
import com.gitee.usl.plugin.annotation.Negative;
import com.gitee.usl.plugin.enhancer.NegativeValidEnhancer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 负数注解测试
 *
 * @author jiahao.song
 */
class NegativeValidPluginTest {

    @Test
    void valid() {
        USLRunner runner = new USLRunner(USLRunner.defaultConfiguration()
                .setEnableDebug(Boolean.TRUE)
                .enhancer(new NegativeValidEnhancer()));

        Result result = runner.run(new Param("test12(0)"));
        assertEquals(ResultCode.FAILURE.code(), result.getCode());
    }

    @FunctionGroup
    static class NegativeFunctionTest {

        @Function("test12")
        int test(@Negative int num) {
            return num + 10;
        }
    }
}