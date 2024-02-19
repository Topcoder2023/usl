package com.gitee.usl.plugin.impl;

import com.gitee.usl.USLRunner;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;
import com.gitee.usl.infra.enums.ResultCode;
import com.gitee.usl.kernel.domain.Param;
import com.gitee.usl.kernel.domain.Result;
import com.gitee.usl.plugin.annotation.AssertTrue;
import com.gitee.usl.plugin.enhancer.AssertTrueValidEnhancer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 真值注解测试
 * @author jingshu.zeng
 */
class AssertTrueValidPluginTest {

    @Test
    void valid() {
        USLRunner runner = new USLRunner(USLRunner.defaultConfiguration()
                .setEnableDebug(Boolean.TRUE)
                .enhancer(new AssertTrueValidEnhancer()));

        Result result = runner.run(new Param("ValidateAssertTrue(7 > 6)"));
        assertEquals(ResultCode.SUCCESS.code(), result.getCode());
    }

    @FunctionGroup
    static class AssertTrueFunctionTest {

        @Function("ValidateAssertTrue")
        boolean ValidateAssertTrue(@AssertTrue boolean isValid) {
            return isValid;
        }
    }
}
