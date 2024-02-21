package com.gitee.usl.plugin.impl;

import cn.hutool.core.date.DateTime;
import com.gitee.usl.USLRunner;
import com.gitee.usl.api.annotation.Function;
import com.gitee.usl.api.annotation.FunctionGroup;
import com.gitee.usl.infra.enums.ResultCode;
import com.gitee.usl.kernel.domain.Param;
import com.gitee.usl.kernel.domain.Result;
import com.gitee.usl.plugin.annotation.Past;
import com.gitee.usl.plugin.enhancer.PastValidEnhancer;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 时间注解测试
 *
 * @author jiahao.song
 */
class PastValidPluginTest {

    @Test
    void valid() {
        USLRunner runner = new USLRunner(USLRunner.defaultConfiguration()
                .setEnableDebug(Boolean.TRUE)
                .enhancer(new PastValidEnhancer()));

        Result result = runner.run(new Param("test9('2025-01-01T00:00')"));
        assertEquals(ResultCode.FAILURE.code(), result.getCode());
    }

    @FunctionGroup
    static class PastFunctionTest {

        @Function("test9")
        LocalDateTime testPast(@Past LocalDateTime dateTime) {
            return dateTime;
        }
    }
}