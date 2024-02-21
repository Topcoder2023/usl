package com.gitee.usl.plugin.impl;

import com.gitee.usl.USLRunner;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;
import com.gitee.usl.infra.enums.ResultCode;
import com.gitee.usl.kernel.domain.Param;
import com.gitee.usl.plugin.annotation.NotEmpty;
import com.gitee.usl.plugin.enhancer.NotEmptyValidEnhancer;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 非空注解测试
 *
 * @author hongda.li
 */
class NotEmptyValidPluginTest {
    @Test
    void valid() {
        USLRunner runner = new USLRunner(USLRunner.defaultConfiguration()
                .setEnableDebug(Boolean.TRUE)
                .enhancer(new NotEmptyValidEnhancer()));

        assertEquals(ResultCode.FAILURE.code(), runner.run(new Param("test_1(var)")
                .addContext("var", null)).getCode());
        assertEquals(ResultCode.FAILURE.code(), runner.run(new Param("test_2(var)")
                .addContext("var", Collections.emptyMap())).getCode());
        assertEquals(ResultCode.FAILURE.code(), runner.run(new Param("test_3(var)")
                .addContext("var", Collections.emptyList())).getCode());
        assertEquals(ResultCode.FAILURE.code(), runner.run(new Param("test_4(var)")
                .addContext("var", "")).getCode());
    }

    @FunctionGroup
    static class MaxFunctionTest {

        @Function("test_1")
        void test1(@NotEmpty Object obj) {
        }

        @Function("test_2")
        void test2(@NotEmpty Map<?, ?> obj) {
        }

        @Function("test_3")
        void test3(@NotEmpty List<?> obj) {
        }

        @Function("test_4")
        void test4(@NotEmpty String obj) {
        }

    }
}