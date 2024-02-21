package com.gitee.usl.plugin.impl;

import com.gitee.usl.USLRunner;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;
import com.gitee.usl.infra.enums.ResultCode;
import com.gitee.usl.kernel.domain.Param;
import com.gitee.usl.kernel.domain.Result;
import com.gitee.usl.plugin.annotation.PositiveOrZero;
import com.gitee.usl.plugin.enhancer.PositiveOrZeroValidEnhancer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 非负注解测试
 *
 * @author jiahao.song
 */
class PositiveOrZeroValidPluginTest {

    @Test
    void valid() {
        USLRunner runner = new USLRunner(USLRunner.defaultConfiguration()
                .setEnableDebug(Boolean.TRUE)
                .enhancer(new PositiveOrZeroValidEnhancer()));

        Result result = runner.run(new Param("test7(-1)"));
        assertEquals(ResultCode.FAILURE.code(), result.getCode());
    }

    @FunctionGroup
    static class PositiveOrZeroFunctionTest {

        @Function("test7")
        int test(@PositiveOrZero int num) {
            return num + 10;
        }
    }
}