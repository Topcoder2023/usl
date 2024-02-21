package com.gitee.usl.plugin.impl;

import com.gitee.usl.USLRunner;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;
import com.gitee.usl.infra.enums.ResultCode;
import com.gitee.usl.domain.Param;
import com.gitee.usl.domain.Result;
import com.gitee.usl.plugin.annotation.Null;
import com.gitee.usl.plugin.enhancer.NullValidEnhancer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 空注解测试
 *
 * @author jiahao.song
 */
class NullValidPluginTest {

    @Test
    void valid() {
        USLRunner runner = new USLRunner(USLRunner.defaultConfiguration()
                .setEnableDebug(Boolean.TRUE)
                .enhancer(new NullValidEnhancer()));

        Result result = runner.run(new Param("test11('ab')"));
        assertEquals(ResultCode.FAILURE.code(), result.getCode());
    }

    @FunctionGroup
    static class NullFunctionTest {

        @Function("test11")
        void test (@Null Object obj) {

        }
    }
}