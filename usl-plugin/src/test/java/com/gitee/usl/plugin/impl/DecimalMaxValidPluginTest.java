package com.gitee.usl.plugin.impl;

import com.gitee.usl.USLRunner;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;
import com.gitee.usl.infra.enums.ResultCode;
import com.gitee.usl.domain.Param;
import com.gitee.usl.domain.Result;
import com.gitee.usl.plugin.annotation.DecimalMax;
import com.gitee.usl.plugin.enhancer.DecimalMaxValidEnhancer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 最大值注解测试
 * @author jingshu.zeng
 */
class DecimalMaxValidPluginTest {

    @Test
    void valid() {
        USLRunner runner = new USLRunner(USLRunner.defaultConfiguration()
                .setEnableDebug(Boolean.TRUE)
                .enhancer(new DecimalMaxValidEnhancer()));

        Result result = runner.run(new Param("validateDecimalMax(55)")); // 修改输入值为大于等于50的值
        assertEquals(ResultCode.FAILURE.code(), result.getCode()); // 期望测试通过
    }

    @FunctionGroup
    static class DecimalMaxFunctionTest {
        @Function("validateDecimalMax")
        int validateDecimalMax(@DecimalMax("50") int num) { // 修改注解的最大值为50
            return num;
        }
    }
}
