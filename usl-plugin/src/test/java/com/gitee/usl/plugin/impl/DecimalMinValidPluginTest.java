package com.gitee.usl.plugin.impl;

import com.gitee.usl.USLRunner;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;
import com.gitee.usl.infra.enums.ResultCode;
import com.gitee.usl.kernel.domain.Param;
import com.gitee.usl.kernel.domain.Result;
import com.gitee.usl.plugin.annotation.DecimalMin;
import com.gitee.usl.plugin.enhancer.DecimalMinValidEnhancer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 最小值注解测试
 * @author jingshu.zeng
 */
class DecimalMinValidPluginTest {

    @Test
    void valid() {
        USLRunner runner = new USLRunner(USLRunner.defaultConfiguration()
                .setEnableDebug(Boolean.TRUE)
                .enhancer(new DecimalMinValidEnhancer()));

        Result result = runner.run(new Param("validateDecimalMin(45)")); // 修改输入值为大于等于50的值
        assertEquals(ResultCode.FAILURE.code(), result.getCode()); // 期望测试通过
    }

    @FunctionGroup
    static class DecimalMinFunctionTest {
        @Function("validateDecimalMin")
        int validateDecimalMin(@DecimalMin("50") int num) { // 修改注解的最大值为50
            return num;
        }
    }
}
