package com.gitee.usl.plugin.impl;

import com.gitee.usl.USLRunner;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;
import com.gitee.usl.infra.enums.ResultCode;
import com.gitee.usl.domain.Param;
import com.gitee.usl.domain.Result;
import com.gitee.usl.plugin.annotation.Length;
import com.gitee.usl.plugin.enhancer.LengthValidEnhancer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 长度校验插件测试
 * @author jingshu.zeng
 */
class LengthValidPluginTest {

    @Test
    void validLengthWithinRange() {
        // 创建一个 USLRunner，配置启用调试，并使用 LengthValidEnhancer 插件
        USLRunner runner = new USLRunner(USLRunner.defaultConfiguration()
                .setEnableDebug(Boolean.TRUE)
                .enhancer(new LengthValidEnhancer()));

        // 运行函数，传递一个符合长度范围的字符串
        Result result = runner.run(new Param("validateStringLength(\"Hello\")"));

        // 验证结果的 code 是否等于 ResultCode.SUCCESS.code()
        assertEquals(ResultCode.SUCCESS.code(), result.getCode());
    }

    @Test
    void invalidLengthOutOfRange() {
        // 创建一个 USLRunner，配置启用调试，并使用 LengthValidEnhancer 插件
        USLRunner runner = new USLRunner(USLRunner.defaultConfiguration()
                .setEnableDebug(Boolean.TRUE)
                .enhancer(new LengthValidEnhancer()));

        // 运行函数，传递一个不符合长度范围的字符串
        Result result = runner.run(new Param("validateStringLength(\"TooLongString\")"));

        // 验证结果的 code 是否等于 ResultCode.FAILURE.code()
        assertEquals(ResultCode.FAILURE.code(), result.getCode());
    }

    @FunctionGroup
    static class StringLengthValidationFunctions {

        @Function("validateStringLength")
        String validateStringLength(@Length(minLength = 5, maxLength = 10) String text) {
            return text;
        }
    }
}
