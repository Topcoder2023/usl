package com.gitee.usl.plugin.impl;

import com.gitee.usl.USLRunner;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;
import com.gitee.usl.infra.enums.ResultCode;
import com.gitee.usl.kernel.domain.Param;
import com.gitee.usl.kernel.domain.Result;
import com.gitee.usl.plugin.annotation.Type;
import com.gitee.usl.plugin.enhancer.TypeValidEnhancer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 参数类型校验注解测试
 * @author jingshu.zeng
 */
class TypeValidPluginTest {


    @Test
    void valid() {
        // 创建一个USLRunner对象，配置增强器为TypeValidEnhancer
        USLRunner runner = new USLRunner(USLRunner.defaultConfiguration()
                .setEnableDebug(Boolean.TRUE)
                .enhancer(new TypeValidEnhancer()));

        // 运行带有注解的方法
        Result result = runner.run(new Param("testType('hello')"));

        // 校验结果
        assertEquals(ResultCode.FAILURE.code(), result.getCode());
    }

    /**
     * 定义一个包含带有@Type注解的方法的测试函数组
     */
    @FunctionGroup
    static class TypeFunctionTest {
        @Function("testType")
        String testType(@Type(value = Integer.class) String str) {
            return str;
        }
    }
}
