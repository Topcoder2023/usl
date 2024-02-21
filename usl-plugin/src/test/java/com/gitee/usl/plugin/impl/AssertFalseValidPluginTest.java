package com.gitee.usl.plugin.impl;

import com.gitee.usl.USLRunner;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;
import com.gitee.usl.infra.enums.ResultCode;
import com.gitee.usl.domain.Param;
import com.gitee.usl.domain.Result;
import com.gitee.usl.plugin.annotation.AssertFalse;
import com.gitee.usl.plugin.enhancer.AssertFalseValidEnhancer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 假值注解测试
 * @author jingshu.zeng
 */
class AssertFalseValidPluginTest {

    @Test
    void valid() {
        USLRunner runner = new USLRunner(USLRunner.defaultConfiguration()
                .setEnableDebug(Boolean.TRUE)
                .enhancer(new AssertFalseValidEnhancer()));

        Result result = runner.run(new Param("ValidateAssertFalse(6 > 7)"));
        assertEquals(ResultCode.SUCCESS.code(), result.getCode());
    }

    @FunctionGroup
    static class AssertFalseFunctionTest {

        @Function("ValidateAssertFalse")
        boolean ValidateAssertFalse(@AssertFalse boolean isInvalid) {
            return isInvalid;
        }
    }
}
