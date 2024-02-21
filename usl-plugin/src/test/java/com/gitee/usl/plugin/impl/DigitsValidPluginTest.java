package com.gitee.usl.plugin.impl;

import com.gitee.usl.USLRunner;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;
import com.gitee.usl.infra.enums.ResultCode;
import com.gitee.usl.domain.Param;
import com.gitee.usl.domain.Result;
import com.gitee.usl.plugin.annotation.Digits;
import com.gitee.usl.plugin.enhancer.DigitsValidEnhancer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 数字格式注解测试
 * @author jingshu.zeng
 */
class DigitsValidPluginTest {

    @Test
    void valid() {
        USLRunner runner = new USLRunner(USLRunner.defaultConfiguration()
                .setEnableDebug(Boolean.TRUE)
                .enhancer(new DigitsValidEnhancer()));

        // 修改参数为字符串形式，确保小数点后的零不被省略
        Result result = runner.run(new Param("validateDigits(201.24)"));
        assertEquals(ResultCode.FAILURE.code(), result.getCode());
    }

    @FunctionGroup
    static class DigitsFunctionTest {
        @Function("validateDigits")
        double validateDigits(@Digits(integer = 2, fraction = 2) double num) {
            return num;
        }

    }
}
