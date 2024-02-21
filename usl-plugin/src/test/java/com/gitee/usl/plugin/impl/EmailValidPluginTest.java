package com.gitee.usl.plugin.impl;

import com.gitee.usl.USLRunner;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;
import com.gitee.usl.infra.enums.ResultCode;
import com.gitee.usl.domain.Param;
import com.gitee.usl.domain.Result;
import com.gitee.usl.plugin.annotation.Email;
import com.gitee.usl.plugin.enhancer.EmailValidEnhancer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 邮箱注解测试
 * @author jingshu.zeng
 */
class EmailValidPluginTest {

    @Test
    void valid_success() {
        // 创建 USLRunner 实例并设置增强器
        USLRunner runner = new USLRunner(USLRunner.defaultConfiguration()
                .setEnableDebug(Boolean.TRUE)
                .enhancer(new EmailValidEnhancer()));

        // 构造参数并运行函数
        Result result = runner.run(new Param("validateEmail('test@example.com')"));


        // 验证结果
        assertEquals(ResultCode.SUCCESS.code(), result.getCode());
    }

    @Test
    void valid_fail() {
        // 创建 USLRunner 实例并设置增强器
        USLRunner runner = new USLRunner(USLRunner.defaultConfiguration()
                .setEnableDebug(Boolean.TRUE)
                .enhancer(new EmailValidEnhancer()));

        // 构造参数并运行函数
        Result result = runner.run(new Param("validateEmail('test@example..com')"));


        // 验证结果
        assertEquals(ResultCode.FAILURE.code(), result.getCode());
    }

    @FunctionGroup
    static class EmailFunctionTest {
        @Function("validateEmail")
        void validateEmail(@Email String email) {
        }
    }
}
