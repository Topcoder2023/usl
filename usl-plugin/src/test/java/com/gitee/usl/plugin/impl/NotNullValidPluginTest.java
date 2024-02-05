package com.gitee.usl.plugin.impl;

import com.gitee.usl.USLRunner;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;
import com.gitee.usl.infra.enums.ResultCode;
import com.gitee.usl.kernel.domain.Param;
import com.gitee.usl.kernel.domain.Result;
import com.gitee.usl.plugin.annotation.NotNull;
import com.gitee.usl.plugin.enhancer.NotNullValidEnhancer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * NotNull注解测试
 * 作者：jingshu.zeng
 */
class NotNullValidPluginTest {
    @Test
    void valid() {
        USLRunner runner = new USLRunner(USLRunner.defaultConfiguration()
                .setEnableDebug(Boolean.TRUE)
                .enhancer(new NotNullValidEnhancer())); // 使用 NotNullEnhancer

        Result result = runner.run(new Param("validateNotNull()"));
        assertEquals(ResultCode.FAILURE.code(), result.getCode());
    }

    @FunctionGroup
    static class NotNullFunctionTest {
        @Function("validateNotNull")
        void validateNotNull(@NotNull String value) {
        }
    }
}
