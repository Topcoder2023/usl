package com.gitee.usl.plugin.impl;

import com.gitee.usl.USLRunner;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;
import com.gitee.usl.infra.enums.ResultCode;
import com.gitee.usl.domain.Param;
import com.gitee.usl.plugin.annotation.Size;
import com.gitee.usl.plugin.enhancer.SizeValidEnhancer;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 长度校验插件测试
 */
class SizeValidPluginTest {

    @Test
    void valid() {
        USLRunner runner = new USLRunner(USLRunner.defaultConfiguration()
                .setEnableDebug(Boolean.TRUE)
                .enhancer(new SizeValidEnhancer()));

        Map<String, Object> var = new HashMap<>();
        var.put("key1", "value1");
        var.put("key2", "value2");
        assertEquals(ResultCode.FAILURE.code(), runner.run(new Param("test3(var)")
                .addContext("var", var)).getCode());

        assertEquals(ResultCode.FAILURE.code(), runner.run(new Param("test4('abc')")).getCode());

        List<String> var1 = Arrays.asList("value1", "value2", "value3", "value4");
        assertEquals(ResultCode.FAILURE.code(), runner.run(new Param("test5(var)")
                .addContext("var", var1)).getCode());
    }


    @FunctionGroup
    static class SizeValidationFunctions {

        @Function("test3")
        void test(@Size(5) Map<?, ?> obj) {
        }

        @Function("test4")
        void test(@Size(min=5) String obj) {
        }

        @Function("test5")
        void test(@Size(min=5, max=10) Collection<?> obj) {
        }
    }
}
